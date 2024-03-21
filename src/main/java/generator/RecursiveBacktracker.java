package main.java.generator;

import java.util.ArrayList;
import java.util.Collections;

public class RecursiveBacktracker implements MazeGenerator {

    @Override
    public int[][] generateMaze(int width, int height) {
        // Initialize maze with walls (1s)
        int[][] maze = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        // Start generation from the top-left cell (or any cell you prefer)
        recursiveBacktrack(maze, 1, 1);

        maze[1][0] = 4;
        maze[height - 2][width - 1] = 5;

        return maze;
    }

    private void recursiveBacktrack(int[][] maze, int row, int col) {
        // Directions: up, right, down, left
        int[][] directions = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
        // Mark the current cell as a path (0)
        maze[row][col] = 0;

        // Randomize the directions to ensure maze randomness
        ArrayList<Integer> directionIndexes = new ArrayList<>();
        for (int i = 0; i < directions.length; i++)
            directionIndexes.add(i);
        Collections.shuffle(directionIndexes);

        // Explore the neighbors
        for (int i : directionIndexes) {
            int newRow = row + directions[i][0] * 2; // Move two steps in the direction
            int newCol = col + directions[i][1] * 2;

            // Check if the new position is within the maze bounds and has not been visited
            if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length
                    && maze[newRow][newCol] == 1) {
                // Remove the wall between the current cell and the new cell
                maze[row + directions[i][0]][col + directions[i][1]] = 0;
                // Recurse from the new cell
                recursiveBacktrack(maze, newRow, newCol);
            }
        }
    }
}
