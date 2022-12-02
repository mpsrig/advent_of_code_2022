package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.ComputationWithGroups;
import mpsrig.aoc.framework.Runner;

import java.util.Arrays;

public class Day1 extends ComputationWithGroups {
    public static void main(String[] args) {
        Runner.run(null, new Day1());
    }

    @Override
    public Object computePart1() {
        return groups.stream().mapToInt(x -> x.stream().mapToInt(Integer::parseInt).sum()).max();
    }

    @Override
    public Object computePart2() {
        var sums= groups.stream().mapToInt(x -> x.stream().mapToInt(Integer::parseInt).sum()).sorted().toArray();
        return sums[sums.length-3] + sums[sums.length-2] + sums[sums.length-1];
    }
}
