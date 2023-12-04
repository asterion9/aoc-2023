package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07p2Test {

    @Test
    void run() {
        String input = """
                32T3K 765
                T55J5 684
                KK677 28
                KTJJT 220
                QQQJA 483
                """.trim();

        String result = new Day07p2().run(input);
        System.out.println(result);
        assertEquals("5905", result);

    }
}