package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;

import java.util.*;

public class Day12 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run("/year_2022/12.txt", new Day12());
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
    }

    static class Vertex implements Comparable<Vertex> {
        final Coordinate coordinate;
        boolean visited = false;
        long distance = Long.MAX_VALUE;

        Vertex(Coordinate coordinate) {
            this.coordinate = coordinate;
        }

        @Override
        public int compareTo(Vertex o) {
            return Long.compare(this.distance, o.distance);
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "coordinate=" + coordinate +
                    ", visited=" + visited +
                    ", distance=" + distance +
                    '}';
        }
    }

    char[][] grid;
    Coordinate start;
    Coordinate end;

    @Override
    protected void init() {
        super.init();

        grid = new char[input.size()][];
        for (int y = 0; y < grid.length; y++) {
            grid[y] = input.get(y).toCharArray();
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 'S') {
                    start = new Coordinate(x, y);
                } else if (grid[y][x] == 'E') {
                    end = new Coordinate(x, y);
                }
            }
        }
    }

    Map<Coordinate, Vertex> makeGraph() {
        var out = new HashMap<Coordinate, Vertex>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                var c = new Coordinate(x, y);
                out.put(c, new Vertex(c));
            }
        }
        return out;
    }

    void performDijkstra(Map<Coordinate, Vertex> graph, Coordinate source) {
        var sourceV = graph.get(source);
        sourceV.distance = 0;

        var queue = new PriorityQueue<Vertex>();
        queue.add(sourceV);

        while (!queue.isEmpty()) {
            var u = queue.poll();
            for (var neighbor : getReachableNeighbors(u.coordinate)) {
                var neighborV = graph.get(neighbor);
                if (neighborV.visited) {
                    continue;
                }
                var neighborDist = u.distance + 1;
                if (neighborDist < neighborV.distance) {
                    queue.remove(neighborV);
                    neighborV.distance = neighborDist;
                    queue.add(neighborV);
                }
            }
            u.visited = true;
        }
    }

    List<Coordinate> getReachableNeighbors(Coordinate coord) {
        var out = new ArrayList<Coordinate>();
        char a = normalize(grid[coord.y][coord.x]);
        if (reachable(coord.x, coord.y + 1, a)) {
            out.add(new Coordinate(coord.x, coord.y + 1));
        }
        if (reachable(coord.x, coord.y - 1, a)) {
            out.add(new Coordinate(coord.x, coord.y - 1));
        }
        if (reachable(coord.x - 1, coord.y, a)) {
            out.add(new Coordinate(coord.x - 1, coord.y));
        }
        if (reachable(coord.x + 1, coord.y, a)) {
            out.add(new Coordinate(coord.x + 1, coord.y));
        }
        return out;
    }

    boolean reachable(int x, int y, char currentWeight) {
        if (y < 0 || y >= grid.length) {
            return false;
        }
        if (x < 0 || x >= grid[y].length) {
            return false;
        }
        char weight = normalize(grid[y][x]);
        return (weight - 1) <= currentWeight;
    }

    static char normalize(char weight) {
        if (weight == 'S') {
            return 'a';
        }
        if (weight == 'E') {
            return 'z';
        }
        return weight;
    }

    @Override
    public Object computePart1() {
        var graph = makeGraph();
        performDijkstra(graph, start);
        return graph.get(end).distance;
    }

    @Override
    public Object computePart2() {
        long min = Long.MAX_VALUE;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (normalize(grid[y][x]) == 'a') {
                    var graph = makeGraph();
                    performDijkstra(graph, new Coordinate(x, y));
                    var distance = graph.get(end).distance;
                    if (distance < min) {
                        min = distance;
                    }
                }
            }
        }
        return min;
    }
}
