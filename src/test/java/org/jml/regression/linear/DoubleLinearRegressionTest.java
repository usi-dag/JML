package org.jml.regression.linear;


import org.jml.dataset.LoadCSV;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Map;

class DoubleLinearRegressionTest {

    private static DoubleLinearRegression doubleLinearRegression;
    private static DoubleVectorLinearRegression doubleVectorLinearRegression;

    @BeforeAll
    public static void setupModel() {
        doubleLinearRegression = new DoubleLinearRegression();
        doubleVectorLinearRegression = new DoubleVectorLinearRegression();

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


        doubleLinearRegression.fit(x, y);

        doubleVectorLinearRegression.fit(x, y);

        System.out.println(doubleLinearRegression.predict(0.5));
        System.out.println(doubleVectorLinearRegression.predict(0.5));
    }
}
