package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14p2Test {

  @Test
  void run() {
    String input = """
      O....#....
      O.OO#....#
      .....##...
      OO.#O....O
      .O.....O#.
      O.#..O.#.#
      ..O..#O..O
      .......O..
      #....###..
      #OO..#....
      """.trim();

    String result = new Day14p2().run(input);
    System.out.println(result);
    assertEquals("64", result);

  }
}
