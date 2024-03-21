package main.java.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import main.java.application.Main;

public class RecursiveBacktracker implements MazeGenerator {

    private Stack<int[]> stack = new Stack<>();
    private boolean initialized = false;

    public boolean generateMazeTick(int[][] maze) {
        if (!initialized) {
            initializeMaze(maze);
            initialized = true;
        }

        while (!stack.isEmpty()) {
            int[] current = stack.peek(); // Use peek to look at the top of the stack without removing it
            int row = current[0];
            int col = current[1];
            List<int[]> validDirections = new ArrayList<>();

            for (int[] direction : new int[][] { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } }) {
                int newRow = row + direction[0] * 2;
                int newCol = col + direction[1] * 2;

                // Check if the new position is within bounds and is a wall
                if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length
                        && maze[newRow][newCol] == Main.WALL) {
                    validDirections.add(direction);
                }
            }

            if (!validDirections.isEmpty()) {
                // if there are valid directions to extend the maze, choose one at random
                Collections.shuffle(validDirections);
                int[] direction = validDirections.get(0);
                int newRow = row + direction[0] * 2;
                int newCol = col + direction[1] * 2;

                // remove the wall and extend the path
                maze[row + direction[0]][col + direction[1]] = Main.PATH;
                maze[newRow][newCol] = Main.PATH;

                // push the new cell onto the stack
                stack.push(new int[] { newRow, newCol });

                // a step has been made, return to update the UI
                return true;
            } else {
                // if no valid directions, this path is a dead end. pop and try another.
                stack.pop();
            }
        }

        // if the stack is empty, the maze is complete
        MazeUtils.setStartAndEndPoints(maze);
        return false;
    }

    public void initializeMaze(int[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = Main.WALL;
            }
        }
        // Optionally, choose a random starting cell
        int startRow = 1; // Example: start at the top-left corner
        int startCol = 1;
        maze[startRow][startCol] = Main.PATH;
        stack.push(new int[] { startRow, startCol });
    }
}
