package org.jml.regression.linear;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

public class VectorLinearRegression {

    private double intercept, slope;
    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    public void fit(double[] x, double[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");



        int i = SPECIES.length(); // number of elements for vector
        int upperBound = SPECIES.loopBound(x.length); // defines the upperbound of array length in which a vector transformation can be applied

        if (upperBound == 0) { // array is to small to achieve optimal vector operation -> go with normal linear regression
            LinearRegression linearRegression = new LinearRegression();
            linearRegression.fit(x, y);
            slope = linearRegression.getSlope();
            intercept = linearRegression.getIntercept();
            return;
        }

        double sumx = 0.0, sumy = 0.0;

        DoubleVector xs = DoubleVector.fromArray(SPECIES, x, 0);
        DoubleVector ys = DoubleVector.fromArray(SPECIES, y, 0);
        // iteratively sum all elemnts vector by vector to mimic a 'reduce' function
        for (; i < upperBound; i += SPECIES.length()) {
            var xss = DoubleVector.fromArray(SPECIES, x, i);
            var yss = DoubleVector.fromArray(SPECIES, y, i);
            xs = xs.add(xss);
            ys = ys.add(yss);
        }

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        double[] xr = xs.toDoubleArray();
        double[] yr = ys.toDoubleArray();


        for (int j = 0; j < xr.length; j++) {
            sumx += xr[j];
            sumy += yr[j];
        }

        double xbar = sumx / x.length;
        double ybar = sumy / x.length;


        double xxbar = 0.0, xybar = 0.0;
        i = 0;
        double[] x2 = new double[x.length];
        double[] y2 = new double[x.length];

        for (; i < upperBound; i += SPECIES.length()) {
            var xss = DoubleVector.fromArray(SPECIES, x, i);
            var yss = DoubleVector.fromArray(SPECIES, y, i);
            xss = xss.sub(xbar);
            yss = yss.sub(ybar).mul(xss);
            xss = xss.pow(2);
            yss.intoArray(y2, i);
            xss.intoArray(x2, i);
        }

        for (; i < x.length; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }


        i = SPECIES.length();
        xs = DoubleVector.fromArray(SPECIES, x2, 0);
        ys = DoubleVector.fromArray(SPECIES, y2, 0);

        for (; i < upperBound; i += SPECIES.length()) {
            var xss = DoubleVector.fromArray(SPECIES, x2, i);
            var yss = DoubleVector.fromArray(SPECIES, y2, i);
            xs = xs.add(xss);
            ys = ys.add(yss);
        }


        xr = xs.toDoubleArray();
        yr = ys.toDoubleArray();

        for (int j = 0; j < xr.length; j++) {
            xxbar += xr[j];
            xybar += yr[j];
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
