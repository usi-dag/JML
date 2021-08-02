package org.jml.regression.linear.doubles;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class DoubleVectorLinearRegression {

    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;
    static final int SPECIES_LENGTH = SPECIES.length();
    private double intercept;
    private double slope;

    public void fit(double[] x, double[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");


        double sumx = 0, sumy = 0;

        int i = 0; // number of elements for vector
        double upperBound = SPECIES.loopBound(x.length); // defines the upperbound of array length in which a vector transformation can be applied
        DoubleVector sumxV = DoubleVector.zero(SPECIES);
        DoubleVector sumyV = DoubleVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            sumxV = sumxV.add(DoubleVector.fromArray(SPECIES, x, i));
            sumyV = sumyV.add(DoubleVector.fromArray(SPECIES, y, i));
        }

        sumx += sumxV.reduceLanes(VectorOperators.ADD);
        sumy += sumyV.reduceLanes(VectorOperators.ADD);

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        double xbar = sumx / x.length;
        double ybar = sumy / x.length;

        double xxbar = 0;
        double xybar = 0;

        i = 0;
        DoubleVector xxbarV = DoubleVector.zero(SPECIES);
        DoubleVector xybarV = DoubleVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            var xs = DoubleVector.fromArray(SPECIES, x, i).sub(xbar);
            xxbarV = xxbarV.add(DoubleVector.fromArray(SPECIES, y, i).sub(ybar).mul(xs));
            xybarV = xybarV.add(xs.mul(xs));
        }

        xxbar += sumxV.reduceLanes(VectorOperators.ADD);
        xybar += sumyV.reduceLanes(VectorOperators.ADD);

        for (; i < x.length; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }

        slope = xybar / xxbar;
        intercept = ybar - slope * xbar;
    }


    public double getIntercept() {
        return intercept;
    }

    public double getSlope() {
        return slope;
    }

    public double predict(double x) {
        return slope * x + intercept;
    }

    @Override
    public String toString() {
        return "VectorLinearRegression{" +
                "intercept=" + intercept +
                ", slope=" + slope +
                '}';
    }
}
