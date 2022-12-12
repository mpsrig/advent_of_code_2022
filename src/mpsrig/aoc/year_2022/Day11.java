package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.ComputationWithGroups;
import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.ListUtils;

import java.util.*;
import java.util.regex.Pattern;

public class Day11 extends ComputationWithGroups {
    public static void main(String[] args) {
        Runner.run(null, new Day11());
    }

    interface Operation {
        long apply(long old);
    }

    static class MultiplyConstant implements Operation {
        final int c;

        MultiplyConstant(int c) {
            this.c = c;
        }

        @Override
        public long apply(long old) {
            return old * c;
        }

        @Override
        public String toString() {
            return "MultiplyConstant{" +
                    "c=" + c +
                    '}';
        }
    }

    static class AddConstant implements Operation {
        final int c;

        AddConstant(int c) {
            this.c = c;
        }

        @Override
        public long apply(long old) {
            return old + c;
        }

        @Override
        public String toString() {
            return "AddConstant{" +
                    "c=" + c +
                    '}';
        }
    }

    static class MultiplySelf implements Operation {

        @Override
        public long apply(long old) {
            return old * old;
        }

        @Override
        public String toString() {
            return "MultiplySelf{}";
        }
    }

    static class Monkey {

        static Pattern[] PATTERNS = new Pattern[] {
            Pattern.compile("Monkey (\\d+):"),
            Pattern.compile("  Starting items: (.*)"),
            Pattern.compile("  Operation: new = old (.) (.*)"),
            Pattern.compile("  Test: divisible by (\\d+)"),
            Pattern.compile("    If true: throw to monkey (\\d+)"),
            Pattern.compile("    If false: throw to monkey (\\d+)"),
        };

        int id;
        Queue<Long> items;
        Operation op;
        int divisibleBy;
        int trueTargetId;
        int falseTargetId;

        long counter = 0;

        public Monkey(List<String> lines) {
            if (lines.size() != PATTERNS.length) {
                throw new IllegalArgumentException("Wrong number of lines input");
            }
            for (int i = 0; i < PATTERNS.length; i++) {
                var m = PATTERNS[i].matcher(lines.get(i));
                if (!m.find()) {
                    throw new IllegalArgumentException("Could not match");
                }
                switch (i) {
                    case 0 -> {
                        id = Integer.parseInt(m.group(1));
                    }
                    case 1 -> {
                        items = new ArrayDeque<>(ListUtils.map(Arrays.asList(m.group(1).split(", ")), Long::parseLong));
                    }
                    case 2 -> {
                        if (m.group(1).equals("*") && m.group(2).equals("old")) {
                            op = new MultiplySelf();
                        } else if (m.group(1).equals("*")) {
                            op = new MultiplyConstant(Integer.parseInt(m.group(2)));
                        } else {
                            op = new AddConstant(Integer.parseInt(m.group(2)));
                        }
                    }
                    case 3 -> {
                        divisibleBy = Integer.parseInt(m.group(1));
                    }
                    case 4 -> {
                        trueTargetId = Integer.parseInt(m.group(1));
                    }
                    case 5 -> {
                        falseTargetId = Integer.parseInt(m.group(1));
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "id=" + id +
                    ", items=" + items +
                    ", op=" + op +
                    ", divisibleBy=" + divisibleBy +
                    ", trueTargetId=" + trueTargetId +
                    ", falseTargetId=" + falseTargetId +
                    '}';
        }

        void performTurn(List<Monkey> monkeys, boolean isPart1, int lcm) {
            while (!items.isEmpty()) {
                long item = items.poll();
                counter++;
                item = op.apply(item);
                if (isPart1) {
                    item /= 3;
                } else {
                    item %= lcm;
                }
                if (item % divisibleBy == 0) {
                    monkeys.get(trueTargetId).items.add(item);
                } else {
                    monkeys.get(falseTargetId).items.add(item);
                }
            }
        }
    }


    long compute(boolean isPart1) {
        var monkeys = ListUtils.map(groups, Monkey::new);
        int lcm = 1;
        for (var m : monkeys) {
            lcm *= m.divisibleBy;
        }
        int rounds = isPart1 ? 20 : 10000;

        for (int i = 0; i < rounds; i++) {
            for (var m: monkeys) {
                m.performTurn(monkeys, isPart1, lcm);
            }
        }

        var l = ListUtils.map(monkeys, monkey -> monkey.counter);
        Collections.sort(l);
        return l.get(l.size() - 2) * l.get(l.size() - 1);
    }

    @Override
    public Object computePart1() {
        return compute(true);
    }

    @Override
    public Object computePart2() {
        return compute(false);
    }
}
