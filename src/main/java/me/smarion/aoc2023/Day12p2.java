package me.smarion.aoc2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import me.smarion.aoc2023.util.DayChallenge;

public class Day12p2 implements DayChallenge {
  @Override
  public String run(String input) {
    List<Springs> springsList = input.trim().lines()
      .map(Springs::parse)
      .toList();

    AtomicInteger i = new AtomicInteger(0);
    return "" + springsList.stream()
      .peek(s -> System.out.printf("%s => ", s))
      .mapToLong(s -> Springs.computeArrangement(0, s.springs(), 0, s.groups(), new HashMap<>()))
      .peek(nb -> System.out.printf("found %d%n", nb))
      .sum();
  }

  private record Springs(char[] springs, int[] groups) {
    public static Springs parse(String line) {
      String[] parts = line.split("\\s+");
      return new Springs(
        (parts[0] + "?" + parts[0] + "?" + parts[0] + "?" + parts[0] + "?" + parts[0]).toCharArray(),
        Stream.of(
            (parts[1] + "," + parts[1] + "," + parts[1] + "," + parts[1] + "," + parts[1])
              .split(","))
          .mapToInt(Integer::parseInt)
          .toArray());
    }

    private static long computeArrangement(int springIndex, char[] springs, int groupIndex, int[] groups, Map<Integer, Map<Integer, Long>> cache) {
      if(cache.containsKey(springIndex) && cache.get(springIndex).containsKey(groupIndex)) {
        return cache.get(springIndex).get(groupIndex);
      }
      if (groupIndex >= groups.length) {
        for (int i = springIndex; i < springs.length; i++) {
          if (springs[i] == '#') {
            return 0;
          }
        }
        return 1;
      }
      int groupSize = groups[groupIndex];
      long nbArrangement = 0;
      for (int i = springIndex; i < springs.length; i++) {
        if (canFit(i, springs, groupSize)) {
          nbArrangement += computeArrangement(i + groupSize + 1, springs, groupIndex + 1, groups, cache);
        }
        if (springs[i] == '#') {
          break;
        }
      }
      cache.computeIfAbsent(springIndex, si -> new HashMap<>()).put(groupIndex, nbArrangement);
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

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("Springs{");
      sb.append("springs=").append(Arrays.toString(springs));
      sb.append(", groups=").append(Arrays.toString(groups));
      sb.append('}');
      return sb.toString();
    }
  }
}
