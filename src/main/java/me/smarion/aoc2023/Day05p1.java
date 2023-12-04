package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day05p1 implements DayChallenge {
    @Override
    public String run(String input) {
        String[] lines = input.split("\n\n");
        long[] seeds = Stream.of(lines[0].split(" "))
                .skip(1)
                .mapToLong(Long::parseLong)
                .toArray();

        Map<String, Transform> transformers = Stream.of(lines).skip(1)
                .map(Transform::parse)
                .collect(Collectors.toMap(
                        Transform::from,
                        Function.identity()
                ));

        return "" + LongStream.of(seeds)
                .map(s -> {
                    String step = "seed";
                    long value = s;
                    while (!"location".equals(step)) {
                        Transform transformer = transformers.get(step);
                        value = transformer.transform(value);
                        step = transformer.to();
                    }
                    return value;
                }).min().getAsLong();
    }

    record Transform(String from, String to, List<RangeTransform> internalTransformers) {

        private static final Pattern sourceDestinationPatter = Pattern.compile("^(\\w+)-to-(\\w+) map:$");

        public static Transform parse(String transformerInput) {
            String[] lines = transformerInput.split("\n");
            Matcher matcher = sourceDestinationPatter.matcher(lines[0]);
            if (!matcher.matches()) {
                throw new IllegalStateException("can't parse :\n" + transformerInput);
            }
            List<RangeTransform> rts = Stream.of(lines).skip(1)
                    .map(RangeTransform::parse)
                    .toList();
            return new Transform(matcher.group(1), matcher.group(2), rts);
        }

        public long transform(long source) {
            for (RangeTransform rt : internalTransformers) {
                if (rt.canTransform(source)) {
                    return rt.transform(source);
                }
            }
            return source;
        }
    }

    ;

    record RangeTransform(long sourceStart, long length, long offset) {
        public static RangeTransform parse(String line) {
            String[] numbers = line.split(" ");
            long sourceValue = Long.parseLong(numbers[1]);
            return new RangeTransform(sourceValue, Long.parseLong(numbers[2]), Long.parseLong(numbers[0]) - sourceValue);
        }

        boolean canTransform(long value) {
            return value >= sourceStart && (value - sourceStart) < length;
        }

        long transform(long value) {
            return value + offset;
        }
    }
}
