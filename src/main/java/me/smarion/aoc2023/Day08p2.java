package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day08p2 implements DayChallenge {
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

        Set<String> startNodes = paths.keySet().stream().filter(s -> s.endsWith("A")).collect(Collectors.toSet());

        return "" + startNodes.stream().map(s -> new Ghost(s, instructions, paths))
                .mapToLong(Ghost::getTravelLength)
                .mapToObj(BigInteger::valueOf)
                .reduce(Day08p2::lcm).get().longValue();
    }

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        BigInteger gcd = number1.gcd(number2);
        BigInteger absProduct = number1.multiply(number2).abs();
        return absProduct.divide(gcd);
    }

    static class Ghost {
        private final Map<String, CacheEntry> cache = new HashMap<>();
        private String currentLocation;
        private long step = 0;
        private final int instructionLength;

        public Ghost(String startLocation, char[] instructions, Map<String, Fork> paths) {
            this.currentLocation = startLocation;
            this.instructionLength = instructions.length;

            String current = startLocation;
            int step = 0;
            String prevLocation = current;
            long prevStep = step;
            while (!cache.containsKey(current + (step % instructionLength))) {
                current = instructions[step % instructionLength] == 'R' ?
                        paths.get(current).right() : paths.get(current).left();
                step++;
                if (current.endsWith("Z")) {
                    cache.put(prevLocation + (prevStep % instructionLength), new CacheEntry(current, step - prevStep));
                    prevLocation = current;
                    prevStep = step;
                }
            }
        }

        public long getTravelLength() {
            return cache.values().stream().map(CacheEntry::step).findFirst().get();
        }

        public Ghost advance() {
            CacheEntry cacheEntry = cache.get(currentLocation + step % instructionLength);

            currentLocation = cacheEntry.toLocation();
            step += cacheEntry.step();
            return this;
        }

        public long getStep() {
            return step;
        }

        record CacheEntry(String toLocation, long step) {
        }
    }

    record Fork(String current, String left, String right) {
    }
}
