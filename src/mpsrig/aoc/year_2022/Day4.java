package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.SetUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Day4 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day4());
    }

    static class CleaningPair {
        static Pattern PATTERN = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");

        Set<Integer> sectionsLeft;
        Set<Integer> sectionsRight;

        private static Set<Integer> setFromRangeInclusive(String low, String high) {
            var out = new HashSet<Integer>();
            for (int i = Integer.parseInt(low); i <= Integer.parseInt(high); i++) {
                out.add(i);
            }
            return out;
        }

        public CleaningPair(String line) {
            var match = PATTERN.matcher(line);
            if (!match.find()) {
                throw new IllegalStateException();
            }
            sectionsLeft = setFromRangeInclusive(match.group(1), match.group(2));
            sectionsRight = setFromRangeInclusive(match.group(3), match.group(4));
        }
    }

    @Override
    public Object computePart1() {
        int count = 0;
        for (var elem : input) {
            var pair = new CleaningPair(elem);
            if (pair.sectionsLeft.containsAll(pair.sectionsRight) || pair.sectionsRight.containsAll(pair.sectionsLeft)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Object computePart2() {
        int count = 0;
        for (var elem : input) {
            var pair = new CleaningPair(elem);
            if (!SetUtils.intersection(Arrays.asList(pair.sectionsLeft, pair.sectionsRight)).isEmpty()) {
                count++;
            }
        }
        return count;
    }
}
