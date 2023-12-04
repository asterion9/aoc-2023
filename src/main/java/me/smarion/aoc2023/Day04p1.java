package me.smarion.aoc2023;

import com.google.common.collect.Sets;
import me.smarion.aoc2023.util.DayChallenge;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day04p1 implements DayChallenge {

    @Override
    public String run(String input) {
        String[] lines = input.split("\n");
        long total = 0;

        for (String line : lines) {
            String[] numbers = line.split(":\\s+")[1].split("\\s+\\|\\s+");
            String myPart = numbers[0];
            String winningPart = numbers[1];
            Set<String> myNumber = Stream.of(myPart.split("\\s+")).collect(Collectors.toSet());
            Set<String> winningNumbers = Stream.of(winningPart.split("\\s+")).collect(Collectors.toSet());
            int numberMatched = Sets.intersection(myNumber, winningNumbers).size();
            if (numberMatched > 0) {
                total += 1 << (numberMatched - 1);
            }
        }

        return total + "";
    }
}
