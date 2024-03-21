package main.java.solver;

import java.util.PriorityQueue;
import java.util.Comparator;

public class AStarSolver implements MazeSolver {

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
    public int[][] solveMaze(int[][] maze, int startX, int startY, int endX, int endY) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic));
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        visited[startX][startY] = true;

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

                if (nextX >= 0 && nextX < maze[0].length && nextY >= 0 && nextY < maze.length && maze[nextY][nextX] == 0
                        && !visited[nextY][nextX]) {
                    int nextCost = current.cost + 1; // Assuming uniform cost
                    Node nextNode = new Node(nextX, nextY, current, nextCost,
                            estimateDistance(nextX, nextY, endX, endY));
                    openSet.add(nextNode);
                }
            }
        }

        return null; // No path found
    }

    private int estimateDistance(int x, int y, int endX, int endY) {
        // Manhattan distance as a simple heuristic
        return Math.abs(x - endX) + Math.abs(y - endY);
    }

    private int[][] reconstructPath(int[][] maze, Node endNode) {
        Node current = endNode;
        while (current.parent != null) {
            maze[current.y][current.x] = 3; // Mark the solution path
            current = current.parent;
        }
        return maze;
    }
}
