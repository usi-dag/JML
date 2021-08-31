package org.jml.classification.svm;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BenchmarkSvm {

    private static final int ITERATIONS = 1;
    static Random r = new Random();

    static class Data {
        private double[][] X;
        private double[] Y;

        Data(int n, int low, int high, double m, double q, int dimension) {
            linearBinaryClass(n, low, high, m, q, dimension);
        }

        public double[][] getX() {
            return X;
        }

        public double[] getY() {
            return Y;
        }

        private void linearBinaryClass(int n, int low, int high, double m, double q, int dimension) {
            X = new double[n][dimension];
            Y = new double[n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < dimension; j++) {
                    X[i][j] = r.nextInt(high - low) + low;
                }
            }

            for (int i = 0; i < n; i++) {
                if (X[i][1] - (X[i][0] * m + q) > 0) Y[i] = 1;
                else Y[i] = -1;
            }
        }
    }




    @State(Scope.Benchmark)
    public static class MyState {
        Data data = new Data(200, 0, 20, 0.8, 0, 2);
        public double[][] X = data.getX();
        public double[] Y = data.getY();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSvm(MyState state, Blackhole sink) {
        Svm svm = new Svm();
        for (int i = 0; i < ITERATIONS; i++) {
            svm.fit(state.X, state.Y);
            sink.consume(svm.predict(new double[]{3, 0.5}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKSvmVectorized(MyState state, Blackhole sink) {
        Svm svm = new Svm(true);
        for (int i = 0; i < ITERATIONS; i++) {
            svm.fit(state.X, state.Y);
            sink.consume(svm.predict(new double[]{3, 0.5}));
        }
    }
}
