package me.smarion.aoc2023.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternIterator implements Iterator<PatternIterator.Match>, Iterable<PatternIterator.Match> {
    private final Matcher internalMatcher;
    private Boolean findCache = null;

    public PatternIterator(Pattern p, String input) {
        internalMatcher = p.matcher(input);
    }

    @Override
    public boolean hasNext() {
        return Objects.requireNonNullElseGet(findCache, () -> findCache = internalMatcher.find());
    }

    @Override
    public Match next() {
        findCache = null;
        return new Match(internalMatcher.start(), internalMatcher.end(), internalMatcher.group());
    }

    @Override
    public Iterator<Match> iterator() {
        return this;
    }

    public record Match(int start, int end, String value) {
    }

    ;
}
