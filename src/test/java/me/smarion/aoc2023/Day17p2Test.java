package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day17p2Test {

  @Test
  public void testp1() {
    String input = """
      2413432311323
      3215453535623
      3255245654254
      3446585845452
      4546657867536
      1438598798454
      4457876987766
      3637877979653
      4654967986887
      4564679986453
      1224686865563
      2546548887735
      4322674655533
      """.trim();

    String result = new Day17p2(3, 1).run(input);
    System.out.println(result);

    assertEquals("102", result);
  }

  @Test
  public void testP2() {
    String input = """
      2413432311323
      3215453535623
      3255245654254
      3446585845452
      4546657867536
      1438598798454
      4457876987766
      3637877979653
      4654967986887
      4564679986453
      1224686865563
      2546548887735
      4322674655533
      """.trim();

    String result = new Day17p2(10, 4).run(input);
    System.out.println(result);

    assertEquals("94", result);
  }

  @Test
  public void testP2Trivial() {
    String input = """
      111111111111
      999999999991
      999999999991
      999999999991
      999999999991
      """.trim();

    String result = new Day17p2(10, 4).run(input);
    System.out.println(result);

    assertEquals("71", result);
  }
}
