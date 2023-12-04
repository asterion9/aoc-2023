package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Day06p2 implements DayChallenge {
    @Override
    public String run(String input) {
        String[] lines = input.trim().split("\n");
        String timeText = lines[0].substring(5).replaceAll("\\s+", "");
        String distanceText = lines[1].substring(9).replaceAll("\\s+", "");

        long time = Long.parseLong(timeText);
        long distance = Long.parseLong(distanceText);
        Roots roots = roots(-1, time, -distance);
        return "" + (prevNumber(roots.x2()) - nextNumber(roots.x1()) + 1);
    }

    private static long nextNumber(BigDecimal base) {
        BigDecimal rounded = base.setScale(0, RoundingMode.CEILING);
        if (rounded.equals(base)) {
            return rounded.longValue() + 1;
        }
        return rounded.longValue();
    }

    private static long prevNumber(BigDecimal base) {
        BigDecimal rounded = base.setScale(0, RoundingMode.FLOOR);
        if (rounded.equals(base)) {
            return rounded.longValue() - 1;
        }
        return rounded.longValue();
    }

    // distance = totalTime * timePressed - timePressed^2
    private long distance(long timePressed, long totalTime) {
        return (totalTime - timePressed) * timePressed;
    }

    private Roots roots(long a, long b, long c) {
        // equation is a + b*x + c*x^2 = 0

        BigDecimal discriminant = (BigDecimal.valueOf(b).pow(2))
                .add(BigDecimal.valueOf(-4).multiply(BigDecimal.valueOf(a)).multiply(BigDecimal.valueOf(c)));

        BigDecimal discRoot = discriminant.sqrt(MathContext.DECIMAL128);
        BigDecimal twoA = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(2));

        return new Roots(
                (BigDecimal.valueOf(-b).add(discRoot)).divide(twoA, RoundingMode.CEILING),
                (BigDecimal.valueOf(-b).add(discRoot.negate())).divide(twoA, RoundingMode.CEILING)
        );
    }

    record Roots(BigDecimal x1, BigDecimal x2) {
    }

    ;
}
