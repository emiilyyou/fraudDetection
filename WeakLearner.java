import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {

    private int bestdp; // max dimension
    private int bestvp; // max volume
    private int bestsp; // predicted sign
    private int length; // size of input[][] array's column

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException();
        }
        if ((input.length != weights.length)
                || (input.length != labels.length)) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < 0) throw new IllegalArgumentException();
        }
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != 0 && labels[i] != 1) {
                throw new IllegalArgumentException();
            }
        }

        length = input[0].length;

        int currentSign; // predicted sign
        double maxWeight = 0;

        WeakLearnerSort[] sortarray = new WeakLearnerSort[input.length];
        // dp
        for (int dp = 0; dp < input[0].length; dp++) {
            // vp
            // populate sortarray with WeakLearnerSort objects
            for (int vp = 0; vp < input.length; vp++) {
                WeakLearnerSort sort = new WeakLearnerSort(input[vp][dp], vp);
                sortarray[vp] = sort;
            }
            Arrays.sort(sortarray);
            // sp (sign)
            for (int sp = 0; sp < 2; sp++) {
                double currentWeight = 0;
                for (int n = 0; n < input.length; n++) {
                    // updates sign depending on sp
                    if (sp == 0) {
                        currentSign = 1;
                    }
                    else {
                        currentSign = 0;
                    }
                    // add weight if sign is correctly predicted
                    if (labels[n] == currentSign) {
                        currentWeight += weights[n];
                    }
                }
                // k = index in sorted array
                for (int k = 0; k < input.length; k++) {
                    // split is on point; increment/decrement based on accuracy of
                    // sign prediction
                    if (labels[sortarray[k].getIndex()] == sp) {
                        currentWeight += weights[sortarray[k].getIndex()];
                    }
                    else {
                        currentWeight -= weights[sortarray[k].getIndex()];
                    }
                    // update final sp, dp, vp considering corner cases
                    if (currentWeight >= maxWeight && (k == input.length - 1
                            || sortarray[k].point != sortarray[k + 1].point)) {
                        maxWeight = currentWeight;
                        bestsp = sp;
                        bestdp = dp;
                        bestvp = sortarray[k].point;
                    }
                }
            }
        }
    }

    private class WeakLearnerSort implements Comparable<WeakLearnerSort> {
        private int point; // coordinates of input[][] array
        private int index; // point's index

        // initializes point and index
        public WeakLearnerSort(int point, int index) {
            this.point = point;
            this.index = index;
        }

        // returns index value
        public int getIndex() {
            return index;
        }

        // compares two points
        public int compareTo(WeakLearnerSort p) {
            return this.point - p.point;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException();
        }
        if (sample.length != length) {
            throw new IllegalArgumentException();
        }

        int currentSign = -1;

        // compares sample's dp/vp with bestvp/bestdp and update stump
        if (sample[bestdp] <= bestvp && bestsp == 0) {
            currentSign = 0;
        }
        else if (sample[bestdp] > bestvp && bestsp == 0) {
            currentSign = 1;
        }
        else if (sample[bestdp] <= bestvp && bestsp == 1) {
            currentSign = 1;
        }
        else if (sample[bestdp] > bestvp && bestsp == 1) {
            currentSign = 0;
        }
        return currentSign;
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return bestdp;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return bestvp;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return bestsp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In datafile = new In(args[0]);

        int n = datafile.readInt();
        int k = datafile.readInt();

        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }

        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }

        WeakLearner weakLearner = new WeakLearner(input, weights, labels);
        StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(), weakLearner.signPredictor());
        int[] sample = { 1, 2, 3, 4 };
        StdOut.println(weakLearner.predict(sample));
    }
}
