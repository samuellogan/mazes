package main.java.application;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.ui.MainView;

public class Main extends Application {
    public static final int PATH = 0;
    public static final int WALL = 1;
    public static final int VISITED = 10;
    public static final int SOLUTION = 11;
    public static final int START = 20;
    public static final int END = 21;

    public static byte[][] maze;

    @Override
    public void start(Stage primaryStage) {
        new MainView(primaryStage); // Initialize the main UI view
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
