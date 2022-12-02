package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.ListUtils;

import java.util.HashMap;
import java.util.Map;

public class Day2 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day2());
    }

    enum Shape {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        final int score;

        Shape(int score) {
            this.score = score;
        }
    }

    enum Result {
        LOSS(0),
        DRAW(3),
        WIN(6);

        final int score;

        Result(int score) {
            this.score = score;
        }
    }

    static Map<Character, Shape> opponentLUT = new HashMap<>();
    static {
        opponentLUT.put('A', Shape.ROCK);
        opponentLUT.put('B', Shape.PAPER);
        opponentLUT.put('C', Shape.SCISSORS);
    }

    static class Round {
        Shape opponent;
        Shape response;

        static Map<Character, Shape> responseLUT = new HashMap<>();
        static {
            responseLUT.put('X', Shape.ROCK);
            responseLUT.put('Y', Shape.PAPER);
            responseLUT.put('Z', Shape.SCISSORS);
        }

        Round(String line) {
            opponent = opponentLUT.get(line.charAt(0));
            response = responseLUT.get(line.charAt(2));
        }

        Result getResult() {
            return switch(response) {
                case ROCK -> switch(opponent) {
                    case ROCK -> Result.DRAW;
                    case PAPER -> Result.LOSS;
                    case SCISSORS -> Result.WIN;
                };
                case PAPER -> switch (opponent) {
                    case ROCK -> Result.WIN;
                    case PAPER -> Result.DRAW;
                    case SCISSORS -> Result.LOSS;
                };
                case SCISSORS -> switch (opponent) {
                    case ROCK -> Result.LOSS;
                    case PAPER -> Result.WIN;
                    case SCISSORS -> Result.DRAW;
                };
            };
        }

        int getScore() {
            var result = getResult();
            return result.score + response.score;
        }
    }

    static class Problem {
        Shape opponent;
        Result result;

        static Map<Character, Result> resultLUT = new HashMap<>();
        static {
            resultLUT.put('X', Result.LOSS);
            resultLUT.put('Y', Result.DRAW);
            resultLUT.put('Z', Result.WIN);
        }

        Problem(String line) {
            opponent = opponentLUT.get(line.charAt(0));
            result = resultLUT.get(line.charAt(2));
        }

        Shape getResponse() {
            return switch(result) {
                case LOSS -> switch(opponent) {
                    case ROCK -> Shape.SCISSORS;
                    case PAPER -> Shape.ROCK;
                    case SCISSORS -> Shape.PAPER;
                };
                case DRAW -> opponent;
                case WIN -> switch (opponent) {
                    case ROCK -> Shape.PAPER;
                    case PAPER -> Shape.SCISSORS;
                    case SCISSORS -> Shape.ROCK;
                };
            };
        }

        int getScore() {
            return getResponse().score + result.score;
        }
    }


    @Override
    public Object computePart1() {
        return ListUtils.map(input, Round::new).stream().mapToInt(Round::getScore).sum();
    }

    @Override
    public Object computePart2() {
        return ListUtils.map(input, Problem::new).stream().mapToInt(Problem::getScore).sum();
    }
}
