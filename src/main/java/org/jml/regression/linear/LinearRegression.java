package org.jml.regression.linear;


public class LinearRegression {
    private double intercept, slope;


    public void fit(double[] x, double[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");

        double sumx = 0.0, sumy = 0.0;

        for (int i = 0; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        double xbar = sumx / x.length;
        double ybar = sumy / x.length;


        double xxbar = 0.0, xybar = 0.0;
        for (int i = 0; i < x.length; i++) {
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
        return "LinearRegression{" +
                "intercept=" + intercept +
                ", slope=" + slope +
                '}';
    }
}
