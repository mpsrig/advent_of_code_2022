package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.InputUtils;
import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.ListUtils;

import java.util.*;
import java.util.regex.Pattern;

public class Day5 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day5());
    }

    int emptyLine = 0;
    List<Instruction> instructions;

    static class Instruction {
        static Pattern PATTERN = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

        final int quantity;
        final int source;
        final int destination;

        Instruction(String line) {
            var m = PATTERN.matcher(line);
            if (!m.find()) {
                throw new IllegalStateException();
            }
            quantity = Integer.parseInt(m.group(1));
            source = Integer.parseInt(m.group(2));
            destination = Integer.parseInt(m.group(3));
        }
    }

    @Override
    public void init() {
        int i = 0;
        for (var line : input) {
            if (line.isEmpty()) {
                break;
            }
            i++;
        }
        emptyLine = i;

        instructions = ListUtils.map(input.subList(emptyLine + 1, input.size()), Instruction::new);
    }

    private Map<Integer, ArrayDeque<Character>> buildStacks() {
        Map<Integer, ArrayDeque<Character>> stacks = new HashMap<>();
        var ids = InputUtils.parseInts(Arrays.asList(input.get(emptyLine-1).trim().split("\s+")));
        for (var id : ids) {
            var stack = new ArrayDeque<Character>();
            for (int i = emptyLine - 2; i > -1; i--) {
                var line = input.get(i);
                char item = line.charAt((id - 1) * 4 + 1);
                if (item != ' '){
                    stack.push(item);
                }
            }
            stacks.put(id, stack);
        }
        return stacks;
    }

    private static int maxKey(Map<Integer, ArrayDeque<Character>> stacks) {
        return stacks.keySet().stream().mapToInt(x -> x).max().orElseThrow();
    }

    private static void printStacks(Map<Integer, ArrayDeque<Character>> stacks) {
        int mk = maxKey(stacks);
        int height = stacks.values().stream().mapToInt(Collection::size).max().orElseThrow();

        List<List<Character>> temp = new ArrayList<>(mk);
        for (int i = 1; i <= mk; i++) {
            temp.add(new ArrayList<>(stacks.get(i)));
        }

        List<String> outputLines = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            List<String> line = new ArrayList<>(mk);
            for (var stack : temp) {
                var elemIdx = stack.size() - 1 - i;
                if (elemIdx < 0) {
                    line.add("   ");
                } else {
                    line.add("[" + stack.get(elemIdx) + "]");
                }
            }
            outputLines.add(String.join(" ", line));
        }

        var sb = new StringBuilder();
        for (int i = outputLines.size() -1; i > -1; i--) {
            sb.append(outputLines.get(i));
            sb.append("\n");
        }
        sb.append(" ");
        for (int i = 1; i <= mk; i++) {
            sb.append(i);
            sb.append("   ");
        }

        System.err.println(sb);
    }

    private String impl(boolean isPart2) {
        var stacks = buildStacks();

        for (var inst : instructions) {
            if (isPart2) {
                processInstructionPart2(stacks, inst);
            } else {
                processInstructionPart1(stacks, inst);
            }
        }

//        printStacks(stacks);

        int mk = maxKey(stacks);
        var sb = new StringBuilder();
        for (int i = 1; i <= mk; i++) {
            sb.append(stacks.get(i).peek());
        }
        return sb.toString();
    }

    void processInstructionPart1(Map<Integer, ArrayDeque<Character>> stacks, Instruction instruction) {
        for (int i = 0; i < instruction.quantity; i++) {
            var elem = stacks.get(instruction.source).pop();
            stacks.get(instruction.destination).push(elem);
        }
    }

    void processInstructionPart2(Map<Integer, ArrayDeque<Character>> stacks, Instruction instruction) {
        var buffer = new ArrayDeque<Character>(instruction.quantity);
        for (int i = 0; i < instruction.quantity; i++) {
            buffer.push(stacks.get(instruction.source).pop());
        }
        while (!buffer.isEmpty()) {
            stacks.get(instruction.destination).push(buffer.pop());
        }
    }

    @Override
    public Object computePart1() {
        return impl(false);
    }

    @Override
    public Object computePart2() {
        return impl(true);
    }
}
