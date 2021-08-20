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
    static int LIMIT = 1_000_000;
    static int ITERATIONS = 10;
    @State(Scope.Benchmark)
    public static class MyState {
        static LoadCSV loader = new LoadCSV("weatherHistory.csv");

        static Map<String, List<String>> records = loader.getRecords();

        static List<String> temperature = records.get("Temperature (C)");
        static List<String> humidity = records.get("Humidity");
        static List<String> windSpeed = records.get("Wind Speed (km/h)");
        static List<String> windBearing = records.get("Wind Bearing (degrees)");
        static List<String> loudCover = records.get("Loud Cover");
        static List<String> visibility = records.get("Visibility (km)");
        static List<String> pressure = records.get("Pressure (millibars)");
        static int SIZE = temperature.size();

        public double[][] dataset2 = dimension2();
        public double[][] dataset4 = dimension4();
        public double[][] dataset6 = dimension6();
        public double[][] dataset7 = dimension7();


        static double[][] dimension2() {
            double[][] dataset = new double[LIMIT][2];
            for (int i = 0; i < LIMIT; i++) {
                dataset[i] = new double[]{Double.parseDouble(temperature.get(i%SIZE)), Double.parseDouble(humidity.get(i%SIZE))};
            }

            return dataset;
        }

        static double[][] dimension4() {
            double[][] dataset = new double[LIMIT][4];
            for (int i = 0; i < LIMIT; i++) {
                dataset[i] = new double[]{
                        Double.parseDouble(temperature.get(i%SIZE)),
                        Double.parseDouble(humidity.get(i%SIZE)),
                        Double.parseDouble(windSpeed.get(i%SIZE)),
                        Double.parseDouble(windBearing.get(i%SIZE))
                };
            }

            return dataset;
        }

        static double[][] dimension6() {
            double[][] dataset = new double[LIMIT][6];
            for (int i = 0; i < LIMIT; i++) {
                dataset[i] = new double[]{
                        Double.parseDouble(temperature.get(i%SIZE)),
                        Double.parseDouble(humidity.get(i%SIZE)),
                        Double.parseDouble(windSpeed.get(i%SIZE)),
                        Double.parseDouble(windBearing.get(i%SIZE)),
                        Double.parseDouble(loudCover.get(i%SIZE)),
                        Double.parseDouble(visibility.get(i%SIZE))
                };
            }

            return dataset;
        }

        static double[][] dimension7() {
            double[][] dataset = new double[LIMIT][7];
            for (int i = 0; i < LIMIT; i++) {
                dataset[i] = new double[]{
                        Double.parseDouble(temperature.get(i%SIZE)),
                        Double.parseDouble(humidity.get(i%SIZE)),
                        Double.parseDouble(windSpeed.get(i%SIZE)),
                        Double.parseDouble(windBearing.get(i%SIZE)),
                        Double.parseDouble(loudCover.get(i%SIZE)),
                        Double.parseDouble(visibility.get(i%SIZE)),
                        Double.parseDouble(pressure.get(i%SIZE))
                };
            }

            return dataset;
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension2(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset2, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension2(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset2, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension4(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension4(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension6(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset6, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280, 15, 0}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension6(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset6, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280, 15, 0}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension7(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset7, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280, 15, 0, 1000}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension7(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset7, 4);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280, 15, 0, 1000}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension4_2Cluster(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 2);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension4_2Cluster(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 2);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension4_6Cluster(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 6);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension4_6Cluster(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 6);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension4_8Cluster(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 8);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension4_8Cluster(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 8);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansDimension4_10Cluster(MyState state, Blackhole sink) {
        KMeans kMeans = new KMeans();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 10);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testKMeansVectorDimension4_10Cluster(MyState state, Blackhole sink) {
        KMeansVector kMeans = new KMeansVector();
        for (int i = 0; i < ITERATIONS; i++) {
            kMeans.fit(state.dataset4, 10);
            sink.consume(kMeans.predict(new double[]{3, 0.5, 15, 280}));
        }
    }
}
