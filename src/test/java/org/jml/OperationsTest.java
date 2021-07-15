package org.jml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationsTest {

    private static final Operations operations = new Operations();

    @Test
    void sumVectorArraysTest() {
        double [] x = {1,2,3,4,5,6,7,8,9,10};
        double [] y = {1,0,2,5,2,-9,0,3,2,1};
        double [] result = {2,2,5,9,7,-3,7,11,11,11};
        assertArrayEquals(result, operations.sumVectorArrays(x, y));
    }

    @Test
    void sumArraysTest() {
        double [] x = {1,2,3,4,5,6,7,8,9,10};
        double [] y = {1,0,2,5,2,-9,0,3,2,1};
        double [] result = {2,2,5,9,7,-3,7,11,11,11};
        assertArrayEquals(result, operations.sumArrays(x, y));
    }
}