package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

public class Day17 implements DayChallenge {
    private final int maxRange;
    private final int minRange;
    private Cell[][] grid;
    private int shortestPathKnown = 1500;

    public Day17() {
        this(10, 4);
    }

    public Day17(int maxRange, int minRange) {
        this.maxRange = maxRange;
        this.minRange = minRange;
    }

    @Override
    public String run(String input) {
        String[] lines = input.trim().split("\n");
        grid = new Cell[lines[0].length()][lines.length];

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                grid[x][y] = new Cell(Integer.parseInt(lines[y].substring(x, x + 1)));
            }
        }

        findShortestPath(new Position(0, 0), 0, Direction.RIGHT);
        findShortestPath(new Position(0, 0), 0, Direction.DOWN);

        return shortestPathKnown + "";
    }

    //assuming this position is correct
    private void findShortestPath(Position p, int cost, Direction from) {
        // check for goal
        if (p.isGoal(grid)) {
            if (cost < shortestPathKnown) {
                shortestPathKnown = cost;
            }
            return;
        }

        for (Direction nextDir : from.sides()) {
            int nextCost = cost;
            Position nextPos = p.copyOf();
            for (int i = 1; i <= maxRange; i++) {
                nextPos.move(nextDir);
                if (!nextPos.isInGrid(grid)) {
                    break;
                }
                Cell nextCell = grid[nextPos.x][nextPos.y];
                nextCost += nextCell.cost;
                if (nextCost >= shortestPathKnown) {
                    continue;
                }
                if (i < minRange) {
                    continue;
                }
                if (nextCell.cache.get(nextDir) > nextCost) {
                    nextCell.cache.put(nextDir, nextCost);
                    findShortestPath(nextPos, nextCost, nextDir);
                }
            }
        }
    }

    private static class Position {
        public int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        public Position copyOf() {
            return new Position(x, y);
        }

        public <T> boolean isInGrid(T[][] grid) {
            return x >= 0 && x < grid.length && y >= 0 && y < grid[x].length;
        }

        public <T> boolean isGoal(T[][] grid) {
            return x == grid.length - 1 && y == grid[x].length - 1;
        }

        public void move(Direction direction) {
            switch (direction) {
                case RIGHT -> x++;
                case DOWN -> y++;
                case LEFT -> x--;
                case UP -> y--;
            }
        }
    }

    private enum Direction {
        RIGHT, DOWN, LEFT, UP;

        public Set<Direction> sides() {
            return switch (this) {
                case RIGHT, LEFT -> EnumSet.of(DOWN, UP);
                case DOWN, UP -> EnumSet.of(RIGHT, LEFT);
            };
        }
    }

    private static class Cell {
        public final int cost;
        final EnumMap<Direction, Integer> cache;

        public Cell(int cost) {
            this.cost = cost;
            this.cache = new EnumMap<>(Direction.class);
            Stream.of(Direction.values())
                    .forEach(d -> cache.put(d, Integer.MAX_VALUE));
        }
    }
}
