package main.java.generator;

public interface MazeGenerator {
    /**
     * Initializes the maze generation process.
     *
     * @param maze The maze array to be generated.
     */
    void initializeMaze(int[][] maze);

    /**
     * Performs a single step of the maze generation process. This method should be
     * called repeatedly to animate the maze generation.
     *
     * @param maze The maze array being generated.
     * @return true if the generation process should continue, false if it is
     *         complete.
     */
    boolean generateMazeTick(int[][] maze);
}
