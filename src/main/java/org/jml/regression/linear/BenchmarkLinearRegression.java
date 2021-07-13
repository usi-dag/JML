package org.jml.regression.linear;

import org.jml.dataset.LoadCSV;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class BenchmarkLinearRegression {

    @State(Scope.Benchmark)
    public static class MyState {
//        private LoadCSV loader = new LoadCSV("weatherHistory.csv");
//        private Map<String, List<String>> records = loader.getRecords();

        private long size = 10_000_000;
        public double[] dx = DoubleStream
                .generate(ThreadLocalRandom.current()::nextDouble)
                .limit(size)
                .toArray();;
        public double[] dy = DoubleStream
                .generate(ThreadLocalRandom.current()::nextDouble)
                .limit(size)
                .toArray();

        public long[] lx = LongStream
                .generate(ThreadLocalRandom.current()::nextLong)
                .limit(size)
                .toArray();;
        public long[] ly = LongStream
                .generate(ThreadLocalRandom.current()::nextLong)
                .limit(size)
                .toArray();

        public int[] ix = IntStream
                .generate(ThreadLocalRandom.current()::nextInt)
                .limit(size)
                .toArray();;
        public int[] iy = IntStream
                .generate(ThreadLocalRandom.current()::nextInt)
                .limit(size)
                .toArray();

        public LinearRegression linearRegression = new LinearRegression();
        public VectorLinearRegression vectorLinearRegression = new VectorLinearRegression();

        public LongLinearRegression longLinearRegression = new LongLinearRegression();
        public LongVectorLinearRegression longVectorLinearRegression = new LongVectorLinearRegression();

        public IntegerLinearRegression integerLinearRegression = new IntegerLinearRegression();
        public IntegerVectorLinearRegression integerVectorLinearRegression = new IntegerVectorLinearRegression();
    }

      @Benchmark
      @OutputTimeUnit(TimeUnit.MILLISECONDS)
      public void testLinearRegression(MyState state) {
              state.linearRegression.fit(state.dx, state.dy);
      }

      @Benchmark
      @OutputTimeUnit(TimeUnit.MILLISECONDS)
      public void testVectorLinearRegression(MyState state) {
              state.vectorLinearRegression.fit(state.dx, state.dy);
      }
//
//    @Benchmark
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    @Fork(value = 1, warmups = 2)
//    @BenchmarkMode(Mode.AverageTime)
//    public void testLongLinearRegression(MyState state) {
//        state.longLinearRegression.fit(state.lx, state.ly);
//    }
//
//    @Benchmark
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    @Fork(value = 1, warmups = 2)
//    @BenchmarkMode(Mode.AverageTime)
//    public void testLongVectorLinearRegression(MyState state) {
//        state.longVectorLinearRegression.fit(state.lx, state.ly);
//    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public int testIntLinearRegression(MyState state) {
        IntegerLinearRegression integerLinearRegression = new IntegerLinearRegression();
        integerLinearRegression.fit(state.ix, state.iy);
        return integerLinearRegression.predict(ThreadLocalRandom.current().nextInt());
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public int testIntVectorLinearRegression(MyState state) {
        IntegerVectorLinearRegression integerVectorLinearRegression = new IntegerVectorLinearRegression();
        integerVectorLinearRegression.fit(state.ix, state.iy);
        return integerVectorLinearRegression.predict(ThreadLocalRandom.current().nextInt());
    }

// capire perche int e double differenti velocita
    // capire se le istruzioni vector sono giuste
    // cercare varie implementzaioni in c (scipy, numpy)

}
