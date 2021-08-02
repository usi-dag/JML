package org.jml.regression.linear;

import org.jml.dataset.LoadCSV;
import org.jml.regression.linear.doubles.DoubleLinearRegression;
import org.jml.regression.linear.doubles.DoubleVectorLinearRegression;
import org.jml.regression.linear.integers.IntegerLinearRegression;
import org.jml.regression.linear.integers.IntegerVectorLinearRegression;
import org.jml.regression.linear.longs.LongLinearRegression;
import org.jml.regression.linear.longs.LongVectorLinearRegression;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

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
    }


    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testDoubleLinearRegression(MyState state, Blackhole sink) {
        DoubleLinearRegression doubleLinearRegression = new DoubleLinearRegression();
        for (int i = 0; i < 100; i++) {
            doubleLinearRegression.fit(state.dx, state.dy);
            sink.consume(doubleLinearRegression.predict(0.5));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testDoubleVectorLinearRegression(MyState state, Blackhole sink) {
        DoubleVectorLinearRegression doubleVectorLinearRegression = new DoubleVectorLinearRegression();
        for (int i = 0; i < 100; i++) {
            doubleVectorLinearRegression.fit(state.dx, state.dy);
            sink.consume(doubleVectorLinearRegression.predict(0.5));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testLongLinearRegression(MyState state, Blackhole sink) {
        LongLinearRegression longLinearRegression = new LongLinearRegression();
        for (int i = 0; i < 100; i++) {
            longLinearRegression.fit(state.lx, state.ly);
            sink.consume(longLinearRegression.predict(1));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testLongVectorLinearRegression(MyState state, Blackhole sink) {
        LongVectorLinearRegression longVectorLinearRegression = new LongVectorLinearRegression();
        for (int i = 0; i < 100; i++) {
            longVectorLinearRegression.fit(state.lx, state.ly);
            sink.consume(longVectorLinearRegression.predict(1));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testIntLinearRegression(MyState state, Blackhole sink) {
        IntegerLinearRegression integerLinearRegression = new IntegerLinearRegression();
        for (int i = 0; i < 100; i++) {
            integerLinearRegression.fit(state.ix, state.iy);
            sink.consume(integerLinearRegression.predict(ThreadLocalRandom.current().nextInt()));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testIntVectorLinearRegression(MyState state, Blackhole sink) {
        IntegerVectorLinearRegression integerVectorLinearRegression = new IntegerVectorLinearRegression();
        for (int i = 0; i < 100; i++) {
            integerVectorLinearRegression.fit(state.ix, state.iy);
            sink.consume(integerVectorLinearRegression.predict(ThreadLocalRandom.current().nextInt()));
        }
    }

}
