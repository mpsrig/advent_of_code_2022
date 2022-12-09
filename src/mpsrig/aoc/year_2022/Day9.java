package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class Day9 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day9());
    }

    static class Coordinate {
        final int x;
        final int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        Coordinate moveHead(String direction) {
            return switch(direction) {
                case "L" -> new Coordinate(x - 1, y);
                case "R" -> new Coordinate(x + 1, y);
                case "U" -> new Coordinate(x , y + 1);
                case "D" -> new Coordinate(x , y - 1);
                default -> throw new IllegalArgumentException("Unexpected value: " + direction);
            };
        }

        Coordinate moveToward(Coordinate head) {
            int xDist = head.x - x;
            int yDist = head.y - y;

            if (Math.abs(xDist) > 1 || Math.abs(yDist) > 1) {
                return new Coordinate(x + Integer.signum(xDist), y + Integer.signum(yDist));
            }
            return this;
        }
    }

    private int compute(int length) {
        var tailLocations = new HashSet<Coordinate>();

        var knots = new Coordinate[length];
        Arrays.fill(knots, new Coordinate(0, 0));

        tailLocations.add(knots[knots.length - 1]);

        for (var line : input) {
            var parts = line.split(" ");
            if (parts.length != 2) {
                throw new IllegalArgumentException();
            }
            int magnitude = Integer.parseInt(parts[1]);
            if (magnitude < 1) {
                throw new IllegalStateException();
            }
            for (int i = 0; i < magnitude; i++) {
                knots[0] = knots[0].moveHead(parts[0]);
                for (int j = 1; j < knots.length; j++) {
                    knots[j] = knots[j].moveToward(knots[j-1]);
                }
                tailLocations.add(knots[knots.length - 1]);
            }
        }

        return tailLocations.size();
    }

    @Override
    public Object computePart1() {
        return compute(2);
    }

    @Override
    public Object computePart2() {
        return compute(10);
    }
}
