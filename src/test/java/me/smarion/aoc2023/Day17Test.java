package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {

    @Test
    void runNoob() {
        String input = """
                111111111111
                999999999991
                999999999991
                999999999991
                999999999991
                """.trim();

        String result = new Day17().run(input);
        System.out.println(result);
        assertEquals("71", result);

    }

    @Test
    void run() {
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

        String result = new Day17().run(input);
        System.out.println(result);
        assertEquals("94", result);
    }
}