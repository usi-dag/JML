package org.jml.classification.kmeans;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jml.dataset.LoadCSV;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class KMeansVector{

    private int size;
    private int dimension;
    private int n_cluster;
    private double[][] distanceMatrix;
    private double[][] centroids;
    private int[] cluster_ids;
    private final Random random = new Random();

    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;
    static final int SPECIES_LENGTH = SPECIES.length();


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

        randomCentroid(x);

        // compute the distance matrix of the centroid
        boolean updated = true;
        while (updated) { // new centroids
            l2Matrix(x, centroids, distanceMatrix);

            //assign each point
            for (int i = 0; i < size; i++) {
                double min = distanceMatrix[i][0];
                int min_pos = 0;
                for (int j = 0; j < n_cluster; j++) {
                    if (distanceMatrix[i][j] < min) {
                        //TODO vectorize min point cluster search [-- n_cluster --] where n_cluster > 2
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
                //TODO is possible to vectorize this loop (for the same i calculate 2 js)
                distanceMatrix[i][j] = euclideanDistance(centroids[j], x[i]);
            }
        }


    }

    private void l2HorizontalMatrix(double[][] x, double[][] medeoids, double[][] distanceMatrix) {
        for (int i = 0; i < n_cluster; i++) {
            for (int j = 0; j < size; j++) {
                //TODO is possible to vectorize this loop (for the same i calculate 2 js)
                distanceMatrix[i][j] = euclideanDistance(medeoids[i], x[j]);
            }
        }
    }

    private double euclideanDistance(double[] x, double[] y) {
        double dist = 0;
        int upperBound = SPECIES.loopBound(x.length);

        int i;
        DoubleVector xs;
        DoubleVector ys;

        for (i = 0; i < upperBound; i += SPECIES_LENGTH) {
            xs = DoubleVector.fromArray(SPECIES, x, i);
            ys = DoubleVector.fromArray(SPECIES, y, i);

            xs = xs.sub(ys);
            dist +=  xs.mul(xs).reduceLanes(VectorOperators.ADD); ;
        }

        for (; i < dimension; i++) {
            dist += (x[i] - y[i]) * (x[i] - y[i]);
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

    /**
     * Recompute the centroid of each cluster
     * Compute the mean point of the cluster and
     * set as centroid the point with minimum of
     * this point.
     */
    private double[][] recomputeCentroid(double[][] x) {
        double[][] medeoids = new double[n_cluster][dimension];

        for (int i = 0; i < n_cluster; i++) {
            int counter = 0;
            for (int j = 0; j < size; j++) {
                if (i == cluster_ids[j]) counter++;
            }
            for (int j = 0; j < size; j++) {
                if (i == cluster_ids[j]) {
                    for (int k = 0; k < dimension; k++) {
                        medeoids[i][k] += x[j][k] / counter; //TODO Vectorize

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
                    //TODO vecrtorize points to single centroid serch [--- size ----] <-- min
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
//        KMeansVector kMeans = new KMeansVector();
//        LoadCSV loader = new LoadCSV("weatherHistory.csv");
//
//        Map<String, List<String>> records = loader.getRecords();
//
//        List<String> temperature = records.get("Temperature (C)");
//        List<String> humidity = records.get("Humidity");
//
//        double[][] dataset = new double[temperature.size()][2];
//        for (int i = 0; i < temperature.size(); i++) {
//            dataset[i] = new double[]{Double.parseDouble(temperature.get(i)), Double.parseDouble(humidity.get(i))};
//        }
//
//
//
//        kMeans.fit(dataset, 3);
//
//        List<String> clusters = Arrays
//                .stream(kMeans.getCluster_ids())
//                .mapToObj(Integer::toString)
//                .collect(Collectors.toList());
//
//        loader.addRecord("Cluster", clusters);
//
//        loader.saveCSV("weather.csv");
//
//        System.out.println(Arrays.deepToString(kMeans.getCentroids()));

        double[] data = new double[]{1, 4, 2, 4, 5, 0, 10, 2, 7, 6, 1, 5};
        int upperBound = SPECIES.loopBound(data.length);
        DoubleVector minValues = DoubleVector.fromArray(SPECIES, data, 0);
        DoubleVector indices = DoubleVector.fromArray(SPECIES, new double[]{0, 1, 2, 3}, 0);

        for (int i = SPECIES_LENGTH; i < upperBound; i += SPECIES_LENGTH) {
            DoubleVector mask = DoubleVector.fromArray(SPECIES, data, i);
            VectorMask<Double> less = mask.lt(minValues);
            minValues = minValues.min(mask);
            DoubleVector current_indices = DoubleVector.fromArray(SPECIES, new double[]{i, i+1, i+2, i+3}, 0);
            indices = indices.blend(current_indices, less);
        }

        // scalar part to find min inside the 4 double element index
        double[] values = minValues.toDoubleArray();
        double min = values[0];
        int index = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
                index = i;
            }
        }

        int cluster_index = indices.toIntArray()[index];

        System.out.println("Min index is: " + cluster_index + ", minimum is: " + data[cluster_index]);
    }

}
