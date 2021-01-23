package data;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LiveChartCpuUsage extends Application {
	final int WINDOW_SIZE = 10;
	private ScheduledExecutorService scheduledExecutorService;
	private ProcessManagerProcessHandle rowData;
	private Stage gcStage;
	private Scene gcScene;
	private TextArea gcTextArea;
	private ScrollPane pane;

	public LiveChartCpuUsage(ProcessManagerProcessHandle rowData) {
		this.rowData = rowData;
		gcStage = new Stage();
		gcTextArea = new TextArea();
		gcTextArea.setPrefColumnCount(80);
		gcTextArea.setPrefRowCount(60);
		pane = new ScrollPane();
		pane.setContent(gcTextArea);
		gcScene = new Scene(pane, 400, 300);
		gcStage.setScene(gcScene);
		gcStage.setX(800);
		gcStage.setY(500);
		gcStage.setTitle("JavaFX Garbage Collector Clean Output");
		gcStage.hide();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("JavaFX Realtime Chart Demo");

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Time/s");
		xAxis.setAnimated(false);
		yAxis.setLabel("CPU usage %");
		yAxis.setAnimated(false);

		final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle("Realtime CPU usage");
		lineChart.setAnimated(false);

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Data Series");
		lineChart.getData().add(series);

		Scene scene = new Scene(lineChart, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(() -> {
			Platform.runLater(() -> {
				int random = 0;
				try {
					random = (int) getLiveCpuUsage();
				} catch (Exception e) {
					e.printStackTrace();
				}

				Date now = new Date();
				series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), random));

				if (series.getData().size() > WINDOW_SIZE)
					series.getData().remove(0);

				if (!primaryStage.isShowing()) {
					try {
						stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
					primaryStage.close();
				}
			});
		}, 0, 1, TimeUnit.SECONDS);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		scheduledExecutorService.shutdownNow();
		scheduledExecutorService = null;
		gcStage.close();
		gcStage = null;
		gcScene = null;
		gcTextArea = null;
	}

	private long getLiveCpuUsage() throws Exception {
		SystemInfo si = new SystemInfo();
		OperatingSystem os = si.getOperatingSystem();
		List<OSProcess> processes = os.getProcesses(100, OperatingSystem.ProcessSort.CPU);
		boolean isAnyOver80Percent = processes.stream().anyMatch(p -> {
			double cpu = 100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime();
			if (cpu >= 80.01) {
				return true;
			} else {
				return false;
			}
		});

		if (isAnyOver80Percent) {
			gcStage.show();
			zapisiLog();
		}

		Optional<OSProcess> p = processes.stream().filter(pr -> pr.getProcessID() == rowData.getPid()).findFirst();
		if (p.isPresent()) {
			double cpu = 100d * (p.get().getKernelTime() + p.get().getUserTime()) / p.get().getUpTime();
			return (long) cpu;
		} else {
			return 0;
		}
	}

	private void zapisiLog() {
		String text = gcTextArea.getText();
		text += "\nFree bytes prior to gc(): " + Runtime.getRuntime().freeMemory();
		gcTextArea.setText(text);
		System.gc();
		text = gcTextArea.getText();
		text += "\nFreee bytes after the gc(): " + Runtime.getRuntime().freeMemory();
		gcTextArea.setText(text);
	}
}