package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19p2 implements DayChallenge {
    @Override
    public String run(String input) {
        String[] parts = input.trim().split("\n\n");
        Map<String, String> workflowText = parts[0].lines().collect(Collectors.toMap(
                s -> s.split("\\{")[0],
                s -> s.split("\\{")[1].split("}")[0])
        );

        Filter inputFilter = new WorkflowParser(workflowText).getInput();

        Interval interval = new Interval(1, 4000);
        return inputFilter.count(new HyperCube(interval, interval, interval, interval)) + "";
    }

    private static final Pattern FILTER_PATTERN = Pattern.compile("([amxs])([<>])(\\d+):(\\w+)");

    private class WorkflowParser {
        private final Map<String, Filter> workflows;
        private final Map<String, String> workflowTexts;

        public WorkflowParser(Map<String, String> workflowTexts) {
            this.workflowTexts = workflowTexts;
            workflows = new HashMap<>(Map.of(
                    "A", new Filter() {
                        @Override
                        public long count(HyperCube p) {
                            return p.volume();
                        }

                        @Override
                        public String toString() {
                            return "A";
                        }
                    },
                    "R", new Filter() {
                        @Override
                        public long count(HyperCube p) {
                            return 0;
                        }

                        @Override
                        public String toString() {
                            return "R";
                        }
                    }
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
                    String dimension = filterMatcher.group(1);
                    int splitValue = Integer.parseInt(filterMatcher.group(3));
                    Filter destinationTrue = parse(filterMatcher.group(4));
                    Filter destinationFalse = parse(filters[1]);
                    return switch (filterMatcher.group(2)) {
                        case "<" -> new SplitFilter(dimension, splitValue, destinationTrue, destinationFalse);
                        case ">" -> new SplitFilter(dimension, splitValue + 1, destinationFalse, destinationTrue);
                        default -> throw new IllegalArgumentException(filterMatcher.group(2));
                    };
                } else {
                    throw new IllegalStateException(filters[0]);
                }
            }
        }
    }

    private interface Filter {
        long count(HyperCube p);
    }

    private record SplitFilter(String dimension, int splitValue, Filter outputInf, Filter outputSup) implements Filter {
        @Override
        public long count(HyperCube p) {
            if (p.get(dimension).s() >= splitValue) {
                return outputSup.count(p);
            } else if (p.get(dimension).e() < splitValue) {
                return outputInf.count(p);
            } else {
                Pair split = p.split(dimension, splitValue);
                return outputInf.count(split.bottom()) + outputSup.count(split.top());
            }
        }

        @Override
        public String toString() {
            return "SplitFilter{" +
                    "dimension='" + dimension + '\'' +
                    ", splitValue=" + splitValue +
                    ", outputInf=" + outputInf +
                    ", outputSup=" + outputSup +
                    '}';
        }
    }

    private record Interval(int s, int e) {
        public long length() {
            return e - s + 1;
        }
    }

    private record HyperCube(Interval x, Interval m, Interval a, Interval s) {
        public Pair split(String dimension, int value) {
            return switch (dimension) {
                case "x" ->
                        new Pair(new HyperCube(new Interval(x.s, value - 1), m, a, s), new HyperCube(new Interval(value, x.e), m, a, s));
                case "m" ->
                        new Pair(new HyperCube(x, new Interval(m.s, value - 1), a, s), new HyperCube(x, new Interval(value, m.e), a, s));
                case "a" ->
                        new Pair(new HyperCube(x, m, new Interval(a.s, value - 1), s), new HyperCube(x, m, new Interval(value, a.e), s));
                case "s" ->
                        new Pair(new HyperCube(x, m, a, new Interval(s.s, value - 1)), new HyperCube(x, m, a, new Interval(value, s.e)));
                default -> throw new IllegalArgumentException(dimension);
            };
        }

        public long volume() {
            return x.length() * m.length() * a.length() * s.length();
        }

        public Interval get(String dimension) {
            return switch (dimension) {
                case "x" -> x;
                case "m" -> m;
                case "a" -> a;
                case "s" -> s;
                default -> throw new IllegalArgumentException(dimension);
            };
        }
    }

    private record Pair(HyperCube bottom, HyperCube top) {
    }
}
