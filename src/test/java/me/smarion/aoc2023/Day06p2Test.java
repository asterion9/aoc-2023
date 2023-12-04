package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06p2Test {

    @Test
    void run() {
        String input = """
                Time:      7  15   30
                Distance:  9  40  200
                """.trim();

        String result = new Day06p2().run(input);
        System.out.println(result);
        assertEquals("71503", result);

    }
}