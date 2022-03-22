/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 14-Mar-2022
 *  Description: Lockstep breadth first search between two set of vertices.
 * to find out the shortest ancestor path
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code SxLockstepBFS} class represents data type for
 * finding the shortest ancestral path between two sets of
 * source vertices v and w.
 * This use lockstep BFS with exploring vertices from v and w
 * back and forth till it reaches the minimum distance between
 * two sources
 * TODO clear existing arrays instead of creating new ones , for subsequent call
 * on the same SxLockstepBFS object
 */
public class SxLockstepBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] markedV;  // marked[v] = is there an s->v path
    private boolean[] markedW;  // marked[v] = is there an s->v path?
    private int[] distToV;      // distTo[v] = length of shortest s->v path
    private int[] distToW;      // distTo[v] = length of shortest s->v path
    private int shortestAncDist;
    private int shortestAnc;
    private Digraph G;
    private int V;

    public SxLockstepBFS(Digraph G) {
        this.G = G;
        this.V = G.V();
        markedV = new boolean[V];
        markedW = new boolean[V];
        distToV = new int[V];
        distToW = new int[V];
        shortestAncDist = INFINITY;
        shortestAnc = -1;


    }


    private void initialize() {
        markedV = new boolean[V];
        markedW = new boolean[V];
        distToV = new int[V];
        distToW = new int[V];
        shortestAncDist = INFINITY;
        shortestAnc = -1;
    }

    public void lockstepBFS(Iterable<Integer> sourceV, Iterable<Integer> sourceW) {

        initialize();
        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        for (int v : sourceV) {
            markedV[v] = true;
            distToV[v] = 0;
            qV.enqueue(v);
        }

        for (int w : sourceW) {
            markedW[w] = true;
            distToW[w] = 0;
            qW.enqueue(w);
        }
        runBFS(qV, qW);

    }

    public void lockstepBFS(int v, int w) {
        initialize();
        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        markedV[v] = true;
        distToV[v] = 0;
        qV.enqueue(v);

        markedW[w] = true;
        distToW[w] = 0;
        qW.enqueue(w);

        runBFS(qV, qW);

    }

    private void runBFS(Queue<Integer> qV, Queue<Integer> qW) {
        // start exploring vertices
        int currentDistV = 0;
        int currentDistW = 0;
        while (!(qV.isEmpty() && qW.isEmpty())) {

            if (!qV.isEmpty()) {
                int v = qV.dequeue();
                for (int v1 : G.adj(v)) {
                    if (!markedV[v1]) {

                        distToV[v1] = distToV[v] + 1;
                        markedV[v1] = true;
                        qV.enqueue(v1);

                        // check if the vertex v1 already marked from w
                        currentDistV = distToV[v1] + distToW[v1];
                        if (markedW[v1] && (currentDistV < shortestAncDist)) {
                            shortestAncDist = currentDistV;
                            shortestAnc = v1;
                        }

                    }
                    // break
                    if (distToV[v1] >= shortestAncDist) break;
                }

            }
            if (!qW.isEmpty()) {
                int w = qW.dequeue();
                for (int w1 : G.adj(w)) {
                    if (!markedW[w1]) {

                        distToW[w1] = distToW[w] + 1;
                        markedW[w1] = true;
                        qW.enqueue(w1);

                        // check if the vertex v1 already marked from w
                        currentDistW = distToV[w1] + distToW[w1];
                        if (markedV[w1] && (currentDistW < shortestAncDist)) {
                            shortestAncDist = currentDistW;
                            shortestAnc = w1;
                        }
                    }

                    // break
                    if (distToW[w1] >= shortestAncDist) break;
                }
            }

        }
    }

    public int ancestor() {
        return shortestAnc;
    }

    public int distance() {
        return shortestAncDist != INFINITY ? shortestAncDist : -1;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        /**
         ArrayList<Integer> v = new ArrayList<Integer>();
         v.add(13);
         v.add(23);
         v.add(24);
         ArrayList<Integer> w = new ArrayList<Integer>();
         w.add(6);
         w.add(16);
         w.add(17);
         SxLockstepBFS bfs = new SxLockstepBFS(G, v, w);
         StdOut.print("distance:");
         StdOut.println(bfs.distance());
         StdOut.print("ancestor:");
         StdOut.println(bfs.ancestor());
         **/
        StdOut.println("digraph1 3 11");

        SxLockstepBFS bfs = new SxLockstepBFS(G);
        bfs.lockstepBFS(3, 11);
        StdOut.print("distance:");
        StdOut.println(bfs.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs.ancestor());

        StdOut.println("digraph1 9 12");

        StdOut.print("distance:");
        bfs.lockstepBFS(9, 12);
        StdOut.println(bfs.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs.ancestor());

        StdOut.println("digraph1 7 2");

        bfs.lockstepBFS(7, 2);
        StdOut.print("distance:");
        StdOut.println(bfs.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs.ancestor());

        StdOut.println("digraph1 1 6");

        bfs.lockstepBFS(1, 6);
        StdOut.print("distance:");
        StdOut.println(bfs.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs.ancestor());

    }
}
