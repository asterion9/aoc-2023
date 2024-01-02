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

            long totalRemoved = -nextIntervals.stream()
                    .mapToLong(next -> mergeInterval(next, currentIntervals))
                    .filter(i -> i < 0)
                    .sum();

            area += (nextX - currentX) * currentLength;

            long nextLength = currentIntervals.stream()
                    .mapToLong(i -> i.e - i.s + 1)
                    .sum();

            if (totalRemoved > 0) {
                area += totalRemoved;
            }

            currentLength = nextLength;
            currentX = nextX;
        }

        return area + "";
    }

    public static long mergeInterval(Interval next, List<Interval> currentIntervals) {
        int i = 0;
        long sizeVar;
        while (i < currentIntervals.size()) {
            if (currentIntervals.get(i).isAdding(next)) {
                Interval removed = currentIntervals.remove(i);
                next = removed.join(next);
                sizeVar = next.length() - removed.length();
                if (currentIntervals.size() > i && currentIntervals.get(i).isAdding(next)) {
                    removed = currentIntervals.remove(i);
                    next = removed.join(next);
                    sizeVar = next.length() - removed.length();
                }
                currentIntervals.add(i, next);
                return sizeVar;
            } else if (currentIntervals.get(i).isSubstracting(next)) {
                Interval removed = currentIntervals.remove(i);
                List<Interval> remaining = removed.substract(next);
                currentIntervals.addAll(i, remaining);
                return remaining.stream().mapToLong(Interval::length).sum() - removed.length();
            } else if (currentIntervals.get(i).s() > next.e()) {
                currentIntervals.add(i, next);
                return next.length();
            } else {
                i++;
            }
        }
        currentIntervals.add(next);
        return next.length();
    }

    public record Interval(long s, long e) {
        public boolean isAdding(Interval other) {
            return other.s - e == 0 || other.s - e == 1 || s - other.e == 0 || s - other.e == 1;
        }

        public long length() {
            return e - s + 1;
        }

        public boolean isSubstracting(Interval other) {
            return other.s >= s && other.e <= e;
        }

        public Interval join(Interval other) {
            return new Interval(Math.min(other.s, s), Math.max(other.e, e));
        }

        public List<Interval> substract(Interval other) {
            List<Interval> result = new ArrayList<>();
            if (s != other.s) {
                result.add(new Interval(s, other.s));
            }
            if (e != other.e) {
                result.add(new Interval(other.e, e));
            }
            return result;
        }
    }


    private record Position(long x, long y) {
    }

    private enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        //        public static Direction parsePlan(char c) {
//            return switch (c) {
//                case 'U' -> UP;
//                case 'D' -> DOWN;
//                case 'R' -> RIGHT;
//                case 'L' -> LEFT;
//                default -> throw new IllegalArgumentException(c + " is not a valid direction");
//            };
//        }
//    }
//
//    private record PlanLine(Direction direction, int length) {
//        public static PlanLine parse(String input) {
//            String[] parts = input.split("\\s+");
//            return new PlanLine(
//                    Direction.parsePlan(parts[0].charAt(0)),
//                    Integer.parseInt(parts[1])
//            );
//        }
//    }
        public static Direction parsePlan(char c) {
            return switch (c) {
                case '3' -> UP;
                case '1' -> DOWN;
                case '0' -> RIGHT;
                case '2' -> LEFT;
                default -> throw new IllegalArgumentException(c + " is not a valid direction");
            };
        }
    }

    private record PlanLine(Direction direction, long length) {
        public static PlanLine parse(String input) {
            String instruction = input.split("\\s+")[2].substring(2, 8);
            return new PlanLine(
                    Direction.parsePlan(instruction.charAt(5)),
                    Long.parseLong(instruction.substring(0, 5), 16)
            );
        }
    }
}
