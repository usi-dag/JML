package org.jml.regression.linear.longs;


public class LongLinearRegression {
    private long intercept, slope;


    public void fit(long[] x, long[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");

        long sumx = 0, sumy = 0;

        for (int i = 0; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        long xbar = sumx / x.length;
        long ybar = sumy / x.length;


        long xxbar = 0, xybar = 0;
        for (int i = 0; i < x.length; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }


        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;
    }

    public long getIntercept() {
        return intercept;
    }

    public long getSlope() {
        return slope;
    }

    public long predict(long x) {
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
