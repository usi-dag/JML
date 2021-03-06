package org.jml.regression.linear.integers;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class IntegerVectorLinearRegression {

    private int intercept;
    private int slope;
    static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;
    static final int SPECIES_LENGTH = SPECIES.length();

    public void fit(int[] x, int[] y) {
        if (x.length != y.length) throw new IllegalArgumentException("Arrays length are not equals");


        int sumx = 0, sumy = 0;

        int i = 0; // number of elements for vector
        int upperBound = SPECIES.loopBound(x.length); // defines the upperbound of array length in which a vector transformation can be applied
        IntVector sumxV = IntVector.zero(SPECIES);
        IntVector sumyV = IntVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            sumxV = sumxV.add(IntVector.fromArray(SPECIES, x, i));
            sumyV = sumyV.add(IntVector.fromArray(SPECIES, y, i));
        }

        sumx += sumxV.reduceLanes(VectorOperators.ADD);
        sumy += sumyV.reduceLanes(VectorOperators.ADD);

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        int xbar = sumx / x.length;
        int ybar = sumy / x.length;

        int xxbar = 0;
        int xybar = 0;

        i = 0;
        IntVector xxbarV = IntVector.zero(SPECIES);
        IntVector xybarV = IntVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            var xs = IntVector.fromArray(SPECIES, x, i).sub(xbar);
            xxbarV = xxbarV.add(IntVector.fromArray(SPECIES, y, i).sub(ybar).mul(xs));
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
        return "VectorLinearRegression{" +
                "intercept=" + intercept +
                ", slope=" + slope +
                '}';
    }
}
