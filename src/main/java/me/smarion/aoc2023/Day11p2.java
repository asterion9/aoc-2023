package me.smarion.aoc2023;

import com.google.common.base.Objects;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.StreamEx;

public class Day11p2 implements DayChallenge {

  private final int expansion;

  public Day11p2() {
    this(1_000_000);
  }

  public Day11p2(int expansion) {
    this.expansion = expansion;
  }

  @Override
  public String run(String input) {
    char[][] galaxiesMap = input.trim().lines()
      .map(String::toCharArray)
      .toArray(char[][]::new);

    Set<Galaxy> galaxies = new HashSet<>();

    for (int x = 0; x < galaxiesMap.length; x++) {
      for (int y = 0; y < galaxiesMap[x].length; y++) {
        if (galaxiesMap[x][y] == '#') {
          galaxies.add(new Galaxy(x, y));
        }
      }
    }
    {
      AtomicLong xOffset = new AtomicLong(0);
      AtomicLong prevXindex = new AtomicLong(-1);
      galaxies.stream()
        .sorted(Comparator.comparingLong(Galaxy::getX))
        .forEachOrdered(g -> {
          xOffset.addAndGet((expansion-1) * Math.max(0, g.getX() - prevXindex.get() - 1));
          prevXindex.set(g.getX());
          g.setX(g.getX() + xOffset.get());
        });
    }

    {
      AtomicLong yOffset = new AtomicLong(0);
      AtomicLong prevYindex = new AtomicLong(-1);
      galaxies.stream()
        .sorted(Comparator.comparingLong(Galaxy::getY))
        .forEachOrdered(g -> {
          yOffset.addAndGet((expansion-1) * Math.max(0, g.getY() - prevYindex.get() - 1));
          prevYindex.set(g.getY());
          g.setY(g.getY() + yOffset.get());
        });
    }

    return "" + StreamEx.ofPairs(
        galaxies.stream().toList(),
        Galaxy::distance)
      .mapToLong(Long::longValue)
      .sum();
  }

  private static class Galaxy {
    private long x, y;

    public Galaxy(long x, long y) {
      this.x = x;
      this.y = y;
    }

    public long distance(Galaxy other) {
      return Math.abs(other.getY() - getY()) + Math.abs(other.getX() - getX());
    }

    public Galaxy setX(long x) {
      this.x = x;
      return this;
    }

    public Galaxy setY(long y) {
      this.y = y;
      return this;
    }

    public long getX() {
      return x;
    }

    public long getY() {
      return y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Galaxy galaxy = (Galaxy) o;
      return x == galaxy.x && y == galaxy.y;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(x, y);
    }
  }
}
