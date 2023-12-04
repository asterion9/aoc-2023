package me.smarion.aoc2023;

import com.google.common.collect.Sets;
import me.smarion.aoc2023.util.DayChallenge;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day04p2 implements DayChallenge {

    @Override
    public String run(String input) {
        String[] lines = input.split("\n");

        int[] cardQuantity = new int[lines.length];
        Arrays.fill(cardQuantity, 1);

        for (String line : lines) {
            String[] cardContents = line.split(":\\s+");
            int cardNum = Integer.parseInt(cardContents[0].substring(4).trim()) - 1;
            String[] numbers = cardContents[1].split("\\s+\\|\\s+");
            String myPart = numbers[0];
            String winningPart = numbers[1];
            Set<String> myNumber = Stream.of(myPart.split("\\s+")).collect(Collectors.toSet());
            Set<String> winningNumbers = Stream.of(winningPart.split("\\s+")).collect(Collectors.toSet());
            int numberMatched = Sets.intersection(myNumber, winningNumbers).size();
            for (int i = 1; i <= numberMatched; i++) {
                cardQuantity[cardNum + i] += cardQuantity[cardNum];
            }
        }
        return IntStream.of(cardQuantity).sum() + "";
    }
}
