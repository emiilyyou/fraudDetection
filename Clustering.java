import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

public class Clustering {
    private int m; // vertices; locations on map
    private int k; // number of clusters
    private CC cc; // graph of connected components

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null) {
            throw new IllegalArgumentException();
        }
        m = locations.length;
        if (k < 1 || k > m) {
            throw new IllegalArgumentException();
        }
        this.k = k;

        // graph that connects locations and is weighted according to distance
        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations.length; j++) {
                if (i == j) continue;
                double distance = locations[i].distanceSquaredTo(locations[j]);
                Edge edge = new Edge(i, j, distance);
                graph.addEdge(edge);
            }
        }

        KruskalMST mst = new KruskalMST(graph);

        EdgeWeightedGraph newgraph = new EdgeWeightedGraph(m);

        // keeps track of the number of edges
        int count = 0;
        // add m-k edges to graph with lowest weight of spanning tree
        for (Edge edge : mst.edges()) {
            if (count == m - k) break;
            count++;
            newgraph.addEdge(edge);
        }

        cc = new CC(newgraph);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i > m - 1) {
            throw new IllegalArgumentException();
        }
        return cc.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        if (input.length != m) {
            throw new IllegalArgumentException();
        }
        // create int array of reduced dimensions
        int[] array = new int[k];
        for (int i = 0; i < input.length; i++) {
            int cluster = clusterOf(i);
            array[cluster] += input[i];
        }
        return array;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Point2D[] points = new Point2D[in.readInt()];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        Clustering cluster = new Clustering(points, 5);
        StdOut.println(cluster.clusterOf(2));
        int[] input = { 1, 2, 3, 4 };
        StdOut.println(cluster.reduceDimensions(input));
    }
}
