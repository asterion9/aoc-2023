package me.smarion.aoc2023;

import com.google.common.collect.Sets;
import me.smarion.aoc2023.util.DayChallenge;
import me.smarion.aoc2023.util.PatternIterator;

import java.util.*;
import java.util.regex.Pattern;

public class Day03p2 implements DayChallenge {

    private final static Pattern numberPattern = Pattern.compile("\\d+");
    private final static Pattern symbolPattern = Pattern.compile("[^\\d.]");

    @Override
    public String run(String input) {
        String[] lines = input.split("\n");

        Map<Symbol, List<Integer>> values = new HashMap<>();
        for (int y = 0; y < lines.length; y++) {
            PatternIterator patternIterator = new PatternIterator(numberPattern, lines[y]);
            while (patternIterator.hasNext()) {
                PatternIterator.Match m = patternIterator.next();
                Set<Symbol> symbols = findSymbols(y, lines[y], m);
                if (y != 0) {
                    symbols = Sets.union(symbols, findSymbols(y - 1, lines[y - 1], m));
                }
                if (y != (lines.length - 1)) {
                    symbols = Sets.union(symbols, findSymbols(y + 1, lines[y + 1], m));
                }
                symbols.forEach(s -> values
                        .computeIfAbsent(s, k -> new ArrayList<>())
                        .add(Integer.parseInt(m.value())));
            }

        }
        return values.entrySet().stream()
                .filter(e -> e.getKey().value == '*')
                .filter(e -> e.getValue().size() == 2)
                .mapToLong(e -> e.getValue().stream().mapToLong(i -> i).reduce(1, (a, b) -> a * b)).sum() + "";
    }

    private static Set<Symbol> findSymbols(int y, String line, PatternIterator.Match m) {
        int lineStart = Math.max(0, m.start() - 1);
        int lineEnd = Math.min(line.length(), m.end() + 1);
        PatternIterator patternIterator = new PatternIterator(symbolPattern, line.substring(lineStart, lineEnd));
        Set<Symbol> symbols = new HashSet<>();
        for (PatternIterator.Match match : patternIterator) {
            symbols.add(new Symbol(lineStart + match.start(), y, match.value().charAt(0)));
        }
        return symbols;
    }

    private record Symbol(int x, int y, char value) {
    }

    ;
}
