package org.jml.regression.linear;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class VectorLinearRegression {

    private double intercept;
    private double slope;
    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    public void fit(double[] x, double[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");


        double sumx = 0.0, sumy = 0.0;

        int i = 0; // number of elements for vector
        int upperBound = SPECIES.loopBound(x.length); // defines the upperbound of array length in which a vector transformation can be applied
        for (; i < upperBound; i += SPECIES.length()) {
            var xs = DoubleVector.fromArray(SPECIES, x, i);
            var ys = DoubleVector.fromArray(SPECIES, y, i);
            sumx += xs.reduceLanes(VectorOperators.ADD);
            sumy += ys.reduceLanes(VectorOperators.ADD);
        }

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        double xbar = sumx / x.length;
        double ybar = sumy / x.length;

        double xxbar = 0.0;
        double xybar = 0.0;

        i = 0;
        for (; i < upperBound; i += SPECIES.length()) {
            var xs = DoubleVector.fromArray(SPECIES, x, i);
            var ys = DoubleVector.fromArray(SPECIES, y, i);
            xs = xs.sub(xbar);
            ys = ys.sub(ybar).mul(xs);
            xs = xs.mul(xs);
            xxbar = xs.reduceLanes(VectorOperators.ADD);
            xybar = ys.reduceLanes(VectorOperators.ADD);
        }

        for (; i < x.length; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }

        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;
    }

    public double getIntercept() {
        return intercept;
    }

    public double getSlope() {
        return slope;
    }

    public double predict(double x) {
        return slope*x + intercept;
    }

    @Override
    public String toString() {
        return "VectorLinearRegression{" +
                "intercept=" + intercept +
                ", slope=" + slope +
                '}';
    }
}
