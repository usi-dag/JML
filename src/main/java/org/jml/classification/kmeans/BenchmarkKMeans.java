package org.jml.classification.kmeans;

import org.jml.dataset.LoadCSV;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;


import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class BenchmarkKMeans {

    @State(Scope.Benchmark)
    public static class MyState {
        static LoadCSV loader = new LoadCSV("weatherHistory.csv");

        static Map<String, List<String>> records = loader.getRecords();

        static List<String> temperature = records.get("Temperature (C)");
        static List<String> humidity = records.get("Humidity");

        public double[][] dataset = points();

        static double[][] points() {
            double[][] dataset = new double[temperature.size()][2];
            for (int i = 0; i < temperature.size(); i++) {
                dataset[i] = new double[]{Double.parseDouble(temperature.get(i)), Double.parseDouble(humidity.get(i))};
            }

            return dataset;
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeans(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < 100; i++) {
            kMeans.fit(state.dataset, 2);
            sink.consume(kMeans.predict(new double[]{3, 0.5}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVector(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < 100; i++) {
            kMeans.fit(state.dataset, 2);
            sink.consume(kMeans.predict(new double[]{3, 0.5}));
        }
    }

}
