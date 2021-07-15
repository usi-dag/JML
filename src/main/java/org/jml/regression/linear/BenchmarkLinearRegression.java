package org.jml.regression.linear;

import org.jml.Operations;
import org.jml.dataset.LoadCSV;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class BenchmarkLinearRegression {

    @State(Scope.Benchmark)
    public static class MyState {
        public static final int N = 500;
        private final LoadCSV loader = new LoadCSV("weatherHistory.csv");
        private final Map<String, List<String>> records = loader.getRecords();
        private final long size = 10_000_000;



        private final double[] x = records.get("Temperature (C)").stream()
                .mapToDouble(Double::parseDouble)
                .toArray();

        private final double[] y = records.get("Humidity").stream()
                .mapToDouble(Double::parseDouble)
                .toArray();


        final double[] dx = Collections.nCopies(N, x)
                .stream()
                .flatMapToDouble(Arrays::stream)
                .limit(size)
                .toArray();

        final double[] dy = Collections.nCopies(N, y)
                .stream()
                .flatMapToDouble(Arrays::stream)
                .limit(size)
                .toArray();

        final long[] lx = Collections.nCopies(N, x).stream()
                .flatMapToDouble(Arrays::stream)
                .mapToLong(a -> (long) a)
                .limit(size)
                .toArray();

        final long[] ly = Collections.nCopies(N, y).stream()
                .flatMapToDouble(Arrays::stream)
                .mapToLong(a -> (long) a)
                .limit(size)
                .toArray();

        final int[] ix = Collections.nCopies(N, x).stream()
                .flatMapToDouble(Arrays::stream)
                .mapToInt(a -> (int) a)
                .limit(size)
                .toArray();

        final int[] iy = Collections.nCopies(N, y).stream()
                .flatMapToDouble(Arrays::stream)
                .mapToInt(a -> (int) a)
                .limit(size)
                .toArray();

        double[] left = Arrays.copyOfRange(dx, 0, dx.length/2);
        double[] right = Arrays.copyOfRange(dx, dx.length/2, dx.length);
    }


    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testDoubleLinearRegression(MyState state) {
        DoubleLinearRegression doubleLinearRegression = new DoubleLinearRegression();
        doubleLinearRegression.fit(state.dx, state.dy);
        return doubleLinearRegression.predict(0.5);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testDoubleVectorLinearRegression(MyState state) {
        DoubleVectorLinearRegression doubleVectorLinearRegression = new DoubleVectorLinearRegression();
        doubleVectorLinearRegression.fit(state.dx, state.dy);
        return doubleVectorLinearRegression.predict(0.5);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public long testLongLinearRegression(MyState state) {
        LongLinearRegression longLinearRegression = new LongLinearRegression();
        longLinearRegression.fit(state.lx, state.ly);
        return longLinearRegression.predict(1);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public long testLongVectorLinearRegression(MyState state) {
        LongVectorLinearRegression longVectorLinearRegression = new LongVectorLinearRegression();
        longVectorLinearRegression.fit(state.lx, state.ly);
        return longVectorLinearRegression.predict(1);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int testIntLinearRegression(MyState state) {
        IntegerLinearRegression integerLinearRegression = new IntegerLinearRegression();
        integerLinearRegression.fit(state.ix, state.iy);
        return integerLinearRegression.predict(ThreadLocalRandom.current().nextInt());
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int testIntVectorLinearRegression(MyState state) {
        IntegerVectorLinearRegression integerVectorLinearRegression = new IntegerVectorLinearRegression();
        integerVectorLinearRegression.fit(state.ix, state.iy);
        return integerVectorLinearRegression.predict(ThreadLocalRandom.current().nextInt());
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testReduceSpecialSingle(MyState state) {
        Operations operations = new Operations();
        return operations.reduceSpecial(state.dx);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testReduceSpecial(MyState state) {
        Operations operations = new Operations();
        return operations.reduceSpecial(state.right, state.left);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testReduceLaneSingle(MyState state) {
        Operations operations = new Operations();
        return operations.reduceLane(state.dx);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testReduceLane(MyState state) {
        Operations operations = new Operations();
        return operations.reduceLane(state.right, state.left);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testReduceSingle(MyState state) {
        Operations operations = new Operations();
        return operations.reduce(state.dx);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public double testReduce(MyState state) {
        Operations operations = new Operations();
        return operations.reduce(state.right, state.left);
    }


// capire perche int e double differenti velocita
    // capire se le istruzioni vector sono giuste
    // cercare varie implementzaioni in c (scipy, numpy)
}
