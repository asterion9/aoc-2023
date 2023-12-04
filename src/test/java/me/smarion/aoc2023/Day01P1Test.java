package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01P1Test {
    @Test
    public void testPart1() {
        String result = new Day01p1().run("""
                1abc2
                pqr3stu8vwx
                a1b2c3d4e5f
                treb7uchet
                """);

        assertEquals("142", result);
    }
}