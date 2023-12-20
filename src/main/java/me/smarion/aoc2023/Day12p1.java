package me.smarion.aoc2023;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;
import me.smarion.aoc2023.util.DayChallenge;

public class Day12p1 implements DayChallenge {
  @Override
  public String run(String input) {
    List<Springs> springsList = input.trim().lines()
      .map(Springs::parse)
      .toList();

    return "" + springsList.stream()
      .mapToLong(s -> Springs.computeArrangement(0, s.springs(), 0, s.groups()))
      .sum();
  }

  private record Springs(char[] springs, int[] groups) {
    public static Springs parse(String line) {
      String[] parts = line.split("\\s+");
      return new Springs(
        parts[0].toCharArray(),
        Stream.of(parts[1].split(","))
          .mapToInt(Integer::parseInt)
          .toArray());
    }

    private static long computeArrangement(int springIndex, char[] springs, int groupIndex, int[] groups) {
      if (groupIndex >= groups.length) {
        for (int i = springIndex; i < springs.length; i++) {
          if(springs[i] == '#') {
            return 0;
          }
        }
        return 1;
      }
      int groupSize = groups[groupIndex];
      long nbArrangement = 0;
      for (int i = springIndex; i < springs.length; i++) {
        if (canFit(i, springs, groupSize)) {
          nbArrangement += computeArrangement(i + groupSize + 1, springs, groupIndex + 1, groups);
        }
        if(springs[i] == '#') {
          break;
        }
      }
      return nbArrangement;
    }

    private static boolean canFit(int springIndex, char[] springs, int groupSize) {
      if (springIndex > 0 && springs[springIndex - 1] == '#') {
        return false;
      }
      for (int i = 0; i < groupSize; i++) {
        if (springIndex + i >= springs.length) {
          return false;
        }
        if (springs[springIndex + i] == '.') {
          return false;
        }
      }
      if (springIndex + groupSize < springs.length && springs[springIndex + groupSize] == '#') {
        return false;
      }
      return true;
    }
  }
}
