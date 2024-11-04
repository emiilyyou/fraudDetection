Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
To implement the Clustering constructor, we created three instance variables,
specifically the number of locations (m), the number of clusters (k), and a
CC data type that graphed connected components (cc). To create the EdgeWeighted-
Graph, we used m and iterated through a double for loop to compute the distances
between two points to use that as an argument in creating an Edge object. Each
Edge object took in i, j, and distance as parameters, which we incrementally added
to our graph. Later, we also created a MST that considered m-k edges with lowest
weight of the spanning tree to form a cluster graph (cc) with these edges. As
a result, we created a condition that if count = m-k, we would break the loop;
otherwise, we continued to add the edges to the graph.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
 To implement the WeakLearner constructor, we created four instance variables:
 bestdp for the max dimension, bestvp for the max volume, bestsp for the
 predicted sign, length for the size of input[][] array's columns. After throwing
 all the appropriate illegal argument exceptions, we first sort the input array
 using a helper method created later in order to populate a new array with the
 sorted weaklearner objects. Then, we use for loops to go through dp, vp, and sp
 and update the currentsign according to the given sp. We also update the weight
 according to the labels and the sign prediction. Finally, we update the final
 sp, dp, vp according to the corner cases.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
    100         100         0.94875             2.112
    100         200         0.94875             3.927
    100         300         0.94875             5.403
    100         400         0.94875             7.053
    100         500         0.94875             9.129
    125         300         0.95                6.637
    125         400         0.95                8.289
    125         450         0.95                9.587
    125         475         0.9525              9.778
    125         480         0.95                9.783
    130         300         0.91375             6.448
    150         300         0.86                7.101


/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */

Our maximized accuracy was when k was 125 and T was around 475.

1. Our strategy was holding K as a constant while we tried different values
of T to see the different test accuracies and associated times. We incremented
T by 100 to see how our accuracy and time was affected. When we reached the 0.95
threshold we changed the T little at a time to stay as close to 10 seconds while
still being under.

2. A small value of T leads to low test accuracy because more iterations would
result in a higher probability of being predicted as the correct label, thus
increasing accuracy.

3. A k that is too small leads to too few dimensions and other variables of
clusters to consider, giving us a stastically insignificant accuracy and a k
that is too big leads to too many variables to consider with the increase of
clusters, making the range difficult to compute.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

N/A

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

We had trouble understanding the assignment conceptually. However, we were able
to receive insight from lab TAs and understood it better.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
