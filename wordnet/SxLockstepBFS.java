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

import java.util.ArrayList;

/**
 * The {@code SxLockstepBFS} class represents data type for
 * finding the shortest ancestral path between two sets of
 * source vertices v and w.
 * This use lockstep BFS with exploring vertices from v and w
 * back and forth till it reaches the minimum distance between
 * two sources
 */
public class SxLockstepBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] markedV;  // marked[v] = is there an s->v path
    private boolean[] markedW;  // marked[v] = is there an s->v path?
    private int[] distToV;      // distTo[v] = length of shortest s->v path
    private int[] distToW;      // distTo[v] = length of shortest s->v path
    private int shortestAncDist;
    private int shortestAnc;

    public SxLockstepBFS(Digraph G, Iterable<Integer> sourceV, Iterable<Integer> sourceW) {
        markedV = new boolean[G.V()];
        markedW = new boolean[G.V()];
        distToV = new int[G.V()];
        distToW = new int[G.V()];
        shortestAncDist = INFINITY;
        shortestAnc = -1;
        lockstepBFS(G, sourceV, sourceW);

    }

    private void lockstepBFS(Digraph G, Iterable<Integer> sourceV, Iterable<Integer> sourceW) {
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

        // start exploring vertices
        int currentDistV = 0;
        int currentDistW = 0;
        while (!(qV.isEmpty() && qW.isEmpty())) {

            if (!qV.isEmpty()) {
                int v = qV.dequeue();
                for (int v1 : G.adj(v)) {
                    if (!markedV[v1]) {
                        // edgeToV[v1] = v1;
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
                        // edgeToW[w1] = w1;
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
        ArrayList<Integer> v = new ArrayList<Integer>();
        v.add(3);
        ArrayList<Integer> w = new ArrayList<Integer>();
        w.add(11);
        SxLockstepBFS bfs = new SxLockstepBFS(G, v, w);
        StdOut.print("distance:");
        StdOut.println(bfs.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs.ancestor());

        StdOut.println("digraph1 9 12");
        ArrayList<Integer> v1 = new ArrayList<Integer>();
        v1.add(9);
        ArrayList<Integer> w1 = new ArrayList<Integer>();
        w1.add(12);
        SxLockstepBFS bfs1 = new SxLockstepBFS(G, v1, w1);
        StdOut.print("distance:");
        StdOut.println(bfs1.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs1.ancestor());

        StdOut.println("digraph1 7 2");
        ArrayList<Integer> v2 = new ArrayList<Integer>();
        v2.add(7);
        ArrayList<Integer> w2 = new ArrayList<Integer>();
        w2.add(2);
        SxLockstepBFS bfs2 = new SxLockstepBFS(G, v2, w2);
        StdOut.print("distance:");
        StdOut.println(bfs2.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs2.ancestor());

        StdOut.println("digraph1 1 6");
        ArrayList<Integer> v3 = new ArrayList<Integer>();
        v3.add(1);
        ArrayList<Integer> w3 = new ArrayList<Integer>();
        w3.add(6);
        SxLockstepBFS bfs3 = new SxLockstepBFS(G, v3, w3);
        StdOut.print("distance:");
        StdOut.println(bfs3.distance());
        StdOut.print("ancestor:");
        StdOut.println(bfs3.ancestor());

    }
}
