package me.smarion.aoc2023;

import java.util.stream.Stream;
import me.smarion.aoc2023.util.DayChallenge;

public class Day13p1 implements DayChallenge {
  @Override
  public String run(String input) {
    String[] patterns = input.trim().split("\n\n");
    return "" + Stream.of(patterns)
      .map(Day13p1::parse)
      .mapToInt(a -> findFold(a) + 100 * findFold(transpose(a)))
      .sum();
  }

  private static char[][] transpose(char[][] array) {
    char[][] result = new char[array[0].length][array.length];
    for (int x = 0; x < array.length; x++) {
      for (int y = 0; y < array[x].length; y++) {
        result[y][x] = array[x][y];
      }
    }
    return result;
  }

  private static char[][] parse(String pattern) {
    String[] lines = pattern.split("\n");
    char[][] patternArray = new char[lines[0].length()][lines.length];
    for (int x = 0; x < lines[0].length(); x++) {
      for (int y = 0; y < lines.length; y++) {
        patternArray[x][y] = lines[y].charAt(x);
      }
    }
    return patternArray;
  }

  private static int findFold(char[][] pattern) {
    for (int x = 1; x < pattern.length; x++) {
      if (isFoldingIn(pattern, x)) {
        return x;
      }
    }
    return 0;
  }

  private static boolean isFoldingIn(char[][] pattern, int x) {
    for (int offset = 0; x - offset - 1 >= 0 && x + offset < pattern.length; offset++) {
      for (int y = 0; y < pattern[x].length; y++) {
        if (pattern[x - offset - 1][y] != pattern[x + offset][y]) {
          return false;
        }
      }
    }
    return true;
  }
}
