package org.jml.classification.kmeans;


import org.jml.dataset.LoadCSV;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class KMeans {

    private int size;
    private int dimension;
    private int n_cluster;
    private double[][] distanceMatrix;
    private double[][] centroids;
    private int[] cluster_ids;
    private final Random random = new Random();


    /**
     *
     * @param x array list of point (represented as array)
     * @param n_cluster the number of cluster
     */
    public void fit(final double[][] x, final int n_cluster) {
        this.n_cluster = n_cluster;
        this.size = x.length;
        this.dimension = x[0].length;
        this.distanceMatrix = new double[size][n_cluster];
        this.centroids = new double[n_cluster][dimension];
        this.cluster_ids = new int[size];
        //TODO use kmeans++ to choose the centroid instead of simple randomization
        fixedCentroid(x);

        // compute the distance matrix of the centroid
        boolean updated = true;
        while (updated) { // new centroid
            l2Matrix(x, centroids, distanceMatrix);

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
            double[][] newCentroids = recomputeCentroid(x);

            updated = !Arrays.deepEquals(newCentroids, centroids);
            centroids = newCentroids;
        }
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
    private void l2Matrix(double[][] x, double[][] centroids, double[][] distanceMatrix) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < n_cluster; j++) {
                distanceMatrix[i][j] = euclideanDistance(centroids[j], x[i]);
            }
        }


    }

    private void l2HorizontalMatrix(double[][] x, double[][] medeoids, double[][] distanceMatrix) {
        for (int i = 0; i < n_cluster; i++) {
            for (int j = 0; j < size; j++) {
                distanceMatrix[i][j] = euclideanDistance(medeoids[i], x[j]);
            }
        }
    }

    private double euclideanDistance(double[] x, double[] y) {
        double dist = 0;

        for (int i = 0; i < dimension; i++) {
            dist += x[i] * x[i] + y[i] * y[i] + -2 * x[i] * y[i];
        }

        return dist;
    }

    /**
     * Select the initial @n_cluster centroid
     */
    private void randomCentroid(double[][] x) {
        for(int i = 0; i < this.n_cluster; ++i) {
            centroids[i] = x[random.nextInt(size)];
        }
    }

    private void fixedCentroid(double[][] x) {
        int dist = x.length / this.n_cluster;
        int centroid = 0;
        for (int i = 0; i < n_cluster; i++) {
            centroids[i] = x[centroid];
            centroid += dist;
        }
    }

    /**
     * Recompute the centroid of each cluster
     * Compute the mean point of the cluster and
     * set as centroid the point with minimum of
     * this point.
     */
    private double[][] recomputeCentroid(double[][] x) {
        double[][] medeoids = new double[n_cluster][dimension];
        int [] clusterSizes = new int[n_cluster];

        for (int i = 0; i < size; i++) {
            clusterSizes[cluster_ids[i]] += 1;
        }

        for (int i = 0; i < n_cluster; i++) {
            for (int j = 0; j < size; j++) {
                if (i == cluster_ids[j]) {
                    for (int k = 0; k < dimension; k++) {
                        medeoids[i][k] += x[j][k] / clusterSizes[i]; //TODO Vectorize

                    }
                }
            }
        }

        double[][] medeoidDistanceMatrix = new double[n_cluster][size];


        // find near point to medeoid with a new distance matrix
        l2HorizontalMatrix(x, medeoids, medeoidDistanceMatrix);

        // assign point with minimum distance to be new cluster
        double[][] updatedCentroids = new double[n_cluster][dimension];

        for (int i = 0; i < n_cluster; i++) {
            double minDist = Double.POSITIVE_INFINITY;
            for (int j = 0; j < size; j++) {
                if (medeoidDistanceMatrix[i][j] < minDist) {
                    minDist = medeoidDistanceMatrix[i][j];
                    updatedCentroids[i] = x[j];
                }
            }
        }
        return updatedCentroids;
    }

    public double[][] getCentroids() {
        return centroids;
    }

    public int[] getCluster_ids() {
        return cluster_ids;
    }

    public static void main(String[] args) throws IOException {
        KMeans kMeans = new KMeans();
        LoadCSV loader = new LoadCSV("weatherHistory.csv");

        Map<String, List<String>> records = loader.getRecords();

        List<String> temperature = records.get("Temperature (C)");
        List<String> humidity = records.get("Humidity");

        double[][] dataset = new double[temperature.size()][2];
        for (int i = 0; i < temperature.size(); i++) {
            dataset[i] = new double[]{Double.parseDouble(temperature.get(i)), Double.parseDouble(humidity.get(i))};
        }



        kMeans.fit(dataset, 3);

        List<String> clusters = Arrays
                .stream(kMeans.getCluster_ids())
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());

        loader.addRecord("Cluster", clusters);

        loader.saveCSV("weather.csv");

        System.out.println(Arrays.deepToString(kMeans.getCentroids()));
    }
}
