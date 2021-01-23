package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public  class Main extends Application {
    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene((Parent) FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml")));
        stage.setTitle("Task Manager ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}