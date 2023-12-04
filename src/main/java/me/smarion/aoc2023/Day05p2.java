package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day05p2 implements DayChallenge {
    @Override
    public String run(String input) {
        String[] lines = input.split("\n\n");
        long[] seeds = Stream.of(lines[0].split(" "))
                .skip(1)
                .mapToLong(Long::parseLong)
                .toArray();

        List<Range> seedRanges = new ArrayList<>();
        for (int i = 0; i < seeds.length; i += 2) {
            seedRanges.add(new Range(seeds[i], seeds[i] + seeds[i + 1]));
        }

        Map<String, Step> transformers = Stream.of(lines).skip(1)
                .map(Step::parse)
                .collect(Collectors.toMap(
                        Step::from,
                        Function.identity()
                ));


        String step = "seed";
        List<Range> ranges = seedRanges;
        while (!"location".equals(step)) {
            Step transformer = transformers.get(step);
            ranges = transformer.transform(ranges);
            step = transformer.to();
        }
        return ranges.get(0).start() + "";
    }


    record Step(String from, String to, List<Transformer> transformers) {

        private static final Pattern sourceDestinationPatter = Pattern.compile("^(\\w+)-to-(\\w+) map:$");

        public static Step parse(String transformerInput) {
            String[] lines = transformerInput.split("\n");
            Matcher matcher = sourceDestinationPatter.matcher(lines[0]);
            if (!matcher.matches()) {
                throw new IllegalStateException("can't parse :\n" + transformerInput);
            }
            List<Transformer> rts = Stream.of(lines).skip(1)
                    .map(Transformer::parse)
                    .toList();
            return new Step(matcher.group(1), matcher.group(2), rts);
        }

        public List<Range> transform(List<Range> sources) {
            List<Range> result = new ArrayList<>();
            List<Range> remainings = sources;
            for (Transformer internalTransformer : transformers()) {
                List<Range> nextRemainings = new ArrayList<>();
                for (Range remaining : remainings) {
                    remaining.intersection(internalTransformer.range)
                            .map(r -> r.add(internalTransformer.offset()))
                            .ifPresent(result::add);
                    nextRemainings.addAll(remaining.exclusion(internalTransformer.range));
                }
                remainings = nextRemainings;
            }
            result.addAll(remainings);
            return Range.merge(result);
        }
    }


    record Transformer(Range range, long offset) {
        public static Transformer parse(String line) {
            String[] numbers = line.split(" ");
            long sourceValue = Long.parseLong(numbers[1]);
            return new Transformer(new Range(sourceValue, sourceValue + Long.parseLong(numbers[2])), Long.parseLong(numbers[0]) - sourceValue);
        }
    }

    record Range(long start, long end) {

        static List<Range> merge(List<Range> ranges) {
            if (ranges == null || ranges.isEmpty()) {
                return Collections.emptyList();
            }

            // Sort the ranges based on the start value
            ranges.sort(Comparator.comparingLong(Range::start));

            List<Range> mergedRanges = new ArrayList<>();
            Range currentRange = ranges.get(0);

            for (int i = 1; i < ranges.size(); i++) {
                Range nextRange = ranges.get(i);

                // Check for overlap
                if (currentRange.end() >= nextRange.start()) {
                    // Merge the overlapping ranges
                    currentRange = new Range(currentRange.start(), Math.max(currentRange.end(), nextRange.end()));
                } else {
                    // Add the non-overlapping range to the result
                    mergedRanges.add(currentRange);
                    currentRange = nextRange;
                }
            }

            // Add the last range to the result
            mergedRanges.add(currentRange);

            return mergedRanges;
        }

        Range add(long offset) {
            return new Range(start + offset, end + offset);
        }

        Optional<Range> intersection(Range other) {
            // Check for non-overlapping ranges
            if (end < other.start || other.end < start) {
                return Optional.empty();  // No intersection
            }

            // Calculate the intersection range
            long intersectionStart = Math.max(start, other.start);
            long intersectionEnd = Math.min(end, other.end);

            // Check if there is a valid intersection
            if (intersectionStart <= intersectionEnd) {
                Range intersectionRange = new Range(intersectionStart, intersectionEnd);
                return Optional.of(intersectionRange);
            } else {
                return Optional.empty();  // No valid intersection
            }
        }

        List<Range> exclusion(Range other) {
            List<Range> excludedRanges = new ArrayList<>();

            // Check for non-overlapping ranges
            if (end < other.start || other.end < start) {
                // The entire range is excluded
                excludedRanges.add(this);
            } else {
                // Check and add excluded portions before the other range
                if (start < other.start) {
                    excludedRanges.add(new Range(start, other.start));
                }

                // Check and add excluded portions after the other range
                if (other.end < end) {
                    excludedRanges.add(new Range(other.end, end));
                }
            }

            return excludedRanges;
        }
    }
}
