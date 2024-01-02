package me.smarion.aoc2023;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day18p2Test {

    @Test
    void testMerge() {
        List<Day18p2.Interval> baseIntervals = List.of(new Day18p2.Interval(-1, 1), new Day18p2.Interval(4, 6));

        assertResult(
                new Day18p2.Interval(-4, -3),
                baseIntervals,
                List.of(new Day18p2.Interval(-4, -3), new Day18p2.Interval(-1, 1), new Day18p2.Interval(4, 6)));

        assertResult(
                new Day18p2.Interval(-3, -1),
                baseIntervals,
                List.of(new Day18p2.Interval(-3, 1), new Day18p2.Interval(4, 6)));

        assertResult(
                new Day18p2.Interval(-3, -2),
                baseIntervals,
                List.of(new Day18p2.Interval(-3, 1), new Day18p2.Interval(4, 6)));

        assertResult(
                new Day18p2.Interval(1, 2),
                baseIntervals,
                List.of(new Day18p2.Interval(-1, 2), new Day18p2.Interval(4, 6)));

        assertResult(
                new Day18p2.Interval(1, 4),
                baseIntervals,
                List.of(new Day18p2.Interval(-1, 6)));

        assertResult(
                new Day18p2.Interval(3, 4),
                baseIntervals,
                List.of(new Day18p2.Interval(-1, 1), new Day18p2.Interval(3, 6)));

        assertResult(
                new Day18p2.Interval(6, 8),
                baseIntervals,
                List.of(new Day18p2.Interval(-1, 1), new Day18p2.Interval(4, 8)));

        assertResult(
                new Day18p2.Interval(8, 10),
                baseIntervals,
                List.of(new Day18p2.Interval(-1, 1), new Day18p2.Interval(4, 6), new Day18p2.Interval(8, 10)));


        assertResult(
                new Day18p2.Interval(-1, 0),
                baseIntervals,
                List.of(new Day18p2.Interval(0, 1), new Day18p2.Interval(4, 6)));

        assertResult(
                new Day18p2.Interval(-1, 1),
                baseIntervals,
                List.of(new Day18p2.Interval(4, 6)));

        assertResult(
                new Day18p2.Interval(0, 1),
                baseIntervals,
                List.of(new Day18p2.Interval(-1, 0), new Day18p2.Interval(4, 6)));
    }

    @Test
    void testBug() {
        List<Day18p2.Interval> baseIntervals = List.of(
                new Day18p2.Interval(-179, -147),
                new Day18p2.Interval(-126, 82),
                new Day18p2.Interval(90, 138));

        List<Day18p2.Interval> intermediate = List.of(
                new Day18p2.Interval(-185, -147),
                new Day18p2.Interval(-126, 82),
                new Day18p2.Interval(90, 138));

        assertResult(
                new Day18p2.Interval(-185, -179),
                baseIntervals,
                intermediate);

        baseIntervals = intermediate;
        intermediate = List.of(new Day18p2.Interval(-185, -138),
                new Day18p2.Interval(-126, 82),
                new Day18p2.Interval(90, 138));

        assertResult(
                new Day18p2.Interval(-147, -138),
                baseIntervals,
                intermediate);

        baseIntervals = intermediate;
        intermediate = List.of(new Day18p2.Interval(-185, -138),
                new Day18p2.Interval(-126, 77),
                new Day18p2.Interval(90, 138));

        assertResult(
                new Day18p2.Interval(77, 82),
                baseIntervals,
                intermediate);
    }

    private static void assertResult(Day18p2.Interval toMerge, List<Day18p2.Interval> with, List<Day18p2.Interval> expected) {
        ArrayList<Day18p2.Interval> currentIntervals = new ArrayList<>(with);
        Day18p2.mergeInterval(toMerge, currentIntervals);
        assertArrayEquals(expected.toArray(), currentIntervals.toArray());
    }

    @Test
    void run() {
        String input = """
                R 6 (#70c710)
                D 5 (#0dc571)
                L 2 (#5713f0)
                D 2 (#d2c081)
                R 2 (#59c680)
                D 2 (#411b91)
                L 5 (#8ceee2)
                U 2 (#caa173)
                L 1 (#1b58a2)
                U 2 (#caa171)
                R 2 (#7807d2)
                U 3 (#a77fa3)
                L 2 (#015232)
                U 2 (#7a21e3)
                """.trim();

        String result = new Day18p2().run(input);
        System.out.println(result);
        assertEquals("62", result);
    }
}
