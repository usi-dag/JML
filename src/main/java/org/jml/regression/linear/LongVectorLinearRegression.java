package org.jml.regression.linear;

import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class LongVectorLinearRegression {

    private long intercept;
    private long slope;
    static final VectorSpecies<Long> SPECIES = LongVector.SPECIES_PREFERRED;

    public void fit(long[] x, long[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");


        long sumx = 0, sumy = 0;

        int i = 0;
        int upperBound = SPECIES.loopBound(x.length); // defines the upperbound of array length in which a vector transformation can be applied
        for (; i < upperBound; i += SPECIES.length()) {
            var xs = LongVector.fromArray(SPECIES, x, i);
            var ys = LongVector.fromArray(SPECIES, y, i);
            sumx += xs.reduceLanes(VectorOperators.ADD);
            sumy += ys.reduceLanes(VectorOperators.ADD);
        }

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        long xbar = sumx / x.length;
        long ybar = sumy / x.length;

        long xxbar = 0;
        long xybar = 0;

        i = 0;
        for (; i < upperBound; i += SPECIES.length()) {             // (x[i] - xbar) * (y[i] - xbar)
            var xs = LongVector.fromArray(SPECIES, x, i);
            var ys = LongVector.fromArray(SPECIES, y, i);
            xs = xs.sub(xbar);
            ys = ys.sub(ybar).mul(xs);
//             ys = xs.mul(ys.sub(ybar));
            xs = xs.mul(xs);
            xxbar += xs.reduceLanes(VectorOperators.ADD);
            xybar += ys.reduceLanes(VectorOperators.ADD);
        }

        for (; i < x.length; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }

        slope = xybar / xxbar;
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
        return "VectorLinearRegression{" +
                "intercept=" + intercept +
                ", slope=" + slope +
                '}';
    }
}
