package me.smarion.aoc2023;

import com.google.common.base.Objects;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import me.smarion.aoc2023.util.DayChallenge;

public class Day14p2 implements DayChallenge {
  @Override
  public String run(String input) {
    String[] lines = input.trim().split("\n");
    char[][] grid = new char[lines[0].length()][lines.length];
    for (int y = 0; y < lines.length; y++) {
      for (int x = 0; x < lines[0].length(); x++) {
        grid[x][y] = lines[y].charAt(x);
      }
    }

    Map<String, Integer> cache = new HashMap<>();

    int cycle = 1_000_000_000;
    while (cycle > 0) {
      String s = asString(grid);
      if (cache.containsKey(s)) {
        int adv = (1_000_000_000 - cache.get(s)) - cycle;
        System.out.println("cache hit : " + adv);
        cycle = cycle % adv;
      } else {
        cache.put(s, 1_000_000_000 - cycle);
      }
      moveUp(grid);
      grid = rotateClockwise(grid);
      moveUp(grid);
      grid = rotateClockwise(grid);
      moveUp(grid);
      grid = rotateClockwise(grid);
      moveUp(grid);
      grid = rotateClockwise(grid);
      cycle--;
      System.out.printf("only %d cycles left%n", cycle);
    }


    long sum = 0;
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        if (grid[x][y] == 'O') {
          sum += grid[x].length - y;
        }
      }
    }

    return "" + sum;
  }

  private String asString(char[][] grid) {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < grid[0].length; y++) {
      for (int x = 0; x < grid.length; x++) {
        sb.append(grid[x][y]);
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public void moveUp(char[][] grid) {
    for (int y = 0; y < grid[0].length; y++) {
      for (int x = 0; x < grid.length; x++) {
        if (grid[x][y] == 'O') {
          for (int i = y - 1; i >= 0 && grid[x][i] == '.'; i--) {
            grid[x][i + 1] = '.';
            grid[x][i] = 'O';
          }
        }
      }
    }
  }

  public char[][] rotateClockwise(char[][] grid) {
    char[][] rot = new char[grid[0].length][grid.length];
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[0].length; y++) {
        rot[grid.length - y - 1][x] = grid[x][y];
      }
    }
    return rot;
  }
}
