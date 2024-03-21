package main.java.generator;

import java.util.ArrayList;
import java.util.Collections;

public class RecursiveBacktracker implements MazeGenerator {

    @Override
    public int[][] generateMaze(int width, int height) {
        // initialize maze with walls (1s)
        int[][] maze = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        // start generation from the top-left cell (or any cell you prefer)
        recursiveBacktrack(maze, 1, 1);

        maze[1][0] = 4;
        maze[height - 2][width - 1] = 5;

        return maze;
    }

    /**
     * recursive backtracking algorithm to generate a maze
     * 
     * @param maze the maze to generate
     * @param row  the current row
     * @param col  the current column
     */
    private void recursiveBacktrack(int[][] maze, int row, int col) {
        // directions: up, right, down, left
        int[][] directions = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
        // mark the current cell as a path (0)
        maze[row][col] = 0;

        // randomize the directions to ensure maze randomness
        ArrayList<Integer> directionIndexes = new ArrayList<>();
        for (int i = 0; i < directions.length; i++)
            directionIndexes.add(i);
        Collections.shuffle(directionIndexes);

        // explore the neighbors
        for (int i : directionIndexes) {
            // move two steps in the direction
            int newRow = row + directions[i][0] * 2;
            int newCol = col + directions[i][1] * 2;

            // check if the new position is within the maze bounds and has not been visited
            if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length
                    && maze[newRow][newCol] == 1) {
                // remove the wall between the current cell and the new cell
                maze[row + directions[i][0]][col + directions[i][1]] = 0;
                // recurse from the new cell
                recursiveBacktrack(maze, newRow, newCol);
            }
        }
    }
}
