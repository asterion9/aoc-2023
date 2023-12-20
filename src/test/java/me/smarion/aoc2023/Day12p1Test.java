package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12p1Test {

  @Test
  void run() {
    Day12p1 underTest = new Day12p1();
    assertEquals("2", underTest.run("??#?? 1,1"));

    assertEquals("8", underTest.run("????.??#?.?.????# 3,4,1,1"));
    assertEquals("15", underTest.run("???.??????? 1,4"));
    assertEquals("1", underTest.run("???.### 1,1,3"));
    assertEquals("4", underTest.run(".??..??...?##. 1,1,3"));
    assertEquals("1", underTest.run("?#?#?#?#?#?#?#? 1,3,1,6"));
    assertEquals("1", underTest.run("????.#...#... 4,1,1"));
    assertEquals("4", underTest.run("????.######..#####. 1,6,5"));
    assertEquals("10", underTest.run("?###???????? 3,2,1"));
  }
}
