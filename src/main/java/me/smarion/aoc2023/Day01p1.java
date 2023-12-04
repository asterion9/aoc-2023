package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day01p1 implements DayChallenge {
    public static Pattern numberPattern = Pattern.compile("\\d");

    @Override
    public String run(String input) {
        return input.trim().lines()
                .map(numberPattern::matcher)
                .map(m -> {
                    List<String> l = Stream.generate(() -> m.find() ? m.group() : null)
                            .takeWhile(Objects::nonNull)
                            .toList();
                    if (l.isEmpty()) {
                        throw new IllegalArgumentException("empty");
                    }
                    return l;
                })
                .map(l -> new Pair(l.get(0),
                        l.get(l.size() - 1)))
                .mapToLong(Pair::value)
                .sum() + "";
    }

    record Pair(String first, String last) {

        public long value() {
            return Long.parseLong(first + last);
        }
    }
}
