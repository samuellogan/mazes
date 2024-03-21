package main.java.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeUtils {

    private static final int WALL = 1;
    private static final int PATH = 0;
    private static final int START = 4;
    private static final int END = 5;

    /**
     * Sets the start and end points of the maze by finding dead-ends.
     * 
     * @param maze The maze array
     */
    public static void setStartAndEndPoints(int[][] maze) {
        List<int[]> deadEnds = findDeadEnds(maze);

        if (deadEnds.size() < 2) {
            System.err.println("Not enough dead-ends to set start and end points.");
            return;
        }

        Collections.shuffle(deadEnds);

        int[] start = deadEnds.remove(0);
        maze[start[0]][start[1]] = START;

        int[] end = deadEnds.remove(0);
        maze[end[0]][end[1]] = END;
    }

    /**
     * Finds dead-ends in the maze.
     * 
     * @param maze The maze array
     * @return A list of dead-end coordinates
     */
    private static List<int[]> findDeadEnds(int[][] maze) {
        List<int[]> deadEnds = new ArrayList<>();
        for (int row = 1; row < maze.length - 1; row++) {
            for (int col = 1; col < maze[row].length - 1; col++) {
                if (maze[row][col] == PATH && isDeadEnd(maze, row, col)) {
                    deadEnds.add(new int[] { row, col });
                }
            }
        }
        return deadEnds;
    }

    /**
     * Checks if a cell is a dead-end.
     * 
     * @param maze The maze array
     * @param row  The row of the cell
     * @param col  The column of the cell
     * @return True if the cell is a dead-end, else false
     */
    private static boolean isDeadEnd(int[][] maze, int row, int col) {
        int openPaths = 0;
        if (maze[row - 1][col] == PATH)
            openPaths++;
        if (maze[row + 1][col] == PATH)
            openPaths++;
        if (maze[row][col - 1] == PATH)
            openPaths++;
        if (maze[row][col + 1] == PATH)
            openPaths++;

        return openPaths == 1;
    }
}
