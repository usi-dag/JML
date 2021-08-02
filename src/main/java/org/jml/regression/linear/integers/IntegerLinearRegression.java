package org.jml.regression.linear.integers;

// -XX:UseAVX=0
//-XX:+TraceLoopOtps
// -XX:+PrintAssembly
public class IntegerLinearRegression {
    private int intercept, slope;


    public void fit(int[] x, int[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");

        int sumx = 0, sumy = 0;

        for (int i = 0; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        int xbar = sumx / x.length;
        int ybar = sumy / x.length;


        int xxbar = 0, xybar = 0;
        for (int i = 0; i < x.length; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }


        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;
    }

    public int getIntercept() {
        return intercept;
    }

    public int getSlope() {
        return slope;
    }

    public int predict(int x) {
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
