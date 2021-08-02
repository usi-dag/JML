package org.jml.regression.linear.doubles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleLinearRegressionTest {

    private static DoubleLinearRegression doubleLinearRegression;

    @BeforeEach
    void setUp() {
        doubleLinearRegression = new DoubleLinearRegression();
    }

    @Test
    void fitConstantFunctionTest() {
        double[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] y = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        doubleLinearRegression.fit(x, y);

        assertEquals(0, doubleLinearRegression.getSlope(), "Slope of constant function is 0");
        assertEquals(1, doubleLinearRegression.getIntercept(), "Intercept of constant function is 1");

        assertEquals(1, doubleLinearRegression.predict(5), "Predict a known value");
        assertEquals(1, doubleLinearRegression.predict(-6), "Predict a unknown negative value");
        assertEquals(1, doubleLinearRegression.predict(9.5), "Predict a unknown value");
    }

    @Test
    void fitPositiveLinearFunctionTest() {
        double[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] y = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        doubleLinearRegression.fit(x, y);

        assertEquals(1, doubleLinearRegression.getSlope(), "Slope of constant function is 1");
        assertEquals(1, doubleLinearRegression.getIntercept(), "Intercept of constant function is 1");

        assertEquals(6, doubleLinearRegression.predict(5), "Predict a known value");
        assertEquals(-5, doubleLinearRegression.predict(-6), "Predict a unknown negative value");
        assertEquals(10.5, doubleLinearRegression.predict(9.5), "Predict a unknown value");
    }

    @Test
    void fitNegativeLinearFunctionTest() {
        double[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] y = {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10};

        doubleLinearRegression.fit(x, y);

        assertEquals(-1, doubleLinearRegression.getSlope(), "Slope of negative function is -1");
        assertEquals(-1, doubleLinearRegression.getIntercept(), "Intercept of function is -1");

        assertEquals(-6, doubleLinearRegression.predict(5), "Predict a known value");
        assertEquals(5, doubleLinearRegression.predict(-6), "Predict a unknown negative value");
        assertEquals(-10.5, doubleLinearRegression.predict(9.5), "Predict a unknown value");
    }

}