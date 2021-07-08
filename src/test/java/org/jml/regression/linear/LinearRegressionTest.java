package org.jml.regression.linear;


import org.jml.dataset.LoadCSV;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearRegressionTest {

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
        LoadCSV csv = new LoadCSV("weatherHistory.csv");
        Map<String, List<String>> records = csv.getRecords();

        double[] x = records.get("Humidity").stream()
                .mapToDouble(Double::parseDouble)
                .toArray();

        double[] y = records.get("Apparent Temperature (C)").stream()
                .mapToDouble(Double::parseDouble)
                .toArray();

        linearRegression.fit(x, y);

        vectorLinearRegression.fit(x, y);
    }
}
