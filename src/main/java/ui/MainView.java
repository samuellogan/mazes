package main.java.ui;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.java.generator.RecursiveBacktracker;
import main.java.solver.AStarSolver;

public class MainView {

    private Canvas canvas;
    private Stage primaryStage;
    private int[][] currentMaze;

    public MainView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeView();
    }

    private void initializeView() {
        primaryStage.setTitle("Maze Viewer");

        StackPane mainLayout = new StackPane();
        canvas = new Canvas(750, 550);
        mainLayout.getChildren().add(canvas);

        Scene mainScene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(mainScene);

        openSettingsWindow();
    }

    private void drawMaze(int[][] maze) {
        currentMaze = maze;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        int tilesX = maze[0].length;
        int tilesY = maze.length;

        double cellWidth = canvasWidth / tilesX;
        double cellHeight = canvasHeight / tilesY;

        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                switch (maze[row][col]) {
                    case 1: // Wall
                        gc.setFill(Color.BLACK);
                        break;
                    case 2: // Visited
                        gc.setFill(Color.GRAY);
                        break;
                    case 3: // Solution
                        gc.setFill(Color.GREEN);
                        break;
                    case 4: // Start
                        gc.setFill(Color.BLUE);
                        break;
                    case 5: // End
                        gc.setFill(Color.RED);
                        break;
                    default: // Path
                        gc.setFill(Color.WHITE);
                        break;
                }
                gc.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }
    }

    private void openSettingsWindow() {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings Panel");

        Button generateButton = new Button("Generate Maze");
        generateButton.setOnAction(event -> {
            RecursiveBacktracker generator = new RecursiveBacktracker();
            int[][] maze = generator.generateMaze(75, 45); // Adjust size as needed
            drawMaze(maze);
        });

        Button solveButton = new Button("Solve Maze");
        solveButton.setOnAction(event -> {
            if (currentMaze != null) {
                // Find start (4) and end (5) positions
                int startX = -1, startY = -1, endX = -1, endY = -1;
                for (int row = 0; row < currentMaze.length; row++) {
                    for (int col = 0; col < currentMaze[row].length; col++) {
                        if (currentMaze[row][col] == 4) { // Start
                            startX = col;
                            startY = row;
                        } else if (currentMaze[row][col] == 5) { // End
                            endX = col - 1;
                            endY = row;
                        }
                    }
                }

                // Ensure start and end were found
                if (startX != -1 && startY != -1 && endX != -1 && endY != -1) {
                    AStarSolver solver = new AStarSolver();
                    int[][] solvedMaze = solver.solveMaze(currentMaze, startX, startY, endX, endY);
                    drawMaze(solvedMaze);
                } else {
                    System.out.println("Start or end not found in the maze.");
                }
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(generateButton, solveButton);

        Scene scene = new Scene(layout, 200, 150); // Adjusted for additional button
        settingsStage.setScene(scene);
        settingsStage.show();
    }

}
