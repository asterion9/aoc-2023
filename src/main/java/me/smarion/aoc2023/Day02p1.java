package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day02p1 implements DayChallenge {

    @Override
    public String run(String input) {
        final CubeSet condition = new CubeSet(14, 13, 12);
        return input.trim().lines()
                .map(l -> {
                    String[] split = l.split(": ");
                    return new Game(Integer.parseInt(split[0].substring(5)),
                            Stream.of(split[1].split("; "))
                                    .map(CubeSet::parse)
                                    .collect(Collectors.toSet())
                    );
                })
                .filter(g -> g.hands.stream().allMatch(h -> h.isContainedIn(condition)))
                .mapToInt(g -> g.id)
                .sum() + "";
    }

    record Game(int id, Set<CubeSet> hands) {
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

        public boolean isContainedIn(CubeSet other) {
            return blue <= other.blue && green <= other.green && red <= other.red;
        }
    }
}
