package main.java.application;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.ui.MainView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new MainView(primaryStage); // Initialize the main UI view
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
