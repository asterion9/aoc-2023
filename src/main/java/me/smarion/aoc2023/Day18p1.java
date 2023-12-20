package me.smarion.aoc2023;

import java.util.Arrays;
import java.util.List;
import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.StreamEx;

public class Day18p1 implements DayChallenge {
  @Override
  public String run(String input) {
    List<PlanLine> instructions = input.trim().lines()
      .map(PlanLine::parse)
      .toList();

    List<Position> positions = StreamEx.of(instructions)
      .scanLeft(new Position(0, 0), ((p, planLine) ->
        switch (planLine.direction) {
          case UP -> new Position(p.x, p.y - planLine.length);
          case RIGHT -> new Position(p.x + planLine.length, p.y);
          case DOWN -> new Position(p.x, p.y + planLine.length);
          case LEFT -> new Position(p.x - planLine.length, p.y);
        }));

    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (Position p : positions) {
      if (p.x < minX) {
        minX = p.x;
      }
      if (p.y < minY) {
        minY = p.y;
      }
      if (p.x > maxX) {
        maxX = p.x;
      }
      if (p.y > maxY) {
        maxY = p.y;
      }
    }
    int xOffset = -minX;
    int yOffset = -minY;
    maxX = maxX + xOffset;
    maxY = maxY + yOffset;

    char[][] grid = new char[maxX + 1][maxY + 1];
    for (char[] chars : grid) {
      Arrays.fill(chars, '.');
    }

    StreamEx.of(positions).append(positions.get(0))
      .map(p -> new Position(p.x + xOffset, p.y + yOffset))
      .forPairs((position, position2) -> {
        for (int x = Math.min(position.x, position2.x); x <= Math.max(position.x, position2.x); x++) {
          for (int y = Math.min(position.y, position2.y); y <= Math.max(position.y, position2.y); y++) {
            grid[x][y] = '#';
          }
        }
      });


    Filler filler = new Filler(grid, '.', 'x');
    for (int x = 0; x < grid.length; x++) {
      filler.fillAdjacent(x, 0);
      filler.fillAdjacent(x, grid[x].length - 1);
    }
    for (int y = 0; y < grid[0].length; y++) {
      filler.fillAdjacent(0, y);
      filler.fillAdjacent(grid.length - 1, y);
    }

    int counter = 0;
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        if (grid[x][y] == '.' || grid[x][y] == '#') {
          counter++;
        }
      }
    }

    System.out.println(print(grid));

    return counter + "";
  }

  private static String print(char[][] grid) {
    StringBuilder sb = new StringBuilder();
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        sb.append(grid[x][y]);
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  private static class Filler {
    private final char[][] grid;
    private final char replace;
    private final char by;

    public Filler(char[][] grid, char replace, char by) {
      this.grid = grid;
      this.replace = replace;
      this.by = by;
    }

    public void fillAdjacent(int x, int y) {
      if (x >= 0 && x < grid.length) {
        if (y >= 0 && y < grid[x].length) {
          if (grid[x][y] == replace) {
            grid[x][y] = by;
            fillAdjacent(x - 1, y);
            fillAdjacent(x + 1, y);
            fillAdjacent(x, y - 1);
            fillAdjacent(x, y + 1);
          }
        }
      }
    }
  }


  private record Position(int x, int y) {
  }

  private enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction parsePlan(char c) {
      return switch (c) {
        case 'U' -> UP;
        case 'D' -> DOWN;
        case 'R' -> RIGHT;
        case 'L' -> LEFT;
        default -> throw new IllegalArgumentException(c + " is not a valid direction");
      };
    }
  }

  private record PlanLine(Direction direction, int length) {
    public static PlanLine parse(String input) {
      String[] parts = input.split("\\s+");
      return new PlanLine(
        Direction.parsePlan(parts[0].charAt(0)),
        Integer.parseInt(parts[1])
      );
    }
  }
}
