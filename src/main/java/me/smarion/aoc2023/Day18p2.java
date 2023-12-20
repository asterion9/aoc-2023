package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.StreamEx;

import java.util.*;

public class Day18p2 implements DayChallenge {
    @Override
    public String run(String input) {
        List<PlanLine> instructions = input.trim().lines()
                .map(PlanLine::parse)
                .toList();

        List<Position> positions = StreamEx.of(instructions)
                .scanLeft(new Position(0, 0), ((p, planLine) ->
                        switch (planLine.direction) {
                            case UP -> new Position(p.x, p.y - planLine.length);
                            case RIGHT -> new Position(p.x + planLine.length, p.y);
                            case DOWN -> new Position(p.x, p.y + planLine.length);
                            case LEFT -> new Position(p.x - planLine.length, p.y);
                        }));

        TreeMap<Long, List<Position>> posSortedByX = StreamEx.of(positions)
                .skip(1)
                .sorted(Comparator.comparing(Position::y))
                .foldLeft(new TreeMap<>(), (t, p) -> {
                    t.computeIfAbsent(p.x(), k -> new ArrayList<>()).add(p);
                    return t;
                });

        long area = 0;
        long currentX = posSortedByX.firstKey();
        long currentLength = 0;
        List<Interval> currentIntervals = new ArrayList<>();
        while (!posSortedByX.isEmpty()) {
            Map.Entry<Long, List<Position>> longListEntry = posSortedByX.pollFirstEntry();
            long nextX = longListEntry.getKey();
            List<Position> nextVertex = longListEntry.getValue();

            List<Interval> nextIntervals = new ArrayList<>();
            while (!nextVertex.isEmpty()) {
                nextIntervals.add(new Interval(nextVertex.remove(0).y, nextVertex.remove(0).y));
            }

            currentIntervals.addAll(
                    nextIntervals.stream()
                            .map(next -> {
                                int i = 0;
                                while (i < currentIntervals.size()) {
                                    if (currentIntervals.get(i).isAdding(next)) {
                                        next = currentIntervals.remove(i).join(next);
                                    } else if (currentIntervals.get(i).isSubstracting(next)) {
                                        next = currentIntervals.remove(i).disjoin(next);
                                    } else {
                                        i++;
                                    }
                                }
                                return next;
                            }).toList()
            );

            area += (nextX - currentX) * currentLength;

            long nextLength = currentIntervals.stream()
                    .mapToLong(i -> i.e - i.s + 1)
                    .sum();

            if (nextLength < currentLength) {
                area += currentLength - nextLength;
            }

            currentLength = nextLength;
            currentX = nextX;
        }

        return area + "";
    }

    private record Interval(long s, long e) {
        public boolean isAdding(Interval other) {
            return other.s == e || other.e == s;
        }

        public boolean isSubstracting(Interval other) {
            return e >= other.s && other.e >= s;
        }

        public Interval join(Interval other) {
            return new Interval(Math.min(other.s, s), Math.max(other.e, e));
        }

        public Interval disjoin(Interval other) {
            if (other.s == s) {
                return new Interval(Math.min(other.e, e), Math.max(other.e, e));
            } else {
                return new Interval(Math.min(other.s, s), Math.max(other.s, s));
            }
        }
    }


    private record Position(long x, long y) {
    }

    private enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        public static Direction parsePlan(char c) {
            return switch (c) {
                case 'U' -> UP;
                case 'D' -> DOWN;
                case 'R' -> RIGHT;
                case 'L' -> LEFT;
                default -> throw new IllegalArgumentException(c + " is not a valid direction");
            };
        }
    }

    private record PlanLine(Direction direction, int length) {
        public static PlanLine parse(String input) {
            String[] parts = input.split("\\s+");
            return new PlanLine(
                    Direction.parsePlan(parts[0].charAt(0)),
                    Integer.parseInt(parts[1])
            );
        }
    }
//        public static Direction parsePlan(char c) {
//            return switch (c) {
//                case '3' -> UP;
//                case '1' -> DOWN;
//                case '0' -> RIGHT;
//                case '2' -> LEFT;
//                default -> throw new IllegalArgumentException(c + " is not a valid direction");
//            };
//        }
//    }
//
//    private record PlanLine(Direction direction, long length) {
//        public static PlanLine parse(String input) {
//            String instruction = input.split("\\s+")[2].substring(2, 8);
//            return new PlanLine(
//                    Direction.parsePlan(instruction.charAt(5)),
//                    Long.parseLong(instruction.substring(0, 5), 16)
//            );
//        }
//    }
}
