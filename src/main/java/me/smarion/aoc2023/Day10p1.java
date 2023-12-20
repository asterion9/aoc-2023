package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day10p1 implements DayChallenge {
    @Override
    public String run(String input) {
        char[][] rawGrid = input.trim().lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        char[][] grid = new char[rawGrid[0].length][rawGrid.length];
        for (int x = 0; x < rawGrid.length; x++) {
            for (int y = 0; y < rawGrid[0].length; y++) {
                grid[y][x] = rawGrid[x][y];
            }
        }

        GridRunner gridRunner = new GridRunner(grid);
        List<Direction> path = new ArrayList<>();
        while (gridRunner.hasNext()) {
            path.add(gridRunner.next());
        }

        return "" + path.size() / 2;
    }

    private static class GridRunner implements Iterator<Direction> {
        private final char[][] grid;
        private int x, y;
        private Direction comingFrom;

        public GridRunner(char[][] grid) {
            this.grid = grid;
            this.comingFrom = null;
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[0].length; y++) {
                    if (grid[x][y] == 'S') {
                        this.x = x;
                        this.y = y;
                        return;
                    }
                }
            }
        }

        @Override
        public boolean hasNext() {
            return grid[x][y] != 'S' || comingFrom == null;
        }

        @Override
        public Direction next() {
            return advanceToward(switch (grid[x][y]) {
                case 'S' -> findOpenPipeAround();
                case '7' -> Direction.LEFT == comingFrom ? Direction.DOWN : Direction.LEFT;
                case 'F' -> Direction.RIGHT == comingFrom ? Direction.DOWN : Direction.RIGHT;
                case 'L' -> Direction.RIGHT == comingFrom ? Direction.UP : Direction.RIGHT;
                case 'J' -> Direction.LEFT == comingFrom ? Direction.UP : Direction.LEFT;
                case '|' -> Direction.UP == comingFrom ? Direction.DOWN : Direction.UP;
                case '-' -> Direction.LEFT == comingFrom ? Direction.RIGHT : Direction.LEFT;

                default -> throw new IllegalStateException("Unexpected value: " + grid[x][y]);
            });
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        private Direction advanceToward(Direction direction) {
            switch (direction) {
                case UP -> this.y--;
                case RIGHT -> this.x++;
                case DOWN -> this.y++;
                case LEFT -> this.x--;
            }
            this.comingFrom = direction.opposite();
            return direction;
        }

        private Direction findOpenPipeAround() {
            if (x > 0) {
                char left = grid[x - 1][y];
                if (left == 'F' || left == 'L' || left == '-') {
                    return Direction.LEFT;
                }
            }
            if (x < grid.length - 1) {
                char right = grid[x + 1][y];
                if (right == '7' || right == 'J' || right == '-') {
                    return Direction.RIGHT;
                }
            }
            if (y > 0) {
                char up = grid[x][y - 1];
                if (up == 'F' || up == '7' || up == '|') {
                    return Direction.UP;
                }
            }
            if (y < grid[0].length - 1) {
                char down = grid[x][y + 1];
                if (down == 'L' || down == 'J' || down == '|') {
                    return Direction.DOWN;
                }
            }
            throw new IllegalStateException("no adjacent pipe from this position");
        }
    }

    private enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        public Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case RIGHT -> LEFT;
                case DOWN -> UP;
                case LEFT -> RIGHT;
            };
        }
    }
}
