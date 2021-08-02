package org.jml.regression.linear.longs;

import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class LongVectorLinearRegression {

    private long intercept;
    private long slope;
    static final VectorSpecies<Long> SPECIES = LongVector.SPECIES_PREFERRED;
    static final int SPECIES_LENGTH = SPECIES.length();

    public void fit(long[] x, long[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");


        long sumx = 0, sumy = 0;

        int i = 0; // number of elements for vector
        int upperBound = SPECIES.loopBound(x.length); // defines the upperbound of array length in which a vector transformation can be applied
        LongVector sumxV = LongVector.zero(SPECIES);
        LongVector sumyV = LongVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            sumxV = sumxV.add(LongVector.fromArray(SPECIES, x, i));
            sumyV = sumyV.add(LongVector.fromArray(SPECIES, y, i));
        }

        sumx += sumxV.reduceLanes(VectorOperators.ADD);
        sumy += sumyV.reduceLanes(VectorOperators.ADD);

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        long xbar = sumx / x.length;
        long ybar = sumy / x.length;

        long xxbar = 0;
        long xybar = 0;

        i = 0;
        LongVector xxbarV = LongVector.zero(SPECIES);
        LongVector xybarV = LongVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            var xs = LongVector.fromArray(SPECIES, x, i).sub(xbar);
            xxbarV = xxbarV.add(LongVector.fromArray(SPECIES, y, i).sub(ybar).mul(xs));
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
