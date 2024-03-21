package main.java.generator;

public interface MazeGenerator {
    /**
     * Generates a maze with the specified dimensions.
     * 
     * @param width  the width of the maze
     * @param height the height of the maze
     * @return a 2D integer array representing the maze, where 0s represent paths,
     *         and 1s represent walls.
     */
    int[][] generateMaze(int width, int height);
}
