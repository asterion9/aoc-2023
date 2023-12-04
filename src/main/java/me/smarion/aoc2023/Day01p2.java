package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day01p2 implements DayChallenge {
    public static Pattern numberPattern = Pattern.compile("(?=(\\d|one|two|three|four|five|six|seven|eight|nine))");

    @Override
    public String run(String input) {
        return input.trim().lines()
                .map(numberPattern::matcher)
                .map(m -> {
                    List<String> l = Stream.generate(() -> m.find() ? m.group(1) : null)
                            .takeWhile(Objects::nonNull)
                            .map(d -> readDigit(d))
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

    private String readDigit(String d) {
        return switch (d) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> d;
        };
    }

    record Pair(String first, String last) {

        public long value() {
            return Long.parseLong(first + last);
        }
    }
}
