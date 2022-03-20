/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 13 - Feb - 2022
 *  Description:
 * Performance requirements.  All methods (and the constructor) should take time at most proportional to E + V in the worst case,
 *  where E and V are the number of edges and vertices in the digraph, respectively. Your data type should use space proportional to E + V.
 * TODO: cache result for length and ancestor
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private int V = -1;
    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = G;
        V = G.V();
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(Integer v) {
        if (v == null)
            throw new IllegalArgumentException("vertex " + v + " is null ");
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(Iterable<Integer> v) {
        for (Integer w : v) {
            if (w == null)
                throw new IllegalArgumentException("vertex " + v + " is null ");
        }

        for (Integer w : v) {
            if (w < 0 || w >= V)
                throw new IllegalArgumentException(
                        "vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // validate
        validateVertex(v);
        validateVertex(w);
        SxLockstepBFS bfs = new SxLockstepBFS(graph, v, w);
        return bfs.distance();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // validate
        validateVertex(v);
        validateVertex(w);
        SxLockstepBFS bfs = new SxLockstepBFS(graph, v, w);
        return bfs.ancestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // validate
        validateVertex(v);
        validateVertex(w);
        SxLockstepBFS bfs = new SxLockstepBFS(graph, v, w);
        return bfs.distance();

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // validate
        validateVertex(v);
        validateVertex(w);
        SxLockstepBFS bfs = new SxLockstepBFS(graph, v, w);
        return bfs.ancestor();

    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
