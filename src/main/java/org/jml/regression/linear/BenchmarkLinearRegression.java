package org.jml.regression.linear;

import org.jml.dataset.LoadCSV;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BenchmarkLinearRegression {

//    @State(Scope.Thread)
    public static class MyState {
        private LoadCSV loader = new LoadCSV("weatherHistory.csv");
        private Map<String, List<String>> records = loader.getRecords();

        public double[] x = records.get("Humidity").stream()
                .mapToDouble(Double::parseDouble).toArray();

        public double[] y = records.get("Apparent Temperature (C)").stream()
                .mapToDouble(Double::parseDouble)
                .toArray();

        public LinearRegression linearRegression = new LinearRegression();
        public VectorLinearRegression vectorLinearRegression = new VectorLinearRegression();
    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
    public void testLinearRegression(MyState state) {
        state.linearRegression.fit(state.x, state.y);
    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
    public void testVectorLinearRegression(MyState state) {
        state.vectorLinearRegression.fit(state.x, state.y);
    }


}
