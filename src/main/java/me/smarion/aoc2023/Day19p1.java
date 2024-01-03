package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19p1 implements DayChallenge {
    @Override
    public String run(String input) {
        String[] parts = input.trim().split("\n\n");
        Map<String, String> workflowText = parts[0].lines().collect(Collectors.toMap(
                s -> s.split("\\{")[0],
                s -> s.split("\\{")[1].split("}")[0])
        );

        Filter inputFilter = new WorkflowParser(workflowText).getInput();

        return "" + parts[1].lines()
                .map(Part::parse)
                .filter(inputFilter::examine)
                .mapToInt(Part::value)
                .sum();
    }

    private static final Pattern FILTER_PATTERN = Pattern.compile("([amxs])([<>])(\\d+):(\\w+)");

    private class WorkflowParser {
        private final Map<String, Filter> workflows;
        private final Map<String, String> workflowTexts;

        public WorkflowParser(Map<String, String> workflowTexts) {
            this.workflowTexts = workflowTexts;
            workflows = new HashMap<>(Map.of(
                    "A", p -> true,
                    "R", p -> false
            ));
            workflowTexts.forEach((k, v) -> {
                if (!workflows.containsKey(k)) {
                    workflows.put(k, parse(v));
                }
            });
        }

        public Filter getInput() {
            return workflows.get("in");
        }

        private Filter parse(String input) {
            String[] filters = input.split(",", 2);
            if (filters.length == 1) {
                if (!workflows.containsKey(filters[0])) {
                    workflows.put(filters[0], parse(workflowTexts.get(filters[0])));
                }
                return workflows.get(filters[0]);
            } else {
                Matcher filterMatcher = FILTER_PATTERN.matcher(filters[0]);
                if (filterMatcher.matches()) {
                    Function<Part, Integer> extractor = Part.getterFor(filterMatcher.group(1));
                    IntPredicate test = buildCondition(filterMatcher.group(2), filterMatcher.group(3));
                    Filter destinationTrue = parse(filterMatcher.group(4));
                    Filter destinationFalse = parse(filters[1]);
                    return new TestingFilter(extractor, test, destinationTrue, destinationFalse);
                } else {
                    throw new IllegalStateException(filters[0]);
                }
            }
        }
    }

    private static IntPredicate buildCondition(String symbol, String value) {
        int intValue = Integer.parseInt(value);
        return switch (symbol) {
            case "<" -> i -> i < intValue;
            case ">" -> i -> i > intValue;
            default -> throw new IllegalArgumentException(symbol);
        };
    }

    private interface Filter {
        boolean examine(Part p);
    }

    private record TestingFilter(Function<Part, Integer> extractor, IntPredicate test, Filter outputTrue,
                                 Filter outputFalse) implements Filter {
        @Override
        public boolean examine(Part p) {
            if (test.test(extractor.apply(p))) {
                return outputTrue.examine(p);
            } else {
                return outputFalse.examine(p);
            }
        }
    }

    private record Part(int x, int m, int a, int s) {
        public static Function<Part, Integer> getterFor(String carac) {
            return switch (carac) {
                case "x" -> Part::x;
                case "m" -> Part::m;
                case "a" -> Part::a;
                case "s" -> Part::s;
                default -> throw new IllegalArgumentException(carac);
            };
        }

        public static Part parse(String input) {
            String[] parts = input.split("[=,}]");
            return new Part(
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[5]),
                    Integer.parseInt(parts[7]));
        }

        public int value() {
            return x + m + a + s;
        }
    }
}
