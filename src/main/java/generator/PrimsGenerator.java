package main.java.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.application.Main;

public class PrimsGenerator implements MazeGenerator {

    private List<int[]> walls;
    private boolean[][] visited;

    @Override
    public void initializeMaze(int[][] maze) {
        walls = new ArrayList<>();
        visited = new boolean[maze.length][maze[0].length];

        // Initialize all cells as walls
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = Main.WALL;
            }
        }

        // Start from a random cell
        int startRow = (int) (Math.random() * maze.length);
        int startCol = (int) (Math.random() * maze[0].length);
        maze[startRow][startCol] = Main.PATH;
        visited[startRow][startCol] = true;

        // Add walls of the starting cell to the list
        addWalls(maze, walls, startRow, startCol);
    }

    @Override
    public boolean generateMazeTick(int[][] maze) {
        if (walls.isEmpty()) {
            setStartAndEnd(maze);
            return false;
        }

        Collections.shuffle(walls);
        int[] wall = walls.remove(0);

        int row = wall[0];
        int col = wall[1];
        int oppositeRow = row + wall[2] * 2;
        int oppositeCol = col + wall[3] * 2;

        if (oppositeRow >= 0 && oppositeRow < maze.length && oppositeCol >= 0 && oppositeCol < maze[0].length) {
            if (!visited[oppositeRow][oppositeCol]) {
                maze[row][col] = Main.PATH;
                maze[row + wall[2]][col + wall[3]] = Main.PATH;
                maze[oppositeRow][oppositeCol] = Main.PATH;

                visited[oppositeRow][oppositeCol] = true;

                addWalls(maze, walls, oppositeRow, oppositeCol);
            }
        }

        return true; // Continue generation
    }

    private void addWalls(int[][] maze, List<int[]> walls, int row, int col) {
        if (row > 1)
            walls.add(new int[] { row - 1, col, -1, 0 }); // Upper wall
        if (col > 1)
            walls.add(new int[] { row, col - 1, 0, -1 }); // Left wall
        if (row < maze.length - 2)
            walls.add(new int[] { row + 1, col, 1, 0 }); // Lower wall
        if (col < maze[0].length - 2)
            walls.add(new int[] { row, col + 1, 0, 1 }); // Right wall
    }

    private void setStartAndEnd(int[][] maze) {
        List<int[]> deadEnds = findDeadEnds(maze);

        if (deadEnds.size() < 2) {
            // Fallback or error handling if there aren't enough dead-ends
            System.err.println("Not enough dead-ends to set start and end points.");
            return;
        }

        // Shuffle the dead-ends list to randomize selection
        Collections.shuffle(deadEnds);

        // Set the start point
        int[] start = deadEnds.remove(0);
        maze[start[0]][start[1]] = Main.START;

        // Set the end point, ensuring it is different from the start point
        int[] end = deadEnds.remove(0);
        maze[end[0]][end[1]] = Main.END;
    }

    private List<int[]> findDeadEnds(int[][] maze) {
        List<int[]> deadEnds = new ArrayList<>();
        for (int row = 1; row < maze.length - 1; row++) {
            for (int col = 1; col < maze[row].length - 1; col++) {
                if (maze[row][col] == Main.PATH && isDeadEnd(maze, row, col)) {
                    deadEnds.add(new int[] { row, col });
                }
            }
        }
        return deadEnds;
    }

    private boolean isDeadEnd(int[][] maze, int row, int col) {
        int openPaths = 0;
        if (maze[row - 1][col] == Main.PATH)
            openPaths++;
        if (maze[row + 1][col] == Main.PATH)
            openPaths++;
        if (maze[row][col - 1] == Main.PATH)
            openPaths++;
        if (maze[row][col + 1] == Main.PATH)
            openPaths++;

        return openPaths == 1;
    }
}
