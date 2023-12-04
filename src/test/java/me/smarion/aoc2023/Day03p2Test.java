package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03p2Test {
    @Test
    public void testRun() {
        String input = """
                467..114..
                ...*......
                ..35..633.
                ......#...
                617*......
                .....+.58.
                ..592.....
                ......755.
                ...$.*....
                .664.598..
                """;

        String result = new Day03p2().run(input);
        System.out.println(result);
        assertEquals("467835", result);
    }

}