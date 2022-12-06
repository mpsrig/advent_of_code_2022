package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.StringUtils;

import java.util.HashSet;
import java.util.List;

public class Day6 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day6());
    }

    List<String> data;

    @Override
    protected void init() {
        data = StringUtils.toCodePoints(input.get(0));
    }

    private Integer impl(int len) {
        for (int i = len; i < data.size(); i++) {
            if ((new HashSet<>(data.subList(i-len, i))).size() == len) {
                return i;
            }
        }
        return null;
    }

    @Override
    public Object computePart1() {
        return impl(4);
    }

    @Override
    public Object computePart2() {
        return impl(14);
    }
}
