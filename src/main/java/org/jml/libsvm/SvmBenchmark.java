package org.jml.libsvm;


import libsvm.*;
import org.jml.dataset.LoadCSV;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SvmBenchmark {

    private static final int ITERATIONS = 1;

    static class Data {
        private double[][] X;
        private double[] Y;
        private static svm_parameter param = new svm_parameter();
        private static svm_problem prob = new svm_problem();

        Data() {
            param.svm_type = svm_parameter.C_SVC;
            param.kernel_type = svm_parameter.LINEAR;
            param.degree = 3;
            param.gamma = 0;	// 1/num_features
            param.coef0 = 0;
            param.nu = 0.5;
            param.cache_size = 100;
            param.C = 1;
            param.eps = 1e-3;
            param.p = 0.1;
            param.shrinking = 1;
            param.probability = 0;
            param.nr_weight = 0;
            param.weight_label = new int[0];
            param.weight = new double[0];

            LoadCSV loader = new LoadCSV("winequality-red.csv");

            Map<String, List<String>> records = loader.getRecords();

            List<String> acidity = records.get("fixed acidity");
            List<String> volatile_acidity = records.get("volatile acidity");
            List<String> citric_acidity = records.get("citric acid");
            List<String> residual_sugar = records.get("residual sugar");
            List<String> chlorides = records.get("chlorides");
            List<String> free_sulfur_dioxide = records.get("free sulfur dioxide");
            List<String> total_sulfur_dioxide = records.get("total sulfur dioxide");
            List<String> density = records.get("density");
            List<String> pH = records.get("pH");
            List<String> sulphates = records.get("sulphates");
            List<String> alcohol = records.get("alcohol");
            List<String> quality = records.get("quality");

            int size = acidity.size();
            X = new double[size][11];

            List<List<String>> tmp = new ArrayList<>();
            tmp.add(acidity);
            tmp.add(volatile_acidity);
            tmp.add(citric_acidity);
            tmp.add(residual_sugar);
            tmp.add(chlorides);
            tmp.add(free_sulfur_dioxide);
            tmp.add(total_sulfur_dioxide);
            tmp.add(density);
            tmp.add(pH);
            tmp.add(sulphates);
            tmp.add(alcohol);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 11; j++) {
//                    X[i][j] = new svm_node();
//                    X[i][j].index = j;
//                    X[i][j].value = Double.parseDouble(tmp.get(j).get(i));
                    X[i][j] =  Double.parseDouble(tmp.get(j).get(i));
                }
            }

            prob.l = size;
            prob.x = X;
            prob.y = quality.stream().mapToDouble(Double::parseDouble).toArray();

            if(param.gamma == 0 && size > 0)
                param.gamma = 1.0/size;
        }

        public static svm_problem getProb() {
            return prob;
        }

        public static svm_parameter getParam() {
            return param;
        }

        public double[][] getX() {
            return X;
        }

        public double[] getY() {
            return Y;
        }
    }

    public static void main(String[] args) {

        Data data = new Data();
        svm_parameter param = data.getParam();
        svm_problem prob = data.getProb();
        svm svm_class = new svm();
        svm_model model = svm_class.svm_train(prob, param);
    }




    @State(Scope.Benchmark)
    public static class MyState {
        Data data = new Data();
        public svm_parameter param = data.getParam();
        public svm_problem prob = data.getProb();

        public double[] pred = createPred();

        double[] createPred() {
            double[] x = new double[11];
            for (int i = 0; i < 11; i++) {
                x[i] = i - 0.5;
            }

            return x;
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testLibSvm(MyState state, Blackhole sink) {
        svm svm_class = new svm();
        svm_model model;
        for (int i = 0; i < ITERATIONS; i++) {
            model = svm_class.svm_train(state.prob, state.param);
            System.out.println(svm_class.svm_predict(model, state.pred));
            sink.consume(svm_class.svm_predict(model, state.pred));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testLibSvmVector(MyState state, Blackhole sink) {
        svm svm_class = new svm(true, true);
        svm_model model;
        for (int i = 0; i < ITERATIONS; i++) {
            model = svm_class.svm_train(state.prob, state.param);
            System.out.println(svm_class.svm_predict(model, state.pred));
            sink.consume(svm_class.svm_predict(model,  state.pred));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testLibSvmVectorNoMask(MyState state, Blackhole sink) {
        svm svm_class = new svm(true, false);
        svm_model model;
        for (int i = 0; i < ITERATIONS; i++) {
            model = svm_class.svm_train(state.prob, state.param);
            System.out.println(svm_class.svm_predict(model, state.pred));
            sink.consume(svm_class.svm_predict(model, state.pred));
        }
    }
}
