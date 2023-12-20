package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

public class Day14p1 implements DayChallenge {
  @Override
  public String run(String input) {
    String[] lines = input.trim().split("\n");
    char[][] grid = new char[lines[0].length()][lines.length];
    for (int y = 0; y < lines.length; y++) {
      for (int x = 0; x < lines[0].length(); x++) {
        grid[x][y] = lines[y].charAt(x);
      }
    }

    moveUp(grid);

    long sum = 0;
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        if(grid[x][y] == 'O') {
          sum += grid[x].length - y;
        }
      }
    }

    return "" + sum;
  }

  public void moveUp(char[][] grid) {
    for (int y = 0; y < grid[0].length; y++) {
      for (int x = 0; x < grid.length; x++) {
        if(grid[x][y] == 'O') {
          for (int i = y-1; i >= 0 && grid[x][i] == '.'; i--) {
            grid[x][i+1] = '.';
            grid[x][i] = 'O';
          }
        }
      }
    }
  }

  public void moveDown(char[][] grid) {
    for (int y = grid[0].length-1; y >= 0 ; y--) {
      for (int x = 0; x < grid.length; x++) {
        if(grid[x][y] == 'O') {
          for (int i = y+1; i < grid[0].length && grid[x][i] == '.'; i++) {
            grid[x][i-1] = '.';
            grid[x][i] = 'O';
          }
        }
      }
    }
  }
}
