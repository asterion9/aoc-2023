package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15p2Test {

    @Test
    void run() {
        String input = """
                rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
                """.trim();

        String result = new Day15p2().run(input);
        System.out.println(result);
        assertEquals("145", result);

    }
}