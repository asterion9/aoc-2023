package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day02p2 implements DayChallenge {
    @Override
    public String run(String input) {
        return input.trim().lines()
                .map(l -> {
                    String[] split = l.split(": ");
                    return Stream.of(split[1].split("; "))
                            .map(CubeSet::parse)
                            .reduce(CubeSet::max).get();
                })
                .mapToLong(CubeSet::power)
                .sum() + "";
    }


    record CubeSet(int blue, int green, int red) {
        private static Pattern BLUE = Pattern.compile("(\\d+) blue");
        private static Pattern GREEN = Pattern.compile("(\\d+) green");
        private static Pattern RED = Pattern.compile("(\\d+) red");

        public static CubeSet parse(String hand) {
            var blueM = BLUE.matcher(hand);
            var greenM = GREEN.matcher(hand);
            var redM = RED.matcher(hand);
            return new CubeSet(
                    blueM.find() ? Integer.parseInt(blueM.group(1)) : 0,
                    greenM.find() ? Integer.parseInt(greenM.group(1)) : 0,
                    redM.find() ? Integer.parseInt(redM.group(1)) : 0
            );
        }

        public CubeSet max(CubeSet other) {
            return new CubeSet(Math.max(blue, other.blue), Math.max(green, other.green), Math.max(red, other.red));
        }

        public long power() {
            return (long) red * (long) green * (long) blue;
        }
    }
}
