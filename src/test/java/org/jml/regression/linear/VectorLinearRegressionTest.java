package org.jml.regression.linear;

import org.jml.dataset.LoadCSV;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VectorLinearRegressionTest {

    private static LongLinearRegression longLinearRegression;
    private static LongVectorLinearRegression longVectorLinearRegression;

    @BeforeAll
    public static void setupModel() {
        longLinearRegression = new LongLinearRegression();
        longVectorLinearRegression = new LongVectorLinearRegression();

    }

    @Test
    @DisplayName("Testing Linear Regression")
    void simpleLinearRegressionTest() {

        long[] x = {0,1,2,3,4,5,6,7,8,9,10};

        long[] y = {4,2,3,4,5,6,-7,4,8,6,4};

        longLinearRegression.fit(x, y);

        longVectorLinearRegression.fit(x, y);

        System.out.println(longLinearRegression.predict(1));
        System.out.println(longVectorLinearRegression.predict(1));
    }


}


