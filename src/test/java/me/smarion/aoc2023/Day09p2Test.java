package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09p2Test {

    @Test
    void run() {
        String input = """
                0 3 6 9 12 15
                1 3 6 10 15 21
                10 13 16 21 30 45
                """.trim();

        String result = new Day09p2().run(input);
        System.out.println(result);
        assertEquals("2", result);

    }
}