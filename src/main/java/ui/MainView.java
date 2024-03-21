package main.java.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    private AnimationTimer animationTimer;
    private MazeGenerator generator;
    private boolean isSolved;
    private boolean isPaused = false;
    private int[][] currentMaze;

    TextField sizeXInput;
    TextField sizeYInput;

    Label generatorControlsLabel;
    Button generatorPlayButton;
    Button generatorPauseButton;
    Button generatorStepButton;
    Button generatorRestartButton;
    Slider generatorDelaySlider;

    ComboBox<String> generatorDropdown;

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
        mainLayout.setStyle("-fx-background-color: black;"); // Set the background color to black

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
                        gc.setFill(Color.YELLOW);
                        break;
                    case 4: // start
                        gc.setFill(Color.GREEN);
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
        generatorDropdown = new ComboBox<>();
        generatorDropdown.getItems().addAll("Recursive Backtracker", "Prims Generator");
        generatorDropdown.setOnAction(event -> {
            clearAndInitialize();
        });

        // dropdown for selecting the maze solver
        ComboBox<String> solverDropdown = new ComboBox<>();
        solverDropdown.getItems().addAll("A* Solver", "Other Solver");

        // input fields for sizeX and sizeY
        sizeXInput = new TextField("25");
        sizeYInput = new TextField("15");
        sizeXInput.setPrefWidth(50);
        sizeYInput.setPrefWidth(50);
        sizeXInput.setOnAction(event -> {
            clearAndInitialize();
        });
        sizeYInput.setOnAction(event -> {
            clearAndInitialize();
        });

        // Define button actions
        generatorControlsLabel = new Label("Generator Controls");
        generatorPauseButton = new Button("Pause");
        generatorPlayButton = new Button("Play");
        generatorStepButton = new Button("Step");
        generatorRestartButton = new Button("Restart");
        generatorDelaySlider = new Slider(0, 500, 100);

        generatorPauseButton.setOnAction(event -> {
            isPaused = true;
            generatorPauseButton.setDisable(true);
            generatorPlayButton.setDisable(false);
            generatorStepButton.setDisable(false);
        });

        generatorPlayButton.setOnAction(event -> {
            if (currentMaze == null || generator == null) {
                // Initialize the maze and generator
                String selectedGenerator = generatorDropdown.getValue();
                generator = getGenerator(selectedGenerator);
                int sizeX = Integer.parseInt(sizeXInput.getText());
                int sizeY = Integer.parseInt(sizeYInput.getText());
                currentMaze = new int[sizeY][sizeX];
                generator.initializeMaze(currentMaze);

                // Setup and start the animation timer if not already running
                setupAndStartAnimationTimer();
            }

            isPaused = false;
            generatorPauseButton.setDisable(false);
            generatorPlayButton.setDisable(true);
            generatorRestartButton.setDisable(false);
            generatorStepButton.setDisable(true);
        });

        generatorStepButton.setOnAction(event -> {
            if (isPaused) {
                // Perform a single generation step. This requires you to extract the step logic
                // into a method that can be called here.
                boolean shouldContinue = generator.generateMazeTick(currentMaze);
                drawMaze(currentMaze);
                if (!shouldContinue) {
                    animationTimer.stop(); // If the generation is complete, stop the timer
                }
            }
        });

        generatorRestartButton.setOnAction(event -> {
            clearAndInitialize();
        });

        generatorDelaySlider.setShowTickMarks(true);
        generatorDelaySlider.setShowTickLabels(true);
        generatorDelaySlider.setMajorTickUnit(10);
        generatorDelaySlider.setBlockIncrement(1);

        HBox generatorButtonBox = new HBox(10, generatorPlayButton, generatorPauseButton,
                generatorStepButton, generatorRestartButton);
        VBox generatorControlCluster = new VBox(10, generatorControlsLabel, generatorButtonBox, generatorDelaySlider);

        HBox sizeLayout = new HBox(5);
        sizeLayout.getChildren().addAll(new Label("Size X:"), sizeXInput, new Label("Size Y:"), sizeYInput);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Select Generator:"), generatorDropdown,
                new Label("Select Solver:"), solverDropdown,
                sizeLayout,
                generatorControlCluster);

        Scene scene = new Scene(layout, 300, 600);
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
            case "Prims Generator":
                return new PrimsGenerator();
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

    private void setupAndStartAnimationTimer() {
        animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                long delay = (long) (generatorDelaySlider.getValue() * 1_000_000); // Adjusted for slider value
                if (!isPaused && now - lastUpdate >= delay) {
                    boolean shouldContinue = generator.generateMazeTick(currentMaze);
                    drawMaze(currentMaze);
                    lastUpdate = now;
                    if (!shouldContinue) {
                        this.stop();
                        generatorPlayButton.setDisable(true); // Optionally disable play when done
                        generatorPauseButton.setDisable(true);
                        generatorRestartButton.setDisable(false);
                    }
                }
            }
        };
        animationTimer.start();
    }

    private void clearAndInitialize() {
        if (animationTimer != null) {
            animationTimer.stop();
        }

        int sizeX = Integer.parseInt(sizeXInput.getText());
        int sizeY = Integer.parseInt(sizeYInput.getText());
        currentMaze = new int[sizeY][sizeX];

        String selectedGenerator = generatorDropdown.getValue();
        this.generator = getGenerator(selectedGenerator);
        this.generator.initializeMaze(currentMaze);

        setupAndStartAnimationTimer();
        drawMaze(currentMaze);

        isPaused = true;
        generatorPlayButton.setDisable(false);
        generatorPauseButton.setDisable(true);
        generatorStepButton.setDisable(false);
        generatorRestartButton.setDisable(false);
    }

}