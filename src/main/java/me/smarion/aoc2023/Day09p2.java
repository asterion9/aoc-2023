package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;

import java.util.concurrent.atomic.AtomicLong;

public class Day09p2 implements DayChallenge {
    @Override
    public String run(String input) {
        return StreamEx.of(input.trim().lines())
                .mapToLong(l -> {
                            String[] serieValues = l.split("\s+");
                            return extrapolateNext(serieValues.length, StreamEx.ofReversed(serieValues).mapToLong(Long::parseLong));
                        }
                ).sum() + "";
    }

    public static long extrapolateNext(int length, LongStreamEx serie) {
        if (length == 1) {
            return serie.sum();
        }
        final AtomicLong lastValue = new AtomicLong();
        return extrapolateNext(length - 1, serie
                .dropWhile(l -> l == 0)
                .peekLast(lastValue::set)
                .pairMap((a, b) -> b - a)) + lastValue.get();
    }
}
