package org.jml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyBenchmarkTest {

    @Test
    @DisplayName("Testing junit example")
    public void testMethod() {
        MyBenchmark myBenchmark = new MyBenchmark();

        int result = myBenchmark.testMethod();
        assertEquals(3, result);

    }
}
