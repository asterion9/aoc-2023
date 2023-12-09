package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08p1Test {

    @Test
    void run() {
        String input = """
                LLR
                                
                AAA = (BBB, BBB)
                BBB = (AAA, ZZZ)
                ZZZ = (ZZZ, ZZZ)
                """.trim();

        String result = new Day08p1().run(input);
        System.out.println(result);
        assertEquals("6", result);

    }
}