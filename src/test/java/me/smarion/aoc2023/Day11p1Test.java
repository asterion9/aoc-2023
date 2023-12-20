package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11p1Test {

  @Test
  void run() {
    String input = """
      ...#......
      .......#..
      #.........
      ..........
      ......#...
      .#........
      .........#
      ..........
      .......#..
      #...#.....
      """.trim();

    String result = new Day11p1().run(input);
    System.out.println(result);
    assertEquals("374", result);

  }
}
