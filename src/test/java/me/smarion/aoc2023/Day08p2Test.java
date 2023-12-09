package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08p2Test {

    @Test
    void run() {
        String input = """
                LR
                                
                11A = (11B, XXX)
                11B = (XXX, 11Z)
                11Z = (11B, XXX)
                22A = (22B, XXX)
                22B = (22C, 22C)
                22C = (22Z, 22Z)
                22Z = (22B, 22B)
                XXX = (XXX, XXX)
                """.trim();

        String result = new Day08p2().run(input);
        System.out.println(result);
        assertEquals("6", result);

    }
}