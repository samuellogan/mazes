package main.java.ui;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import main.java.solver.*;
import main.java.generator.*;

public class MainView {

    private Canvas canvas;
    private Stage primaryStage;
    private int[][] currentMaze;
    private boolean isSolved;

    public MainView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeView();
    }

    /**
     * loads the UI for the program
     */
    private void initializeView() {
        primaryStage.setTitle("Maze Viewer");

        StackPane mainLayout = new StackPane();
        canvas = new Canvas(750, 550);
        mainLayout.getChildren().add(canvas);

        Scene mainScene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(mainScene);

        openSettingsWindow();
    }

    /**
     * draws a maze to the canvas
     * 
     * @param maze the maze to draw
     */
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
                    case 1: // wall
                        gc.setFill(Color.BLACK);
                        break;
                    case 2: // visited
                        gc.setFill(Color.GRAY);
                        break;
                    case 3: // solution
                        gc.setFill(Color.GREEN);
                        break;
                    case 4: // start
                        gc.setFill(Color.BLUE);
                        break;
                    case 5: // end
                        gc.setFill(Color.RED);
                        break;
                    default: // path
                        gc.setFill(Color.WHITE);
                        break;
                }
                gc.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }
    }

    /**
     * loads the Settings window with its UI
     */
    private void openSettingsWindow() {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Settings Panel");

        // dropdown for selecting the maze generator
        ComboBox<String> generatorDropdown = new ComboBox<>();
        generatorDropdown.getItems().addAll("Recursive Backtracker", "Other Generator");

        // dropdown for selecting the maze solver
        ComboBox<String> solverDropdown = new ComboBox<>();
        solverDropdown.getItems().addAll("A* Solver", "Other Solver");

        // input fields for sizeX and sizeY
        TextField sizeXInput = new TextField("25");
        TextField sizeYInput = new TextField("15");
        sizeXInput.setPrefWidth(50);
        sizeYInput.setPrefWidth(50);

        Button generateButton = new Button("Generate Maze");
        generateButton.setOnAction(event -> {
            String selectedGenerator = generatorDropdown.getValue();
            MazeGenerator generator = getGenerator(selectedGenerator);
            int sizeX = Integer.parseInt(sizeXInput.getText()); // TODO: add validation
            int sizeY = Integer.parseInt(sizeYInput.getText()); // TODO: add validation
            int[][] maze = generator.generateMaze(sizeX, sizeY);
            drawMaze(maze);

            isSolved = false;
        });

        Button solveButton = new Button("Solve Maze");
        solveButton.setOnAction(event -> {
            if (isSolved)
                return;

            String selectedSolver = solverDropdown.getValue();
            MazeSolver solver = getSolver(selectedSolver);
            if (currentMaze != null) {

                // find start (4) and end (5) positions
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

                // ensure start and end were found
                if (startX != -1 && startY != -1 && endX != -1 && endY != -1) {
                    int[][] solvedMaze = solver.solveMaze(currentMaze, startX, startY, endX, endY);
                    drawMaze(solvedMaze);
                } else {
                    System.out.println("Start or end not found in the maze.");
                }
            }

            isSolved = true;
        });

        HBox sizeLayout = new HBox(5);
        sizeLayout.getChildren().addAll(new Label("Size X:"), sizeXInput, new Label("Size Y:"), sizeYInput);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Select Generator:"), generatorDropdown,
                new Label("Select Solver:"), solverDropdown,
                sizeLayout,
                generateButton, solveButton);

        Scene scene = new Scene(layout, 300, 200);
        settingsStage.setScene(scene);
        settingsStage.show();
    }

    /**
     * finds a MazeGenerator by its name
     * 
     * @param selectedSolver the name to search for
     * @return the generator
     */
    private MazeGenerator getGenerator(String selectedGenerator) {
        switch (selectedGenerator) {
            case "Recursive Backtracker":
                return new RecursiveBacktracker();
            default:
                return null;
        }
    }

    /**
     * finds a MazeSolver by its name
     * 
     * @param selectedSolver the name to search for
     * @return the solver
     */
    private MazeSolver getSolver(String selectedSolver) {
        switch (selectedSolver) {
            case "A* Solver":
                return new AStarSolver();
            default:
                return null;
        }
    }
}