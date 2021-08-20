package org.jml.classification.kmeans;

import jdk.incubator.vector.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class KMeansVector{

    private int size;
    private int dimension;
    private int n_cluster;
    private double[][] distanceMatrix;
    private double[][] centroids;
    private int[] cluster_ids;
    private final Random random = new Random();
    private boolean calculateCentroids = true;
    static final VectorSpecies<Double> DOUBLE_SPECIES = DoubleVector.SPECIES_PREFERRED;
    static final int DOUBLE_SPECIES_LENGTH = DOUBLE_SPECIES.length();

    static final VectorSpecies<Integer> INTEGER_SPECIES = IntVector.SPECIES_PREFERRED;
    static final int INTEGER_SPECIES_LENGTH = INTEGER_SPECIES.length();

    KMeansVector() {}

    KMeansVector(boolean calculateCentroids) {
        this.calculateCentroids = calculateCentroids;
    }


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

        fixedCentroid(x);

        // compute the distance matrix of the centroid

        while (true) { // new centroids
            boolean converged = true;
            l2Matrix(x, centroids, distanceMatrix);

            //assign each point
            for (int i = 0; i < size; i++) {
                int current_cluster = cluster_ids[i];
                cluster_ids[i] = findMinIndex(distanceMatrix[i]);

                if (current_cluster != cluster_ids[i]) {
                    converged = false;
                }
            }

            double[][] newCentroids = recomputeCentroid(x);
            centroids = newCentroids;

            if (converged) break;
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
        int upperBound = DOUBLE_SPECIES.loopBound(dimension);

        int i;
        DoubleVector xs;
        DoubleVector ys;

        for (i = 0; i < upperBound; i += DOUBLE_SPECIES_LENGTH) {
            xs = DoubleVector.fromArray(DOUBLE_SPECIES, x, i);
            ys = DoubleVector.fromArray(DOUBLE_SPECIES, y, i);

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

        int upperBound = INTEGER_SPECIES.loopBound(cluster_ids.length);
        int i = 0;
        // Count size of cluster only if the number of cluster are less than the species length
        // n_cluster * (size / SPECIES_LENGTH) -> number of iteration --> if n_cluster > SPECIES_LENGTH then more access than scalar
        if (false && n_cluster < INTEGER_SPECIES_LENGTH) { // mask is not optimized therefore skip this loop
            for (i = 0; i < upperBound; i += INTEGER_SPECIES_LENGTH) {
                IntVector points = IntVector.fromArray(INTEGER_SPECIES, cluster_ids, i);
                IntVector id = IntVector.broadcast(INTEGER_SPECIES, 0);
                for (int j = 0; j < n_cluster; j++) {
                    VectorMask<Integer> mask = points.compare(VectorOperators.EQ, id);
                    clusterSizes[j] += mask.trueCount();
                    id = id.add(1);
                }
            }
        }

        for (; i < size; i++) {
            clusterSizes[cluster_ids[i]] += 1;
        }


        upperBound = DOUBLE_SPECIES.loopBound(dimension);
        i = 0;
        for (; i < n_cluster; i++) {
            DoubleVector medeoid = DoubleVector.zero(DOUBLE_SPECIES);
            int k = 0;
            for (; k < upperBound; k += DOUBLE_SPECIES_LENGTH) {
                for (int j = 0; j < size; j++) {
                    if (i == cluster_ids[j]) {
                        medeoid = medeoid.add(DoubleVector.fromArray(DOUBLE_SPECIES, x[j], k));
                    }
                }
                double[] meanMedeoid = medeoid.div(clusterSizes[i]).toDoubleArray();
                for (int j = 0; j < meanMedeoid.length; j++) {
                    medeoids[i][k+j] = meanMedeoid[j];
                }
            }
            // scalar part for remaining points
            for (int j = 0; j < size; j++) {
                if (i == cluster_ids[j]) {
                    for (int dim = k; dim < dimension; dim++) {
                        medeoids[i][dim] += x[j][dim] / clusterSizes[i];

                    }
                }
            }
        }

        if (!calculateCentroids) {
            return medeoids;
        }

        double[][] medeoidDistanceMatrix = new double[n_cluster][size];

        // find near point to medeoid with a new distance matrix
        l2HorizontalMatrix(x, medeoids, medeoidDistanceMatrix);

        // assign point with minimum distance to be new cluster
        double[][] updatedCentroids = new double[n_cluster][dimension];

        for (i = 0; i < n_cluster; i++) {
            int index = findMinIndex(medeoidDistanceMatrix[i]);
            updatedCentroids[i] = x[index];
        }

        return updatedCentroids;
    }

    private int findMinIndex(double[] data) {

        int index = 0;
        int i = 0;
        double min = Double.POSITIVE_INFINITY;

        if (false && DOUBLE_SPECIES_LENGTH < data.length) {// mask is not optimized therefore skip this loop
            double[] ind = new double[DOUBLE_SPECIES_LENGTH];
            for (int j = 0; j < DOUBLE_SPECIES_LENGTH; j++) {
                ind[j] = j;
            }

            int upperBound = DOUBLE_SPECIES.loopBound(data.length);
            DoubleVector minValues = DoubleVector.fromArray(DOUBLE_SPECIES, data, 0);
            DoubleVector indices = DoubleVector.fromArray(DOUBLE_SPECIES, ind, 0);
            DoubleVector currentIndices = indices;
            for (i = DOUBLE_SPECIES_LENGTH; i < upperBound; i += DOUBLE_SPECIES_LENGTH) {
                DoubleVector mask = DoubleVector.fromArray(DOUBLE_SPECIES, data, i);
                VectorMask<Double> less = mask.lt(minValues);
                minValues = minValues.min(mask);
                currentIndices =  currentIndices.add(DOUBLE_SPECIES_LENGTH);
                indices = indices.blend(currentIndices, less);
            }

            // scalar part to find min inside the DOUBLE_SPECIES_LENGTH double element index
            double[] values = minValues.toDoubleArray();
            for (int j = 0; j < values.length; j++) {
                if (values[j] < min) {
                    min = values[j];
                    index = j;
                }
            }
            index = indices.toIntArray()[index];
        }

        // scalar to find min in residuals
        for (; i < data.length; i++) {
            if (data[i] < min) {
                min = data[i];
                index = i;
            }
        }

        return index;
    }

    public double[][] getCentroids() {
        return centroids;
    }

    public int[] getCluster_ids() {
        return cluster_ids;
    }

    public static void main(String[] args) throws IOException {
    }

}
