package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;
import me.smarion.aoc2023.util.PatternIterator;

import java.util.regex.Pattern;

public class Day03p1 implements DayChallenge {

    private final static Pattern numberPattern = Pattern.compile("\\d+");
    private final static Pattern symbolPattern = Pattern.compile("[^\\d.]");

    @Override
    public String run(String input) {
        String[] lines = input.split("\n");

        int total = 0;
        for (int y = 0; y < lines.length; y++) {
            PatternIterator patternIterator = new PatternIterator(numberPattern, lines[y]);
            while (patternIterator.hasNext()) {
                PatternIterator.Match m = patternIterator.next();

                if ((y != 0 && matchInLine(lines[y - 1], m))
                        || matchInLine(lines[y], m)
                        || (y != (lines.length - 1) && matchInLine(lines[y + 1], m))) {
                    total += Integer.parseInt(m.value());
                }
            }
        }

        return total + "";
    }

    private static boolean matchInLine(String line, PatternIterator.Match m) {
        return symbolPattern.matcher(line.substring(Math.max(0, m.start() - 1), Math.min(line.length(), m.end() + 1))).find();
    }
}
