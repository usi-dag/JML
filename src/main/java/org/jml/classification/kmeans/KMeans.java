package org.jml.classification.kmeans;


import java.util.HashMap;
import java.util.Random;


public class KMeans {

    private int size;
    private int n_cluster;
    private double[][] distanceMatrix = new double[size][n_cluster];
    private final double[][] centroids = new double[n_cluster][];
    private int[] cluster_ids = new int[size];
    private final Random random = new Random();


    /**
     *
     * @param x array list of point (represented as array)
     * @param n_cluster the number of cluster
     */
    public void fit(final double[][] x, final int n_cluster) {
        this.n_cluster = n_cluster;
        this.size = x[0].length;
        //TODO use kmeans++ to choose the centroid instead of simple randomization
        randomCentroid(x);

        // compute the distance matrix of the centroid
        l2Matrix(x);

        //assign each point
        for (int i = 0; i < size; i++) {
            double min = distanceMatrix[i][0];
            int min_pos = 0;
            for (int j = 0; j < n_cluster; j++) {
                if (distanceMatrix[i][j] < min) {
                    min = distanceMatrix[i][j];
                    min_pos = j;
                }
            }

            cluster_ids[i] = min_pos;
        }

        //TODO recompute centroid
    }

    /**
     *
     * @param x a double array (double[]) representing the point in the n-space
     * @return the cluster id belonging to the input point x
     */
    public int predict(final double[] x) {
        //TODO compute distances from centroid and return the centroid with minimum distance
        int cluster_id = 0;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n_cluster; i++) {
            if (euclideanDistance(x ,centroids[i]) < min) {
                min = euclideanDistance(x, centroids[i]);
                cluster_id = i;
            }
        }
        return cluster_id;
    }

    /**
     * calculate all the euclidean distances for all points
     * in the dataset and store it in the distance matrix
     */
    private void l2Matrix(double[][] x) {

        for (int i = 0; i < n_cluster; i++) {
            for (int j = 0; j < size; j++) {
                distanceMatrix[j][i] = euclideanDistance(centroids[i], x[j]);
            }
        }
    }

    private double euclideanDistance(double[] x, double[] y) {
        double dist = 0;

        for (int i = 0; i < x.length; i++) {
            dist += x[i] * x[i] + y[i] * y[i] + -2 * x[i] * y[i];
        }

        return dist;
    }

    /**
     * Select the initial @n_cluster centroid
     */
    private void randomCentroid(double[][] x) {
        for(int i = 0; i < this.n_cluster; ++i) {
            centroids[i] = x[random.nextInt()];
        }
    }

    /**
     * Recompute the centroid of each cluster
     * Compute the mean point of the cluster and
     * set as centroid the point with minimum of
     * this point.
     */
    private void recomputeCentroid(double[][] x) {
        double[][] meanCentroids = new double[n_cluster][x[0].length];

        for (int i = 0; i < n_cluster; i++) {
            for (int j = 0; j < size; j++) {
                if (i == cluster_ids[j]) {
                    for (int k = 0; k < x[0].length; k++) {
                        //TODO mean centroid -> better to save centroid in hashmap (id -> point (double[]))
                        meanCentroids[i][k] += x[i][k] / x.length;
                    }
                }
            }
        }

        //TODO find minimum distance point to mean centroid and check if they differ from previous one

    }
}
