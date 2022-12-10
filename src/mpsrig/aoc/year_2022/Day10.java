package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;

import java.util.ArrayList;
import java.util.List;

public class Day10 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day10());
    }

    List<Integer> cycles = new ArrayList<>();

    @Override
    public Object computePart1() {
        int reg = 1;
        cycles.add(reg); // index 0
        for (var line : input) {
            if (line.startsWith("noop")) {
                cycles.add(reg);
                continue;
            }
            int op = Integer.parseInt(line.substring("addx ".length()));
            cycles.add(reg);
            cycles.add(reg);
            reg += op;
        }

        long sum = 0;
        for (int i = 20; i <= 220; i+=40) {
            var x = i * cycles.get(i);
            sum += x;
        }

        return sum;
    }

    @Override
    public Object computePart2() {
        var sb = new StringBuilder();
        for (int i = 0; i < cycles.size() - 1; i++) {
            int val = cycles.get(i+1);

            int horizontalPos = i % 40;
            if (horizontalPos == 0) {
                sb.append('\n');
            }
            if (Math.abs(val - horizontalPos) < 2) {
                sb.append('#');
            } else {
                sb.append('.');
            }
        }

        return sb.toString();
    }
}
