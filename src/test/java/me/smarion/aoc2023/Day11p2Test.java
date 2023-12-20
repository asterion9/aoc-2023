package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11p2Test {

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

    String result = new Day11p2(10).run(input);
    System.out.println(result);
    assertEquals("1030", result);

  }
}
