/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 15-jan-2022
 *  Description:Build BST with points in the nodes, using x and y crdinates of the points as keys
 *  in strictly alternating sequence
 *  Search and insert
 *  Draw
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private Node root;
    private int size;
    private Point2D champion;
    private double championDist;

    public KdTree() {

    }

    private static class Node {
        private Point2D p;    // the point
        private RectHV rect;  // the axis aligned rectangle corresponding to this node
        private Node lb;      // the left/bottom subtree
        private Node rt;      // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }

    }


    public boolean isEmpty()                      // is the set empty?
    {
        return size == 0;
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        put(p);
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param p the Point in the 2D plane
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    private void put(Point2D p) {

        if (p == null) throw new IllegalArgumentException("calls put() with a null key");

        root = put(root, p, true, new RectHV(0, 0, 1, 1));

    }

    private Node put(Node x, Point2D p, boolean dividebyX, RectHV rect) {
        if (x == null) {
            size = size + 1; // update tree size
            return new Node(p, rect);
        }
        int cmp = 0;
        if (dividebyX) {
            if (p.x() > x.p.x()) cmp = 1;
            else if (p.x() < x.p.x()) cmp = -1;
        }
        else {
            if (p.y() > x.p.y()) cmp = 1;
            else if (p.y() < x.p.y()) cmp = -1;
        }

        if (cmp < 0) {
            if (dividebyX)
                x.lb = put(x.lb, p, !dividebyX,
                           new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax()));
            else x.lb = put(x.lb, p, !dividebyX,
                            new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y()));
        }
        else {
            if (dividebyX)
                x.rt = put(x.rt, p, !dividebyX,
                           new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax()));
            else x.rt = put(x.rt, p, !dividebyX,
                            new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax()));
        }

        return x;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        Point2D x = get(p);
        return x != null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param p the point in the 2D plane
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    private Point2D get(Point2D p) {
        return get(root, p, true);
    }

    private Point2D get(Node x, Point2D p, boolean dividebyX) {
        if (p == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = 0;
        if (dividebyX) {
            if (p.x() > x.p.x()) cmp = 1;
            else if (p.x() < x.p.x()) cmp = -1;
        }
        else {
            if (p.y() > x.p.y()) cmp = 1;
            else if (p.y() < x.p.y()) cmp = -1;
        }


        if (cmp < 0) return get(x.lb, p, !dividebyX);
        else return get(x.rt, p, !dividebyX);
        // else return x.p;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    private void draw(Node x, boolean divideByX) {
        if (x == null) return;

        draw(x.lb, !divideByX);
        draw(x.rt, !divideByX);
        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x.p.x(), x.p.y());

        // draw split by x line
        if (divideByX) {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            // draw split by y line
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(
            RectHV rect) {
        Queue<Point2D> points = new Queue<Point2D>();
        search(rect, root, points);
        return points;
    }

    // If a query reactangle does not intersect the corresponding to a node, there is no need to explore
    // that node. A subtree is only searched if it might conatain a point contained in the query rectangle
    private void search(RectHV rect, Node x, Queue<Point2D> points) {
        if (x == null) return;
        if (rect.contains(x.p)) points.enqueue(x.p);
        if ((x.lb != null) && x.lb.rect.intersects(rect)) search(rect, x.lb, points);
        if ((x.rt != null) && x.rt.rect.intersects(rect)) search(rect, x.rt, points);

    }

    // if the closest point discovered so far is closer than the distance between the query point and
    // the rectangle corresponding to a node, there is no need to explore that node (or its subtrees).
    // That is, search a node only only if it might contain a point that is closer than the best one found so far.
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(
            Point2D p) {
        champion = root.p;
        championDist = Double.POSITIVE_INFINITY;
        nearest(root, p, true);
        return champion;
    }

    private void nearest(Node x, Point2D target, boolean dividebyX) {
        if (x == null) return;
        if (x.rect.distanceSquaredTo(target) > championDist) return;

        if (target.distanceSquaredTo(x.p) < championDist) {
            champion = x.p;
            championDist = target.distanceSquaredTo(x.p);
        }
        if (dividebyX) {
            double xlb = 0;
            double xrt = 0;
            if (x.lb != null) {
                xlb = Math.abs(x.p.x() - x.lb.p.x());
            }
            if (x.rt != null) {
                xrt = Math.abs(x.p.x() - x.rt.p.x());
            }

            if (xlb < xrt) {
                nearest(x.lb, target, !dividebyX);
                nearest(x.rt, target, !dividebyX);
            }
            else {
                nearest(x.rt, target, !dividebyX);
                nearest(x.lb, target, !dividebyX);
            }
        }
        else {
            double ylb = 0;
            double yrt = 0;
            if (x.lb != null) {
                ylb = Math.abs(x.p.y() - x.lb.p.y());
            }
            if (x.rt != null) {
                yrt = Math.abs(x.p.y() - x.rt.p.y());
            }

            if (ylb < yrt) {
                nearest(x.lb, target, !dividebyX);
                nearest(x.rt, target, !dividebyX);
            }
            else {
                nearest(x.rt, target, !dividebyX);
                nearest(x.lb, target, !dividebyX);
            }
        }
        
    }

    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        // PointSET brute = new PointSET();
        KdTree tree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);

        }

        StdOut.println("size");
        StdOut.println(tree.size());
        // StdOut.println("contains");
        // StdOut.println(tree.contains(p4));
        Point2D nearest = tree.nearest(new Point2D(0, 0));
        StdOut.println(nearest.toString());
        Point2D nearest2 = tree.nearest(new Point2D(1, 1));
        StdOut.println(nearest2.toString());
        tree.draw();


    }

}
