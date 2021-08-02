package org.jml.regression.linear.integers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerVectorLinearRegressionTest {

    static IntegerVectorLinearRegression linearRegression;

    @BeforeEach
    void setUp() {
        linearRegression = new IntegerVectorLinearRegression();
    }

    @Test
    void fitConstantFunctionTest() {
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] y = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        linearRegression.fit(x, y);

        assertEquals(0, linearRegression.getSlope(), "Slope of constant function is 0");
        assertEquals(1, linearRegression.getIntercept(), "Intercept of constant function is 1");

        assertEquals(1, linearRegression.predict(5), "Predict a known value");
        assertEquals(1, linearRegression.predict(-6), "Predict a unknown negative value");
        assertEquals(1, linearRegression.predict(90), "Predict a unknown value");
    }

    @Test
    void fitPositiveLinearFunctionTest() {
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] y = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        linearRegression.fit(x, y);

        assertEquals(1, linearRegression.getSlope(), "Slope of constant function is 1");
        assertEquals(1, linearRegression.getIntercept(), "Intercept of constant function is 1");

        assertEquals(6, linearRegression.predict(5), "Predict a known value");
        assertEquals(-5, linearRegression.predict(-6), "Predict a unknown negative value");
        assertEquals(91, linearRegression.predict(90), "Predict a unknown value");
    }

    @Test
    void fitNegativeLinearFunctionTest() {
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] y = {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10};

        linearRegression.fit(x, y);

        assertEquals(-1, linearRegression.getSlope(), "Slope of negative function is -1");
        assertEquals(-1, linearRegression.getIntercept(), "Intercept of function is -1");

        assertEquals(-6, linearRegression.predict(5), "Predict a known value");
        assertEquals(5, linearRegression.predict(-6), "Predict a unknown negative value");
        assertEquals(-91, linearRegression.predict(90), "Predict a unknown value");
    }
}