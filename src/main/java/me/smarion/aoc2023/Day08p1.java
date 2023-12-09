package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day08p1 implements DayChallenge {
    @Override
    public String run(String input) {
        String[] parts = input.split("\n\n");
        char[] instructions = parts[0].trim().toCharArray();

        Map<String, Fork> paths = parts[1].lines()
                .map(l -> new Fork(
                        l.substring(0, 3),
                        l.substring(7, 10),
                        l.substring(12, 15))
                )
                .collect(Collectors.toMap(Fork::current, Function.identity()));

        int step = 0;
        String current = "AAA";
        while (!"ZZZ".equals(current)) {
            current = instructions[step % instructions.length] == 'R' ?
                    paths.get(current).right() : paths.get(current).left();
            step++;
        }
        return step + "";
    }

    record Fork(String current, String left, String right) {
    }

    ;
}
