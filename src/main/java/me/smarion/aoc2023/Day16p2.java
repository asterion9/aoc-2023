package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.IntStream;


public class Day16p2 implements DayChallenge {
    @Override
    public String run(String input) {
        String[] lines = input.trim().split("\n");
        Cell[][] grid = new Cell[lines[0].length()][lines.length];
        for (int x = 0; x < grid[0].length; x++) {
            for (int y = 0; y < grid.length; y++) {
                grid[x][y] = new Cell(lines[y].charAt(x));
            }
        }
        return "" + IntStream.concat(
                IntStream.range(0, grid.length)
                        .map(x -> Math.max(countEnergyFrom(x, -1, Direction.DOWN, grid), countEnergyFrom(x, grid[x].length, Direction.UP, grid))),
                IntStream.range(0, grid[0].length)
                        .map(y -> Math.max(countEnergyFrom(-1, y, Direction.RIGHT, grid), countEnergyFrom(grid.length, y, Direction.LEFT, grid)))
        ).max().getAsInt();
    }

    private static int countEnergyFrom(int xStart, int yStart, Direction from, Cell[][] grid) {
        advanceFrom(grid, xStart, yStart, from);
        int count = 0;
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (!grid[x][y].visitedFrom.isEmpty()) {
                    grid[x][y].visitedFrom.clear();
                    count++;
                }
            }
        }
        return count;
    }

    private static void advanceFrom(Cell[][] grid, int x, int y, Direction direction) {
        switch (direction) {
            case UP -> y--;
            case RIGHT -> x++;
            case DOWN -> y++;
            case LEFT -> x--;
        }
        if (x < 0 || x >= grid.length) {
            return;
        }
        if (y < 0 || y >= grid[x].length) {
            return;
        }
        Cell cell = grid[x][y];
        if (cell.isVisited(direction)) {
            return;
        } else {
            cell.setVisitedFrom(direction);
        }
        switch (cell.value()) {
            case '.' -> advanceFrom(grid, x, y, direction);
            case '-' -> {
                if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    advanceFrom(grid, x, y, direction);
                } else {
                    advanceFrom(grid, x, y, Direction.LEFT);
                    advanceFrom(grid, x, y, Direction.RIGHT);
                }
            }
            case '|' -> {
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    advanceFrom(grid, x, y, direction);
                } else {
                    advanceFrom(grid, x, y, Direction.UP);
                    advanceFrom(grid, x, y, Direction.DOWN);
                }
            }
            case '/' -> advanceFrom(grid, x, y, Direction.bounceSlash(direction));
            case '\\' -> advanceFrom(grid, x, y, Direction.bounceAntiSlash(direction));
            default -> throw new IllegalStateException(String.format("cell %c is unknown", cell.value()));
        }
    }

    private enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        public static Direction bounceSlash(Direction direction) {
            return switch (direction) {
                case UP -> RIGHT;
                case RIGHT -> UP;
                case DOWN -> LEFT;
                case LEFT -> DOWN;
            };
        }

        public static Direction bounceAntiSlash(Direction direction) {
            return switch (direction) {
                case UP -> LEFT;
                case RIGHT -> DOWN;
                case DOWN -> RIGHT;
                case LEFT -> UP;
            };
        }
    }

    private static class Cell {
        private final char value;
        private final Set<Direction> visitedFrom;

        public Cell(char value) {
            this.value = value;
            this.visitedFrom = EnumSet.noneOf(Direction.class);
        }

        public char value() {
            return value;
        }

        public boolean isVisited(Direction direction) {
            return visitedFrom.contains(direction);
        }

        public void setVisitedFrom(Direction direction) {
            visitedFrom.add(direction);
        }

        public char print() {
            if (!visitedFrom.isEmpty() && value == '.') {
                return '¤';
            } else {
                return value;
            }
        }

    }
}
