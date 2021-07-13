package org.jml.regression.linear;


import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jml.dataset.LoadCSV;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearRegressionTest {

    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    private static LinearRegression linearRegression;
    private static VectorLinearRegression vectorLinearRegression;

    @BeforeAll
    public static void setupModel() {
        linearRegression = new LinearRegression();
        vectorLinearRegression = new VectorLinearRegression();

    }

    @Test
    @DisplayName("Testing Linear Regression")
    void simpleLinearRegressionTest() {
//        LoadCSV csv = new LoadCSV("weatherHistory.csv");
//        Map<String, List<String>> records = csv.getRecords();
//
//        double[] x = records.get("Humidity").stream()
//                .mapToDouble(Double::parseDouble)
//                .toArray();
//
//        double[] y = records.get("Apparent Temperature (C)").stream()
//                .mapToDouble(Double::parseDouble)
//                .toArray();

        double[] x = {0,1,2,3,4,5,6,7,8,9,10};

        double[] y = {4,2,3,4,5,6,-7,4,8,6,4};

        linearRegression.fit(x, y);

        vectorLinearRegression.fit(x, y);

        System.out.println(linearRegression.predict(2));
        System.out.println(vectorLinearRegression.predict(2));

//        int i = 0;
//        int upperBound = SPECIES.loopBound(x.length);
//        double sum = 0;
//
//        for (; i < upperBound; i += SPECIES.length()) {
//            var xs = DoubleVector.fromArray(SPECIES, x, i);
//
//            sum += xs.reduceLanes(VectorOperators.ADD);
//            System.out.println(sum);
//
//        }
//
//        for (; i < x.length; i++) {
//            sum += x[i];
//            System.out.println(sum);
//        }


    }




}
