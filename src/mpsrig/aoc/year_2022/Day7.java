package mpsrig.aoc.year_2022;

import mpsrig.aoc.framework.Runner;

import java.util.*;

public class Day7 extends Runner.Computation {
    public static void main(String[] args) {
        Runner.run(null, new Day7());
    }

    static class Directory {
        final Directory parent;
        final Map<String, Directory> subdirectories = new HashMap<>();
        final Map<String, Long> files = new HashMap<>();

        public Directory(Directory parent) {
            this.parent = parent;
        }

        private Long sizeCache = null;

        long getSize() {
            if (sizeCache != null) {
                return sizeCache;
            }
            long sum = 0;
            for (var elem : files.values()) {
                sum += elem;
            }
            for (var elem : subdirectories.values()) {
                sum += elem.getSize();
            }
            sizeCache = sum;
            return sum;
        }

        void collectSizes(List<Long> l) {
            for (var elem : subdirectories.values()) {
                elem.collectSizes(l);
            }
            l.add(getSize());
        }
    }

    Directory root;
    List<Long> allSizes;

    @Override
    public void init() {
        root = new Directory(null);
        Directory currentDirectory = null;
        for (int i = 0; i < input.size(); i++) {
            final var elem = input.get(i);
            if (elem.startsWith("$ cd ")) {
                var arg = elem.substring("$ cd ".length());
                if (arg.equals("/")) {
                    currentDirectory = root;
                    continue;
                }
                assert currentDirectory != null;
                if (arg.equals("..")) {
                    currentDirectory = currentDirectory.parent;
                    continue;
                }
                var nextDirectory = currentDirectory.subdirectories.get(arg);
                if (nextDirectory == null) {
                    throw new IllegalStateException("cd to unknown directory");
                }
                currentDirectory = nextDirectory;
                continue;
            }
            assert currentDirectory != null;
            if (elem.equals("$ ls")) {
                i++;
                for (; i < input.size() && !input.get(i).startsWith("$"); i++) {
                    var listing = input.get(i).split(" ");
                    if (listing.length != 2) {
                        throw new IllegalStateException("Could not parse ls output");
                    }
                    if (listing[0].equals("dir")) {
                        if (currentDirectory.subdirectories.containsKey(listing[1])) {
                            throw new IllegalStateException("Already saw directory");
                        }
                        currentDirectory.subdirectories.put(listing[1], new Directory(currentDirectory));
                    } else {
                        currentDirectory.files.put(listing[1], Long.parseLong(listing[0]));
                    }
                }
                i--;
                continue;
            }
            throw new IllegalStateException("Unknown line at " + i + " : " + elem);
        }

        allSizes = new ArrayList<>();
        root.collectSizes(allSizes);
        Collections.sort(allSizes);
    }

    @Override
    public Object computePart1() {
        long sum = 0;
        for (var elem : allSizes) {
            if (elem <= 100000) {
                sum += elem;
            }
        }
        return sum;
    }

    @Override
    public Object computePart2() {
        var spaceNeeded = root.getSize() - 40000000;
        var idx = Collections.binarySearch(allSizes, spaceNeeded);
        if (idx < 0) {
            // Derive the "insertion point" which is the
            // position of the first element greater than the argument
            idx = (-idx) - 1;
        }
        return allSizes.get(idx);
    }
}
