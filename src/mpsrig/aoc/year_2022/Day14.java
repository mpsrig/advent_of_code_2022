package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.ListUtils;

import java.util.*;

public class Day14 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day14());
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
            // Custom hashcode implementation here is
            // 4x faster than Objects.hash
            return 31 * x + y;
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    enum Material {
        ROCK,
        SAND,
    }

    static Coordinate fromPair(String pair) {
        var parts = pair.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException();
        }
        return new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    Map<Coordinate, Material> loadRock() {
        var out = new HashMap<Coordinate, Material>();
        for (var line : input) {
            var coordinates = ListUtils.map(Arrays.asList(line.split(" -> ")), Day14::fromPair);
            for (int i = 0; i < coordinates.size() - 1; i++) {
                for (var c : drawLine(coordinates.get(i), coordinates.get(i+1))) {
                    out.put(c, Material.ROCK);
                }
            }
        }
        return out;
    }

    static List<Coordinate> drawLine(Coordinate p1, Coordinate p2) {
        var out = new ArrayList<Coordinate>();
        if (p1.x == p2.x) {
            for (int y = Math.min(p1.y, p2.y); y <= Math.max(p1.y, p2.y); y++) {
                out.add(new Coordinate(p1.x, y));
            }
        } else if (p1.y == p2.y) {
            for (int x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); x++) {
                out.add(new Coordinate(x, p1.y));
            }
        } else {
            throw new IllegalArgumentException();
        }
        return out;
    }

    @Override
    public Object computePart1() {
        var grid = loadRock();
        final int maxY = grid.keySet().stream().mapToInt(c -> c.y).max().orElseThrow();
        if (grid.containsKey(new Coordinate(500, 0))) {
            throw new IllegalStateException();
        }
        int counter = 0;
        while (true) {
            try {
                addSandPart1(grid, maxY);
            } catch (SandOverflowException e) {
                return counter;
            }
            counter++;
        }
    }

    static class SandOverflowException extends Exception {}

    static void addSandPart1(Map<Coordinate, Material> grid, final int maxY) throws SandOverflowException {
        int sandX = 500;
        int sandY = 0;
        while (sandY <= maxY) {
            if (!grid.containsKey(new Coordinate(sandX, sandY + 1))) {
                sandY++;
                continue;
            }
            if (!grid.containsKey(new Coordinate(sandX - 1, sandY + 1))) {
                sandX--;
                sandY++;
                continue;
            }
            if (!grid.containsKey(new Coordinate(sandX + 1, sandY + 1))) {
                sandX++;
                sandY++;
                continue;
            }
            // Nowhere else to move
            grid.put(new Coordinate(sandX, sandY), Material.SAND);
            return;
        }
        throw new SandOverflowException();
    }

    @Override
    public Object computePart2() {
        var grid = loadRock();
        final int floorY = grid.keySet().stream().mapToInt(c -> c.y).max().orElseThrow() + 2;
        var entry = new Coordinate(500, 0);
        int counter = 0;
        while (!grid.containsKey(entry)) {
           addSandPart2(grid, floorY);
           counter++;
        }
        return counter;
    }

    static void addSandPart2(Map<Coordinate, Material> grid, final int floorY) {
        int sandX = 500;
        int sandY = 0;
        while (true) {
            if (sandY + 1 > floorY) {
                throw new IllegalStateException();
            }
            if (sandY + 1 != floorY) {
                if (!grid.containsKey(new Coordinate(sandX, sandY + 1))) {
                    sandY++;
                    continue;
                }
                if (!grid.containsKey(new Coordinate(sandX - 1, sandY + 1))) {
                    sandX--;
                    sandY++;
                    continue;
                }
                if (!grid.containsKey(new Coordinate(sandX + 1, sandY + 1))) {
                    sandX++;
                    sandY++;
                    continue;
                }
            }
            // Nowhere else to move
            grid.put(new Coordinate(sandX, sandY), Material.SAND);
            return;
        }
    }
}
