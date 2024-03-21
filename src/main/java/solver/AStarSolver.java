package main.java.solver;

import java.util.PriorityQueue;
import java.util.Comparator;

public class AStarSolver implements MazeSolver {

    private static final int WALL = 1;
    private static final int PATH = 0;
    private static final int START = 4; // Assuming 4 marks the start
    private static final int END = 5; // Assuming 5 marks the end

    private class Node {
        int x, y;
        Node parent;
        int cost, distance, heuristic;

        Node(int x, int y, Node parent, int cost, int distance) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.cost = cost;
            this.distance = distance;
            this.heuristic = cost + distance;
        }
    }

    @Override
    public int[][] solveMaze(int[][] maze) {
        int[] start = findPoint(maze, START);
        int[] end = findPoint(maze, END);
        if (start == null || end == null) {
            return null;
        }

        int startX = start[0];
        int startY = start[1];
        int endX = end[0];
        int endY = end[1];

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic));
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        visited[startY][startX] = true;
        openSet.add(new Node(startX, startY, null, 0, estimateDistance(startX, startY, endX, endY)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == endX && current.y == endY) {
                return reconstructPath(maze, current);
            }

            visited[current.y][current.x] = true;

            for (int[] direction : new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }) {
                int nextX = current.x + direction[0];
                int nextY = current.y + direction[1];

                // Check if the next position is within bounds, not visited, and either a path,
                // start, or end
                if (nextX >= 0 && nextX < maze[0].length && nextY >= 0 && nextY < maze.length
                        && !visited[nextY][nextX]
                        && (maze[nextY][nextX] == PATH || maze[nextY][nextX] == START || maze[nextY][nextX] == END)) {

                    int nextCost = current.cost + 1; // Uniform cost assumed
                    Node nextNode = new Node(nextX, nextY, current, nextCost,
                            estimateDistance(nextX, nextY, endX, endY));
                    openSet.add(nextNode);
                }
            }
        }

        return null; // no path was found
    }

    private int[] findPoint(int[][] maze, int pointType) {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == pointType) {
                    return new int[] { x, y };
                }
            }
        }
        return null;
    }

    private int estimateDistance(int startX, int startY, int endX, int endY) {
        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }

    private int[][] reconstructPath(int[][] maze, Node endNode) {
        Node current = endNode.parent;
        while (current != null && current.parent != null) {
            maze[current.y][current.x] = 3;
            current = current.parent;
        }
        return maze;
    }

}
