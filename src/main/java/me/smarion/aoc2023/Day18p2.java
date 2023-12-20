package me.smarion.aoc2023;

import java.util.Arrays;
import java.util.List;
import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.StreamEx;

public class Day18p2 implements DayChallenge {
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


    StreamEx.of(positions).append(positions.get(0))
      .forPairs((position, position2) -> {

        //TODO
      });

    return null;
  }


  private record Position(long x, long y) {
  }

  private enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction parsePlan(char c) {
      return switch (c) {
        case '3' -> UP;
        case '1' -> DOWN;
        case '0' -> RIGHT;
        case '2' -> LEFT;
        default -> throw new IllegalArgumentException(c + " is not a valid direction");
      };
    }
  }

  private record PlanLine(Direction direction, long length) {
    public static PlanLine parse(String input) {
      String instruction = input.split("\\s+")[2].substring(2, 8);
      return new PlanLine(
        Direction.parsePlan(instruction.charAt(5)),
        Long.parseLong(instruction.substring(0, 5), 16)
      );
    }
  }
}
