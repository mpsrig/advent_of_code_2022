package mpsrig.aoc.year_2022;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mpsrig.aoc.framework.ComputationWithGroups;
import mpsrig.aoc.framework.Runner;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day13 extends ComputationWithGroups {
    public static void main(String[] args) {
        Runner.run("/year_2022/13.txt", new Day13());
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object computePart1() {
        int sumIdx = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (isCorrectOrder(groups.get(i))) {
                sumIdx += (i + 1); // 1 based indexing lol
            }
        }
        return sumIdx;
    }

    boolean isCorrectOrder(List<String> group) {
        if (group.size() != 2) {
            throw new IllegalArgumentException();
        }
        JsonNode left;
        JsonNode right;
        try {
            left = objectMapper.readTree(group.get(0));
            right = objectMapper.readTree(group.get(1));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        var result = isCorrectOrderImpl(left, right);
        return switch (result) {
            case ACCEPT -> true;
            case REJECT -> false;
            case SKIP -> throw new IllegalStateException();
        };
    }

    enum Result {
        SKIP,
        ACCEPT,
        REJECT,
    }

    Result isCorrectOrderImpl(JsonNode left, JsonNode right) {
        if (left.isInt() && right.isInt()) {
            var leftInt = left.asInt();
            var rightInt = right.asInt();
            if (leftInt < rightInt) {
                return Result.ACCEPT;
            }
            if (leftInt > rightInt) {
                return Result.REJECT;
            }
            return Result.SKIP;
        }
        if (left.isArray() && right.isArray()) {
            var leftItr = left.iterator();
            var rightItr = right.iterator();
            while (leftItr.hasNext() && rightItr.hasNext()) {
                var res = isCorrectOrderImpl(leftItr.next(), rightItr.next());
                if (res != Result.SKIP) {
                    return res;
                }
            }
            if (rightItr.hasNext()) {
                return Result.ACCEPT;
            }
            if (leftItr.hasNext()) {
                return Result.REJECT;
            }
            return Result.SKIP;
        }
        if (left.isArray() && right.isInt()) {
            var rightArray = objectMapper.createArrayNode();
            rightArray.add(right);
            return isCorrectOrderImpl(left, rightArray);
        }
        if (left.isInt() && right.isArray()) {
            var leftArray = objectMapper.createArrayNode();
            leftArray.add(left);
            return isCorrectOrderImpl(leftArray, right);
        }
        throw new IllegalStateException("Should not be reachable");
    }

    class TheComparator implements Comparator<JsonNode> {
        @Override
        public int compare(JsonNode left, JsonNode right) {
            var result = isCorrectOrderImpl(left, right);
            return switch (result) {
                case ACCEPT -> -1;
                case SKIP -> 0;
                case REJECT -> 1;
            };
        }
    }

    @Override
    public Object computePart2() {
        try {
            var allLines = new ArrayList<JsonNode>();
            var firstDivider = objectMapper.readTree("[[2]]");
            var secondDivider = objectMapper.readTree("[[6]]");

            allLines.add(firstDivider);
            allLines.add(secondDivider);

            for (var line : input) {
                if (line.isEmpty()) {
                    continue;
                }
                allLines.add(objectMapper.readTree(line));
            }

            allLines.sort(new TheComparator());

            int first = allLines.indexOf(firstDivider);
            int second = allLines.indexOf(secondDivider);
            return (first + 1) * (second + 1);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
