package mpsrig.aoc.framework;

import mpsrig.java_utils.ResourceUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Runner {
    public static abstract class Computation {
        protected List<String> input;

        private void setInput(List<String> input) {
            this.input = input;
        }

        protected void init() {}

        public abstract Object computePart1();
        public abstract Object computePart2();
    }

    public static void run(String res, Computation computation) {
        if (res == null) {
            var klassName = computation.getClass().getName();
            int packageEnd = klassName.lastIndexOf('.');
            int packageStart = klassName.lastIndexOf('.', packageEnd - 1);
            res = "/" + klassName.substring(packageStart + 1, packageEnd) + "/" + klassName.substring(packageEnd + 4) + ".txt";
            System.out.println("No resource path specified, using " + res);
        }

        long start = System.currentTimeMillis();
        var input = ResourceUtils.getLinesFromResource(res);
        computation.setInput(Collections.unmodifiableList(input));
        long beforeInit = System.currentTimeMillis();
        computation.init();
        long afterInit = System.currentTimeMillis();
        System.out.println("Part 1: " + computation.computePart1());
        long afterPart1 = System.currentTimeMillis();
        System.out.println("Part 2: " + computation.computePart2());
        long end = System.currentTimeMillis();
        System.out.println("Run took " + (end - start) +
                "ms. Loading input: " + (beforeInit - start)  +
                " ms, Init: " + (afterInit - beforeInit) +
                " ms, Part 1: " + (afterPart1 - afterInit) +
                " ms, Part 2: " + (end - afterPart1) + " ms.");
    }
}
