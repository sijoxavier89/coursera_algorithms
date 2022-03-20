/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 13 - Feb - 2022
 *  Description:
 * Performance requirements.  All methods (and the constructor) should take time at most proportional to E + V in the worst case,
 *  where E and V are the number of edges and vertices in the digraph, respectively. Your data type should use space proportional to E + V.
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // validate 
        
        return 0;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return 0;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;

    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
