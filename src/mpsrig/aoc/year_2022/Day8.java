package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;

public class Day8 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day8());
    }

    int[][] grid;

    @Override
    protected void init() {
        grid = new int[input.size()][];
        for (int y = 0; y < grid.length; y++) {
            grid[y] = input.get(y).codePoints().map(elem -> Integer.parseInt(Character.toString(elem))).toArray();
        }
    }

    boolean isVisible(int y, int x, int deltaY, int deltaX) {
        int tree = grid[y][x];

        if (deltaY == 0) {
            for (x += deltaX; x > -1 && x < grid[y].length; x += deltaX) {
                if (grid[y][x] >= tree) {
                    return false;
                }
            }
        } else if (deltaX == 0) {
            for (y += deltaY; y > -1 && y < grid.length; y += deltaY) {
                if (grid[y][x] >= tree) {
                    return false;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }

        return true;
    }

    boolean isVisible(int y, int x) {
        return isVisible(y, x, 1, 0) ||
                isVisible(y, x, -1, 0) ||
                isVisible(y, x, 0, 1) ||
                isVisible(y, x, 0, -1);
    }

    int scenicScore(int y, int x, int deltaY, int deltaX) {
        int score = 0;

        int tree = grid[y][x];

        if (deltaY == 0) {
            for (x += deltaX; x > -1 && x < grid[y].length; x += deltaX) {
                score++;
                if (grid[y][x] >= tree) {
                    return score;
                }
            }
        } else if (deltaX == 0) {
            for (y += deltaY; y > -1 && y < grid.length; y += deltaY) {
                score++;
                if (grid[y][x] >= tree) {
                    return score;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }

        return score;
    }

    int scenicScore(int y, int x) {
        return scenicScore(y, x, 1, 0) *
                scenicScore(y, x, -1, 0) *
                scenicScore(y, x, 0, 1) *
                scenicScore(y, x, 0, -1);
    }


    @Override
    public Object computePart1() {
        long count = 0;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (isVisible(y, x)) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    public Object computePart2() {
        int maxScore = 0;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int score = scenicScore(y, x);
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }

        return maxScore;
    }
}
