package main.java.solver;

public interface MazeSolver {
    /**
     * Solves the maze and returns the solution path.
     * 
     * @param maze   The maze to solve, represented as a 2D integer array where 0s
     *               are paths and 1s are walls.
     * @param startX The starting X-coordinate (column) for the solver.
     * @param startY The starting Y-coordinate (row) for the solver.
     * @param endX   The ending X-coordinate (column) for the solver.
     * @param endY   The ending Y-coordinate (row) for the solver.
     * @return A 2D integer array representing the maze with the solution path
     *         marked, or null if no solution exists.
     */
    int[][] solveMaze(int[][] maze, int startX, int startY, int endX, int endY);
}
