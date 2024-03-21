package main.java.application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        setupMainMazeViewer(primaryStage);
        openSettingsWindow();
    }

    private void setupMainMazeViewer(Stage primaryStage) {
        primaryStage.setTitle("Maze Viewer");

        StackPane mainLayout = new StackPane();

        Scene mainScene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void openSettingsWindow() {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings Panel");

        Button btnSave = new Button("Button");
        btnSave.setOnAction(event -> System.out.println("Button Clicked"));

        HBox settingsLayout = new HBox(10);
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.getChildren().add(btnSave);

        Scene settingsScene = new Scene(settingsLayout, 300, 200);
        settingsStage.setScene(settingsScene);
        settingsStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
