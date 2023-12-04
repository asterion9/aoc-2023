package me.smarion.aoc2023.util;

import java.io.IOException;

public interface DayChallenge {
    String run(String input);

    default int getDay() {
        return Integer.parseInt(this.getClass().getSimpleName().substring(3, 5));
    }

    private String loadInputFile() {
        try {
            return DataFetcher.fetch(getDay());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    default String run() {
        return run(loadInputFile());
    }
}
