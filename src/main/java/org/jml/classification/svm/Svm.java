package org.jml.classification.svm;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.lang.Math;
import java.util.Arrays;

public class Svm {

    static VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;
    static int DOUBLE_SPECIES_LENGTH = SPECIES.length();

    private double [] w;
    private double intercept = 0;
    private boolean vectorize;

    public Svm() {
        this.vectorize = false;
    }

    public Svm(boolean vectorize) {
        this.vectorize = vectorize;
    }

    private double dot(double[] a, double[] b) {


        int  i = 0;
        double result = 0;

        if (vectorize) {
            int upperBound = SPECIES.loopBound(a.length);
            var res = DoubleVector.zero(SPECIES);

            for (; this.vectorize && i < upperBound; i += DOUBLE_SPECIES_LENGTH) {
                res =  res.add(DoubleVector.fromArray(SPECIES, a, i).mul(DoubleVector.fromArray(SPECIES, b, i)));
            }

            result += res.reduceLanes(VectorOperators.ADD);
        }

        for (; i < a.length; i++) {
            result += a[i] * b[i];
        }

        return result;
    }


    /**
     *
     * @param x list of all data points to train
     * @param y list of the class label related to the x point
     */
    public void fit(double[][] x, double[] y, double regParm, int epochs) {
        final int size = x.length;
        final int dimension = x[0].length;
        // intialize vector species for the number of feature (x dimension)
        if (dimension < 3) {
            SPECIES = DoubleVector.SPECIES_128;
        } else if (dimension < 7) {
            SPECIES = DoubleVector.SPECIES_256;
        } else {
            SPECIES = DoubleVector.SPECIES_MAX;
        }
        DOUBLE_SPECIES_LENGTH = SPECIES.length();

        w = new double[dimension];

         // check that y contains only binary number 1 or -1
        for (double v : y) {
            if (Math.abs(v) != 1) throw new IllegalArgumentException("Target value for y must be either 1 or -1");
        }


        for (int epoch = 1; epoch < epochs; epoch++) {
            double learningRate = ((double) 1)/ ((double) epoch);
            for (int i = 0; i < size; i++) {
                if (y[i] * (dot(x[i], w)) < 1) {
                    int j = 0;
                    if (vectorize) {
                        int upperBound = SPECIES.loopBound(w.length);

                        for (; j < upperBound; j += DOUBLE_SPECIES_LENGTH) {
                            var wv = DoubleVector.fromArray(SPECIES, w, j);
                            var lrv = DoubleVector.broadcast(SPECIES, learningRate);
                            wv = (DoubleVector.broadcast(SPECIES, 1).sub(lrv))
                                    .mul(wv)
                                    .add(
                                            lrv
                                                    .mul(DoubleVector.broadcast(SPECIES, regParm))
                                                    .mul(DoubleVector.broadcast(SPECIES, y[i]))
                                                    .mul(DoubleVector.fromArray(SPECIES, x[i], j))
                                    )
                            ;

                            wv.intoArray(w, j);
                        }
                    }
                    for (; j < w.length; j++) {
                        w[j] = (1 - learningRate) * w[j] + learningRate * regParm * y[i] * x[i][j];
                    }
                } else {
                    int j = 0;
                    if (vectorize) {
                        int upperBound = SPECIES.loopBound(w.length);

                        for (; j < upperBound; j += DOUBLE_SPECIES_LENGTH) {
                            var wv = DoubleVector.fromArray(SPECIES, w, j);
                            var lrv = DoubleVector.broadcast(SPECIES, learningRate);
                            wv = (DoubleVector.broadcast(SPECIES, 1).sub(lrv)).mul(wv);
                            wv.intoArray(w, j);
                        }
                    }
                    for (; j < w.length; j++) {
                        w[j] = (1 - learningRate) * w[j] ;
                    }
                }
            }
        }

        /*double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < size; i++) {
            double distance = distance(w, x[i]);
            if (y[i] * (dot(x[i], w)) < 1) {
                if (y[i] == -1 && Math.abs(distance) > max) {
                    max = distance;
                } else if (y[i] == 1 && Math.abs(distance) < min) {
                    min = distance;
                }
            }
        }

        intercept = -((max+min)/2);*/
    }

//    private double distance(double[] a, double[] b) {
//        double dist = 0;
//        for (int i = 0; i < a.length; i++) {
//            dist += a[i] - b[i];
//        }
//
//        return dist;
//    }

    public void fit(double[][] x, double[] y) {
        fit(x, y, 40, 100000);
    }

    /**
     *
     * @param x the point to classify
     * @return the id of class which point belongs, predicted from the model (either 1 or -1)
     */
    public int predict(double[] x) {
//        System.out.println("Prediction = sgn(dot(x, w) - intercept) = " + "sgn(dot(" + Arrays.toString(x) + ", " + Arrays.toString(w) + ") - " + intercept + ") = sgn(" + dot(x, w) + " + " + intercept + ") = sgn(" + (dot(x, w) - intercept) + ") = " + Math.signum(dot(x, w) - intercept));
        return (int) Math.signum(dot(x, w) /*- intercept*/);
    }

    public double[] getWeights() {
        return w;
    }

    public double getIntercept() {
        return intercept;
    }


    public static void main(String[] args) {
        final int SIZE = 18;
        final int DIMENSION = 2;

        double[][] dataset = new double[SIZE][DIMENSION];
        dataset[0] =  new double[]{-5, -5};
        dataset[1] =  new double[]{-5, -6};
        dataset[2] =  new double[]{-5, -7};
        dataset[3] =  new double[]{-6, -5};
        dataset[4] =  new double[]{-6, -6};
        dataset[5] =  new double[]{-6, -7};
        dataset[6] =  new double[]{-7, -5};
        dataset[7] =  new double[]{-7, -6};
        dataset[8] =  new double[]{-7, -7};
        dataset[9] =  new double[]{5, 5};
        dataset[10] = new double[]{5, 6};
        dataset[11] = new double[]{5, 7};
        dataset[12] = new double[]{6, 5};
        dataset[13] = new double[]{6, 6};
        dataset[14] = new double[]{6, 7};
        dataset[15] = new double[]{7, 5};
        dataset[16] = new double[]{7, 6};
        dataset[17] = new double[]{7, 7};


//        dataset[0] =  new double[]{0, 0};
//        dataset[1] =  new double[]{0, 1};
//        dataset[2] =  new double[]{0, 2};
//        dataset[3] =  new double[]{1, 0};
//        dataset[4] =  new double[]{1, 1};
//        dataset[5] =  new double[]{1, 2};
//        dataset[6] =  new double[]{2, 0};
//        dataset[7] =  new double[]{2, 1};
//        dataset[8] =  new double[]{2, 2};
//        dataset[9] =  new double[]{5, 5};
//        dataset[10] = new double[]{5, 6};
//        dataset[11] = new double[]{5, 7};
//        dataset[12] = new double[]{6, 5};
//        dataset[13] = new double[]{6, 6};
//        dataset[14] = new double[]{6, 7};
//        dataset[15] = new double[]{7, 5};
//        dataset[16] = new double[]{7, 6};
//        dataset[17] = new double[]{7, 7};

        System.out.println(Arrays.deepToString(dataset));

        double[] y = new double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            if (i < 9) y[i] = -1;
            else y[i] = 1;
        }

        Svm svm = new Svm(true);

        svm.fit(dataset, y);
        double w[] = svm.getWeights();
        System.out.println("f(x) = a*x - b = " + (-w[0]/w[1]) + " + " + svm.getIntercept());
        System.out.println("i_: " + svm.getIntercept());
        System.out.println(Arrays.toString(svm.getWeights()));
        System.out.println(svm.predict(new double[]{-3,-7}));
        System.out.println(svm.predict(new double[]{8, 8}));

    }
}
