package me.smarion.aoc2023;

import com.google.common.base.Objects;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import me.smarion.aoc2023.util.DayChallenge;
import one.util.streamex.StreamEx;

public class Day11p1 implements DayChallenge {
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
      AtomicInteger xOffset = new AtomicInteger(0);
      AtomicInteger prevXindex = new AtomicInteger(-1);
      galaxies.stream()
        .sorted(Comparator.comparingInt(Galaxy::getX))
        .forEachOrdered(g -> {
          xOffset.addAndGet(Math.max(0, g.getX() - prevXindex.get() - 1));
          prevXindex.set(g.getX());
          g.setX(g.getX() + xOffset.get());
        });
    }

    {
      AtomicInteger yOffset = new AtomicInteger(0);
      AtomicInteger prevYindex = new AtomicInteger(-1);
      galaxies.stream()
        .sorted(Comparator.comparingInt(Galaxy::getY))
        .forEachOrdered(g -> {
          yOffset.addAndGet(Math.max(0, g.getY() - prevYindex.get() - 1));
          prevYindex.set(g.getY());
          g.setY(g.getY() + yOffset.get());
        });
    }

    return "" + StreamEx.ofPairs(
        galaxies.stream().toList(),
        Galaxy::distance)
      .mapToInt(Integer::intValue)
      .sum();
  }

  private static class Galaxy {
    private int x, y;

    public Galaxy(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int distance(Galaxy other) {
      return Math.abs(other.getY() - getY()) + Math.abs(other.getX() - getX());
    }

    public Galaxy setX(int x) {
      this.x = x;
      return this;
    }

    public Galaxy setY(int y) {
      this.y = y;
      return this;
    }

    public int getX() {
      return x;
    }

    public int getY() {
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
