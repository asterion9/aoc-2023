package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

public class Day15p1 implements DayChallenge {
  @Override
  public String run(String input) {
    return "" + StreamEx.of(input.trim().lines())
      .flatMap(line -> StreamEx.of(line.split(",")))
      .map(String::toCharArray)
      .mapToInt(step ->
        IntStreamEx.of(step)
          .reduce(0, (left, right) -> ((left + right) * 17)%256)
      ).sum();
  }
}
