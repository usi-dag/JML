package org.jml;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.List;

public class Operations {

    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    public static void main(String[] args) {
        System.out.println("PREFERRED: " + DoubleVector.SPECIES_PREFERRED);
        System.out.println("128: " + DoubleVector.SPECIES_128);
        System.out.println("256: " + DoubleVector.SPECIES_256);
        System.out.println("512: " + DoubleVector.SPECIES_512);
        System.out.println("MAX: " + DoubleVector.SPECIES_MAX);

        System.out.println("PREFERRED: " + LongVector.SPECIES_PREFERRED);
        System.out.println("128: " + LongVector.SPECIES_128);
        System.out.println("256: " + LongVector.SPECIES_256);
        System.out.println("512: " + LongVector.SPECIES_512);
        System.out.println("MAX: " + LongVector.SPECIES_MAX);

    }


    public double[] sumVectorArrays(double[] x, double[] y) {
        double[] result = new double[x.length];
        // number of elements for vector
        int upperBound = SPECIES.loopBound(x.length); // defines the upperbound of array length in which a vector transformation can be applied
        int i = 0;
        for (; i < upperBound; i += SPECIES.length()) {
            DoubleVector xs = DoubleVector.fromArray(SPECIES, x, i);
            DoubleVector ys = DoubleVector.fromArray(SPECIES, y, i);

            xs.add(ys).intoArray(result, i);
        }

        for (; i < x.length; i++) {
            result[i] = x[i] + x[i];
        }

        return result;
    }

    public double[] sumArrays(double[] x, double[] y) {
        double[] result = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = x[i] + x[i];
        }

        return result;

    }
}
