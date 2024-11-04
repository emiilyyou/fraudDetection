import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.LinkedList;

public class BoostingAlgorithm {

    private double[] weights; // double array of corresponding weights
    private int[][] reducedInput; // 2D int array of reduced dimensions
    private int[] labels; // int array of signs
    private LinkedList<WeakLearner> list; // list to store WeakLearner objects
    private Clustering cluster; // cluster object


    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        if (input == null || locations == null || labels == null) {
            throw new IllegalArgumentException();
        }
        int m = locations.length;
        int n = input.length;
        if ((input[0].length != m) || (input.length != labels.length)) {
            throw new IllegalArgumentException();
        }
        if (k < 1 || k > m) throw new IllegalArgumentException();

        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != 0 && labels[i] != 1) {
                throw new IllegalArgumentException();
            }
        }
        weights = new double[n];
        this.labels = labels.clone();
        list = new LinkedList<>();
        reducedInput = new int[n][k];

        // normalize weight array so sum of elements equals 1
        for (int i = 0; i < n; i++) {
            weights[i] = 1.0 / n;
        }

        cluster = new Clustering(locations, k);

        // populate reduced dimension array with reduced dimensions of input
        for (int i = 0; i < input.length; i++) {
            reducedInput[i] = cluster.reduceDimensions(input[i]);
        }

    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        double totalWeight = 0;
        // create a WeakLearner object using current weights and input
        WeakLearner weaklearner = new WeakLearner(reducedInput, weights, labels);

        // double weight if weak learner mislabels it
        for (int i = 0; i < weights.length; i++) {
            if (weaklearner.predict(reducedInput[i]) != labels[i]) {
                weights[i] *= 2;
            }
            // update total weight
            totalWeight += weights[i];
        }

        // renormalize weight array to 1 again
        for (int j = 0; j < weights.length; j++) {
            weights[j] /= totalWeight;
        }
        // add WeakLearner object to linked list
        list.add(weaklearner);
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) throw new IllegalArgumentException();

        // keeps track of 0 and 1 counts
        int zeroCount = 0;
        int oneCount = 0;

        // develop clusters of sample array
        int[] reducedSample = cluster.reduceDimensions(sample);

        // update the counts of 1 and 0 depending on their frequency
        for (WeakLearner x : list) {
            if (x.predict(reducedSample) == 0) zeroCount++;
            else oneCount++;
        }

        // output the label that was predicted the most
        if (oneCount > zeroCount) return 1;
        else if (zeroCount > oneCount) return 0;
        else return 0;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int x = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        Stopwatch watch = new Stopwatch();
        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < x; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testAccuracy += 1;
        testAccuracy /= testing.getN();

        StdOut.println("time:" + watch.elapsedTime());

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);
        StdOut.println(model.weightOf(1));
    }

}

