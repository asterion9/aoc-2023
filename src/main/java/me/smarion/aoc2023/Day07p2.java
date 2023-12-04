package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day07p2 implements DayChallenge {

    @Override
    public String run(String input) {
        List<Hand> hands = Arrays.stream(input.trim().split("\n"))
                .map(Hand::parse)
                .sorted()
                .toList();

        long result = 0;

        for (int i = 0; i < hands.size(); i++) {
            result += hands.get(i).bid * (i + 1);
        }
        return result + "";
    }

    enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND;

        static HandType fromCards(char[] cards) {
            char[] c = Arrays.copyOf(cards, cards.length);
            Arrays.sort(c);
            int nbJack = 0;
            for (char card : c) {
                if (card == 'J')
                    nbJack++;
            }

            boolean eq01 = c[0] == c[1];
            boolean eq12 = c[1] == c[2];
            boolean eq23 = c[2] == c[3];
            boolean eq34 = c[3] == c[4];
            if (eq01 && eq12 && eq23 && eq34)
                return FIVE_OF_A_KIND;
            if ((eq12 && eq23) && (eq01 || eq34)) {
                if (nbJack == 1 || nbJack == 4) {
                    return FIVE_OF_A_KIND;
                }
                return FOUR_OF_A_KIND;
            }
            if ((eq01 && eq12) && eq34 || eq01 && (eq23 && eq34)) {
                if (nbJack == 2 || nbJack == 3) {
                    return FIVE_OF_A_KIND;
                }
                return FULL_HOUSE;
            }
            if ((eq01 && eq12) || (eq12 && eq23) || (eq23 && eq34)) {
                if (nbJack == 1) {
                    return FOUR_OF_A_KIND;
                }
                if (nbJack == 3) {
                    return FOUR_OF_A_KIND;
                }
                return THREE_OF_A_KIND;
            }
            if ((eq01 && eq23) || (eq01 && eq34) || (eq12 && eq34)) {
                if (nbJack == 1) {
                    return FULL_HOUSE;
                }
                if (nbJack == 2) {
                    return FOUR_OF_A_KIND;
                }
                return TWO_PAIR;
            }
            if (eq01 || eq12 || eq23 || eq34) {
                if (nbJack == 1) {
                    return THREE_OF_A_KIND;
                }
                if (nbJack == 2) {
                    return THREE_OF_A_KIND;
                }
                return ONE_PAIR;
            }
            if (nbJack == 1) {
                return ONE_PAIR;
            }
            return HIGH_CARD;
        }
    }

    private static Comparator<Character> cardValueComparator = new Comparator<Character>() {
        private static int[] cardValue = new int[256];

        private static char[] cardFaces = {'J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'};

        static {
            for (int i = 0; i < cardFaces.length; i++) {
                cardValue[cardFaces[i]] = i;
            }
        }

        @Override
        public int compare(Character o1, Character o2) {
            return Integer.compare(cardValue[o1], cardValue[o2]);
        }
    };


    record Hand(HandType handType, char[] cards, int bid) implements Comparable<Hand> {
        static Hand parse(String input) {
            String[] parts = input.split("\\s+");
            char[] cards = parts[0].toCharArray();
            int bid = Integer.parseInt(parts[1]);
            return new Hand(HandType.fromCards(cards), cards, bid);
        }

        @Override
        public int compareTo(Hand o) {
            return Comparator.comparing(Hand::handType)
                    .thenComparing(h -> h.cards[0], cardValueComparator)
                    .thenComparing(h -> h.cards[1], cardValueComparator)
                    .thenComparing(h -> h.cards[2], cardValueComparator)
                    .thenComparing(h -> h.cards[3], cardValueComparator)
                    .thenComparing(h -> h.cards[4], cardValueComparator)
                    .compare(this, o);
        }
    }
}
