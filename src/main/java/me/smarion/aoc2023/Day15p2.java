package me.smarion.aoc2023;

import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.IntStreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Day15p2 implements DayChallenge {
    @Override
    public String run(String input) {
        List<ArrayList<Lens>> boxes = Stream.generate(() -> new ArrayList<Lens>()).limit(256).toList();

        input.trim().lines().flatMap(line -> Stream.of(line.split(",")))
                .forEachOrdered(l -> {
                    if (l.contains("-")) {
                        String lensName = l.substring(0, l.length() - 1);
                        boxes.get(hash(lensName)).removeIf(lens -> lensName.equals(lens.label()));
                    } else {
                        Lens lens = Lens.parse(l);
                        ArrayList<Lens> box = boxes.get(lens.hash());
                        int index = box.indexOf(lens);
                        if (index == -1) {
                            box.add(lens);
                        } else {
                            box.set(index, lens);
                        }
                    }
                });

        long focusingPower = 0;

        for (int i = 0; i < boxes.size(); i++) {
            ArrayList<Lens> box = boxes.get(i);
            for (int j = 0; j < box.size(); j++) {
                focusingPower += (long) (i + 1) * (j + 1) * box.get(j).focal();
            }
        }

        return "" + focusingPower;
    }


    private static int hash(String text) {
        return IntStreamEx.of(text.toCharArray())
                .foldLeft(0, (a, b) -> ((a + b) * 17) % 256);
    }

    private record Lens(String label, int focal, int hash) {
        public static Lens parse(String input) {
            String[] parts = input.split("=");
            return new Lens(parts[0], Integer.parseInt(parts[1]), Day15p2.hash(parts[0]));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lens lens = (Lens) o;
            return Objects.equals(label, lens.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }
}
