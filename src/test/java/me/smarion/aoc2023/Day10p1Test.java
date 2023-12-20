package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10p1Test {

    @Test
    void run() {
        String input = """
                7-F7-
                .FJ|7
                SJLL7
                |F--J
                LJ.LJ
                """.trim();

        String result = new Day10p1().run(input);
        System.out.println(result);
        assertEquals("8", result);

    }
}