package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.Arrays;
import java.util.Iterator;

public class Day10p2 implements DayChallenge {
    @Override
    public String run(String input) {
        //first read
        char[][] rawGrid = input.trim().lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        //flip array
        char[][] grid = new char[rawGrid[0].length][rawGrid.length];
        for (int x = 0; x < rawGrid.length; x++) {
            for (int y = 0; y < rawGrid[0].length; y++) {
                grid[y][x] = rawGrid[x][y];
            }
        }

        //clean up grid from unused pipes
        GridRunner gridRunner = new GridRunner(grid);
        char[][] cleanGrid = new char[grid.length][grid[0].length];
        for (char[] line : cleanGrid) {
            Arrays.fill(line, '.');
        }
        cleanGrid[gridRunner.x()][gridRunner.y()] = 'S';
        while (gridRunner.hasNext()) {
            Direction direction = gridRunner.next();
            cleanGrid[gridRunner.x()][gridRunner.y()] = grid[gridRunner.x()][gridRunner.y()];
        }

        print(cleanGrid);

        gridRunner = new GridRunner(cleanGrid);
        while (gridRunner.hasNext()) {
            Direction direction = gridRunner.next();
            switch (cleanGrid[gridRunner.x()][gridRunner.y()]) {
                case '-' -> {
                    if (direction == Direction.LEFT) {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.DOWN);
                    } else {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.UP);
                    }
                }
                case '|' -> {
                    if (direction == Direction.UP) {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.LEFT);
                    } else {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.RIGHT);
                    }
                }
                case 'J' -> {
                    if (direction == Direction.DOWN) {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.RIGHT);
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.DOWN);
                    }
                }
                case 'F' -> {
                    if (direction == Direction.UP) {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.LEFT);
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.UP);
                    }
                }
                case '7' -> {
                    if (direction == Direction.RIGHT) {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.UP);
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.RIGHT);
                    }
                }
                case 'L' -> {
                    if (direction == Direction.LEFT) {
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.DOWN);
                        fillDirection(cleanGrid, gridRunner.x(), gridRunner.y(), Direction.LEFT);
                    }
                }
            }
        }

        int countX = 0;
        int countPoint = 0;
        boolean touchBorder = false;
        for (int x = 0; x < cleanGrid.length; x++) {
            for (int y = 0; y < cleanGrid[0].length; y++) {
                if (cleanGrid[x][y] == 'X') {
                    touchBorder = touchBorder || x == 0 || y == 0 || x == cleanGrid.length - 1 || y == cleanGrid[x].length - 1;
                    countX++;
                } else if (cleanGrid[x][y] == '.') {
                    countPoint++;
                }
            }
        }

        print(cleanGrid);

        return "" + (touchBorder ? countPoint : countX);
    }

    private static void fillDirection(char[][] array, int x, int y, Direction direction) {
        switch (direction) {
            case UP -> fillArea('X', array, x, y - 1);
            case RIGHT -> fillArea('X', array, x + 1, y);
            case DOWN -> fillArea('X', array, x, y + 1);
            case LEFT -> fillArea('X', array, x - 1, y);
        }
    }

    private static void fillArea(char value, char[][] array, int x, int y) {
        if (x >= 0 && x < array.length) {
            if (y >= 0 && y < array[x].length) {
                if (array[x][y] == '.') {
                    array[x][y] = value;
                    fillArea(value, array, x - 1, y);
                    fillArea(value, array, x + 1, y);
                    fillArea(value, array, x, y - 1);
                    fillArea(value, array, x, y + 1);
                }
            }
        }
    }

    private static void print(char[][] grid) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                System.out.print(grid[x][y]);
            }
            System.out.print("\n");
        }
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
