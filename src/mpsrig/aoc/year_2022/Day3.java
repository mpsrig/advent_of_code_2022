package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.ListUtils;
import mpsrig.java_utils.SetUtils;
import mpsrig.java_utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day3 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day3());
    }

    static class RucksackPart1 {
        public List<String> left;
        public List<String> right;

        public RucksackPart1(String line) {
            var elems = StringUtils.toCodePoints(line);
            int midpoint = elems.size()/2;
            if (midpoint * 2 != elems.size()) {
                throw new IllegalArgumentException("Invalid line length " + elems.size());
            }

            left = elems.subList(0, midpoint);
            right = elems.subList(midpoint, elems.size());
        }
    }

    static int getPriority(String x) {
        if (x.length() != 1) {
            throw new IllegalArgumentException("Not an ascii char: " + x);
        }
        char c = x.charAt(0);
        if (65 <= c && c <= 90) {
            return c - 38;
        }
        if (97 <= c && c <= 122) {
            return c - 96;
        }
        throw new IllegalArgumentException("Not an ascii char: " + c);
    }

    @Override
    public Object computePart1() {
        long result = 0;
        var rucksacks = ListUtils.map(input, RucksackPart1::new);
        for (var r : rucksacks) {
            var intersections = SetUtils.intersection(Arrays.asList(r.left, r.right));
            if (intersections.size() != 1) {
                System.err.println("Found multiple common items");
            }
            for (var elem : intersections) {
                result += getPriority(elem);
            }
        }
        return result;
    }

    @Override
    public Object computePart2() {
        List<List<List<String>>> groups = new ArrayList<>();
        List<List<String>> currentGroup = new ArrayList<>();
        int i = 0;
        for (var line : input) {
            currentGroup.add(StringUtils.toCodePoints(line));
            if ((i + 1) % 3 == 0) {
                groups.add(currentGroup);
                currentGroup = new ArrayList<>();
            }
            i++;
        }
        if (i != input.size()) {
            throw new IllegalStateException();
        }

        long result = 0;
        for (var g : groups) {
            var intersections = SetUtils.intersection(g);
            if (intersections.size() != 1) {
                System.err.println("Found multiple common items");
            }
            for (var elem : intersections) {
                result += getPriority(elem);
            }
        }
        return result;
    }
}
