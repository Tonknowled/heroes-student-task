package com.heroes_task.programs;

import com.battle.heroes.army.programs.UnitTargetPathFinder;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0},  // Вверх, вправо, вниз, влево
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Диагонали
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        System.out.println("[UnitTargetPathFinder] A* path finding started");

        List<Edge> path = new ArrayList<>();

        if (attackUnit == null || targetUnit == null) {
            return path;
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        System.out.println("Finding path from (" + startX + "," + startY + ") to (" + targetX + "," + targetY + ")");

        // Создаем карту препятствий
        boolean[][] obstacles = new boolean[WIDTH][HEIGHT];
        if (existingUnitList != null) {
            for (Unit unit : existingUnitList) {
                if (unit != null && unit != attackUnit && unit != targetUnit && unit.isAlive()) {
                    int x = unit.getxCoordinate();
                    int y = unit.getyCoordinate();
                    if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                        obstacles[x][y] = true;
                    }
                }
            }
        }

        // A* алгоритм
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<String, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startX, startY, null, 0, heuristic(startX, startY, targetX, targetY));
        openSet.add(startNode);
        allNodes.put(key(startX, startY), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // Цель достигнута
            if (current.x == targetX && current.y == targetY) {
                System.out.println("Path found! Length: " + current.g + " steps");
                return reconstructPath(current);
            }

            // Исследуем соседей
            for (int[] dir : DIRECTIONS) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (isValid(newX, newY) && !obstacles[newX][newY]) {
                    int newG = current.g + 1;
                    String key = key(newX, newY);

                    if (!allNodes.containsKey(key) || newG < allNodes.get(key).g) {
                        Node neighbor = new Node(newX, newY, current, newG,
                                heuristic(newX, newY, targetX, targetY));
                        allNodes.put(key, neighbor);
                        openSet.add(neighbor);
                    }
                }
            }
        }

        System.out.println("No path found");
        return path;
    }

    private List<Edge> reconstructPath(Node endNode) {
        List<Edge> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(0, new Edge(current.x, current.y));
            current = current.parent;
        }

        return path;
    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        // Манхэттенское расстояние
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private String key(int x, int y) {
        return x + "," + y;
    }

    private static class Node {
        int x, y;
        Node parent;
        int g, h, f;

        Node(int x, int y, Node parent, int g, int h) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }
}