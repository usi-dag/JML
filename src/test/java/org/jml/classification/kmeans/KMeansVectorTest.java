package org.jml.classification.kmeans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class KMeansVectorTest {

    static KMeansVector kMeans;

    @BeforeEach
    void setUp() {
        kMeans = new KMeansVector();
    }

    @Test
    void fit() {
        final int SIZE = 18;
        final int DIMENSION = 2;
        final int N_CLUSTER = 2;

        double[][] dataset = new double[SIZE][DIMENSION];
        dataset[0] = new double[]{0, 0};
        dataset[1] = new double[]{0, 1};
        dataset[2] = new double[]{0, 2};
        dataset[3] = new double[]{1, 0};
        dataset[4] = new double[]{1, 1};
        dataset[5] = new double[]{1, 2};
        dataset[6] = new double[]{2, 0};
        dataset[7] = new double[]{2, 1};
        dataset[8] = new double[]{2, 2};
        dataset[9] = new double[]{5, 5};
        dataset[10] = new double[]{5, 6};
        dataset[11] = new double[]{5, 7};
        dataset[12] = new double[]{6, 5};
        dataset[13] = new double[]{6, 6};
        dataset[14] = new double[]{6, 7};
        dataset[15] = new double[]{7, 5};
        dataset[16] = new double[]{7, 6};
        dataset[17] = new double[]{7, 7};



        kMeans.fit(dataset, N_CLUSTER);

        double[][] centroids = kMeans.getCentroids();
        Arrays.sort(centroids, Comparator.comparingDouble(o -> o[0]));
        assertEquals(N_CLUSTER, kMeans.getCentroids().length);
        assertArrayEquals(new double[][]{new double[]{1, 1}, new double[]{6, 6}}, centroids);
        assertEquals(SIZE, kMeans.getCluster_ids().length);
    }
}