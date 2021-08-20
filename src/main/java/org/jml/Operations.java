package org.jml;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class Operations {

    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_MAX;
    static final int SPECIES_LENGTH = SPECIES.length();

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


    public double reduce(double[] x) {
        double sumx = 0;

        for(int i = 0; i < x.length; i++) {
            sumx += x[i];

        }
        return sumx;
    }

    public double reduce(double[] x, double[] y) {
        double sumx = 0;
        double sumy = 0;

        for(int i = 0; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        return sumx + sumy;
    }

    public double reduceSpecial(double[] x) {

        double sum = 0;

        int i = 0;
        double upperBound = SPECIES.loopBound(x.length);
        DoubleVector sumxV = DoubleVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            sumxV = sumxV.add(DoubleVector.fromArray(SPECIES, x, i));
        }

        sum += sumxV.reduceLanes(VectorOperators.ADD);

        for (; i < x.length; i++) {
            sum += x[i];
        }

//        double[] tmp = sumxV.toDoubleArray();
//
//        for (i = 0; i < tmp.length; i++ ) {
//          sum += tmp[i];
//        }

        return sum;
    }

    public double reduceLane(double[] x) {
        double sum = 0;

        int i = 0;
        double upperBound = SPECIES.loopBound(x.length);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            sum = DoubleVector.fromArray(SPECIES, x, i).reduceLanes(VectorOperators.ADD);
        }

        for (; i < x.length; i++) {
            sum += x[i];
        }

        return sum;
    }

    public double reduceSpecial(double[] x, double[] y) {

        double sumx = 0;
        double sumy = 0;

        int i = 0;
        double upperBound = SPECIES.loopBound(x.length);
        DoubleVector sumxV = DoubleVector.zero(SPECIES);
        DoubleVector sumxY = DoubleVector.zero(SPECIES);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            sumxV = sumxV.add(DoubleVector.fromArray(SPECIES, x, i));
            sumxY = sumxY.add(DoubleVector.fromArray(SPECIES, y, i));
        }

        sumx += sumxV.reduceLanes(VectorOperators.ADD);

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        return sumx + sumy;
    }

    public double reduceLane(double[] x, double[] y) {
        double sumx = 0;
        double sumy = 0;

        int i = 0;
        double upperBound = SPECIES.loopBound(x.length);
        for (; i < upperBound; i += SPECIES_LENGTH) {
            sumx = DoubleVector.fromArray(SPECIES, x, i).reduceLanes(VectorOperators.ADD);
            sumy = DoubleVector.fromArray(SPECIES, y, i).reduceLanes(VectorOperators.ADD);
        }

        for (; i < x.length; i++) {
            sumx += x[i];
            sumy += y[i];

        }

        return sumx + sumy;
    }
}

