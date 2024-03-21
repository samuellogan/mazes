package main.java.solver;

public interface MazeSolver {
    /**
     * solves the maze and returns the solution path
     * 
     * @param maze   the maze to solve, represented as a 2D integer array where 0s
     *               are paths and 1s are walls
     * @param startX the starting X-coordinate (column) for the solver
     * @param startY the starting Y-coordinate (row) for the solver
     * @param endX   the ending X-coordinate (column) for the solver
     * @param endY   the ending Y-coordinate (row) for the solver
     * @return a 2D integer array representing the maze with the solution path
     *         marked, or null if no solution exists
     */
    int[][] solveMaze(int[][] maze, int startX, int startY, int endX, int endY);
}
