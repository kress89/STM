package sample;

import data.LiveChartCpuUsage;
import data.ProcessManagerProcessHandle;
import data.RamMemory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.VirtualMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable, GlobalMemory {
	final private ObservableList<ProcessManagerProcessHandle> processList = FXCollections.observableArrayList();
	private SystemInfo si;
	private OperatingSystem os;

	@FXML
	private TableView<ProcessManagerProcessHandle> processView;

	@FXML
	private TableColumn<ProcessManagerProcessHandle, String> pidTableColumn;

	@FXML
	private TableColumn<ProcessManagerProcessHandle, String> cpuTableColumn;

	@FXML
	private TableColumn<ProcessManagerProcessHandle, String> vszTableColumn;

	@FXML
	private TableColumn<ProcessManagerProcessHandle, String> rssTableColumn;

	@FXML
	private TableColumn<ProcessManagerProcessHandle, String> nameTableColumn;

	@FXML
	private TableColumn<ProcessManagerProcessHandle, String> memTableColumn;

	@FXML
	private ScrollPane pane;

	public Controller() {
		si = new SystemInfo();
		os = si.getOperatingSystem();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		List<OSProcess> processes = os.getProcesses(100, OperatingSystem.ProcessSort.PID);
		RamMemory ramInfo = new RamMemory();
		for (int i = 0; i < processes.size() && i < 100; i++) {
			OSProcess p = processes.get(i);
			double cpu = 100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime();
			double memoryUsage = 100d * p.getResidentSetSize() / ramInfo.getRamInfo().getMaxRam();
			String virtualMemory = FormatUtil.formatBytes(p.getVirtualSize());
			String rssMemory = FormatUtil.formatBytes(p.getResidentSetSize());
			String name = p.getName();
			processList.add(new ProcessManagerProcessHandle(p.getProcessID(), String.format("%5.2f", cpu),
					String.format("%4.2f", memoryUsage), virtualMemory, rssMemory, name));
		}

		pidTableColumn.setCellValueFactory(cd -> new SimpleStringProperty(Long.toString(cd.getValue().getPid())));
		cpuTableColumn.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getCpu())));
		memTableColumn
				.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getMemoryUsage())));
		vszTableColumn
				.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getVirtualMemory())));
		rssTableColumn.setCellValueFactory(
				cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getResidentSetSizeMemory())));
		nameTableColumn.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getName())));

		processView.setItems(processList);
		processView.setRowFactory(tv -> {
			TableRow<ProcessManagerProcessHandle> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					ProcessManagerProcessHandle rowData = row.getItem();
					LiveChartCpuUsage chart = new LiveChartCpuUsage(rowData);
					try {
						chart.start(new Stage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			return row;
		});
	}

	@Override
	public long getTotal() {
		return 0;
	}

	@Override
	public long getAvailable() {
		return 0;
	}

	@Override
	public long getPageSize() {
		return 0;
	}

	@Override
	public VirtualMemory getVirtualMemory() {
		return null;
	}

	@Override
	public List<PhysicalMemory> getPhysicalMemory() {
		return null;
	}
}