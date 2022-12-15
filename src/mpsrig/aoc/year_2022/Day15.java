package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;
import mpsrig.java_utils.ListUtils;

import java.util.List;
import java.util.regex.Pattern;

public class Day15 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run("/year_2022/15.txt", new Day15());
    }

    static int calculateDistance(Day14.Coordinate a, Day14.Coordinate b) {
        return Math.abs(b.x - a.x) + Math.abs(b.y - a.y);
    }

    static class SensorClosestBeacon {
        final Day14.Coordinate sensor;
        final Day14.Coordinate beacon;
        final int distance;

        static final Pattern PATTERN = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

        SensorClosestBeacon(String line) {
            var m = PATTERN.matcher(line);
            if (!m.find()) {
                throw new IllegalArgumentException();
            }
            sensor = new Day14.Coordinate(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            beacon = new Day14.Coordinate(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
            distance = calculateDistance(sensor, beacon);
        }

        @Override
        public String toString() {
            return "SensorClosestBeacon{" +
                    "sensor=" + sensor +
                    ", beacon=" + beacon +
                    '}';
        }

        boolean isKnownToNotHaveBeacon(Day14.Coordinate other) {
            if (other.equals(beacon)) {
                return false;
            }
            return calculateDistance(sensor, other) <= distance;
        }

        Integer isKnownToNotHaveDistressBeaconPart2(Day14.Coordinate other) {
            int yDistance = Math.abs(other.y - sensor.y);
            int allowableXDistance = distance - yDistance;
            int xDistance = Math.abs(other.x - sensor.x);
            if (xDistance <= allowableXDistance) {
                if (!isKnownToNotHaveBeacon(other) && !other.equals(beacon)) {
                    throw new IllegalStateException();
                }
                return sensor.x + allowableXDistance;
            }
            return null;
        }
    }

    List<SensorClosestBeacon> info;

    @Override
    protected void init() {
        super.init();
        info = ListUtils.map(input, SensorClosestBeacon::new);
    }

    boolean isKnownToNotHaveBeacon(Day14.Coordinate other) {
        for (var elem : info) {
            if (elem.isKnownToNotHaveBeacon(other)) {
                return true;
            }
        }
        return false;
    }

    Integer isKnownToNotHaveDistressBeaconPart2(Day14.Coordinate other) {
        for (var elem : info) {
            var check = elem.isKnownToNotHaveDistressBeaconPart2(other);
            if (check != null) {
                return check;
            }
        }
        return null;
    }

    int minX() {
        int min = Integer.MAX_VALUE;
        for (var elem : info) {
            min = Math.min(min, elem.sensor.x);
            min = Math.min(min, elem.beacon.x);
        }
        return min;
    }

    int maxX() {
        int max = Integer.MIN_VALUE;
        for (var elem : info) {
            max = Math.max(max, elem.sensor.x);
            max = Math.max(max, elem.beacon.x);
        }
        return max;
    }

    int maxDistance() {
        int max = Integer.MIN_VALUE;
        for (var elem : info) {
            max = Math.max(max, elem.distance);
        }
        return max;
    }

    @Override
    public Object computePart1() {
        int minX = minX() - maxDistance();
        int maxX = maxX() + maxDistance();

        int counter = 0;
        for (int x = minX; x <= maxX; x++) {
            if (isKnownToNotHaveBeacon(new Day14.Coordinate(x, 2000000))) {
                counter++;
            }
        }
        return counter;
    }

    Day14.Coordinate findDistressBeacon() {
        for (int y = 0; y <= 4000000; y++) {
            for (int x = 0; x <= 4000000; x++) {
                var c = new Day14.Coordinate(x, y);
                var check = isKnownToNotHaveDistressBeaconPart2(c);
                if (check == null) {
                    return c;
                }
                if (check < x) {
                    throw new IllegalStateException();
                }
                x = check;
            }
        }
        throw new IllegalStateException("Not found");
    }

    @Override
    public Object computePart2() {
        var c = findDistressBeacon();
        if (isKnownToNotHaveBeacon(c)) {
            throw new IllegalStateException("Wrong answer");
        }
        return (4000000L * c.x) + c.y;
    }
}
