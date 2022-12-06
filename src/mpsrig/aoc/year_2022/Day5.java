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
    List<String> ids;
    List<Instruction> instructions;

    static class Instruction {
        static Pattern PATTERN = Pattern.compile("move (\\d+) from (.*) to (.*)");

        final int quantity;
        final String source;
        final String destination;

        Instruction(String line) {
            var m = PATTERN.matcher(line);
            if (!m.find()) {
                throw new IllegalStateException();
            }
            quantity = Integer.parseInt(m.group(1));
            source = m.group(2);
            destination = m.group(3);
        }
    }

    @Override
    public void init() {
        emptyLine = input.indexOf("");
        ids = Arrays.asList(input.get(emptyLine-1).trim().split("\s+"));
        instructions = ListUtils.map(input.subList(emptyLine + 1, input.size()), Instruction::new);
    }

    private Map<String, ArrayDeque<Character>> buildStacks() {
        Map<String, ArrayDeque<Character>> stacks = new HashMap<>();
        int j = 0;
        for (var id : ids) {
            var stack = new ArrayDeque<Character>();
            for (int i = emptyLine - 2; i > -1; i--) {
                var line = input.get(i);
                char item = line.charAt(j * 4 + 1);
                if (item != ' '){
                    stack.push(item);
                }
            }
            stacks.put(id, stack);
            j++;
        }

        var serialized = serializeStacks(stacks);
        var selection = input.subList(0, emptyLine);
        if (!serialized.equals(selection)) {
            throw new IllegalStateException("Serialized stack does not match original input");
        }

        return stacks;
    }

    private List<String> serializeStacks(Map<String, ArrayDeque<Character>> stacks) {
        int height = stacks.values().stream().mapToInt(Collection::size).max().orElseThrow();

        List<List<Character>> temp = new ArrayList<>(ids.size());
        for (var id : ids) {
            temp.add(new ArrayList<>(stacks.get(id)));
        }

        String[] outputLines = new String[height + 1];
        for (int i = 0; i < height; i++) {
            List<String> line = new ArrayList<>(ids.size());
            for (var stack : temp) {
                var elemIdx = stack.size() - 1 - i;
                if (elemIdx < 0) {
                    line.add("   ");
                } else {
                    line.add("[" + stack.get(elemIdx) + "]");
                }
            }
            outputLines[height - 1 - i] = String.join(" ", line);
        }

        var sj = new StringJoiner("   ", " ", " ");
        for (var id : ids) {
            sj.add(String.valueOf(id));
        }
        outputLines[outputLines.length - 1] = sj.toString();

        return Arrays.asList(outputLines);
    }

    private void printStacks(Map<String, ArrayDeque<Character>> stacks) {
        for (var line : serializeStacks(stacks)) {
            System.err.println(line);
        }
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

        var sb = new StringBuilder();
        for (var id : ids) {
            sb.append(stacks.get(id).peek());
        }
        return sb.toString();
    }

    void processInstructionPart1(Map<String, ArrayDeque<Character>> stacks, Instruction instruction) {
        for (int i = 0; i < instruction.quantity; i++) {
            var elem = stacks.get(instruction.source).pop();
            stacks.get(instruction.destination).push(elem);
        }
    }

    void processInstructionPart2(Map<String, ArrayDeque<Character>> stacks, Instruction instruction) {
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
