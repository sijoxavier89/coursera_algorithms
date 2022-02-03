/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 15-Jan-2022
 *  Description: Represents the set of points in the unit square
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private SET<Point2D> set;

    public PointSET()                               // construct an empty set of points
    {
        set = new SET<Point2D>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return set.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return set.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : set) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();

        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                q.enqueue(p);
            }
        }

        return q;
    }


    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();

        double distance = Double.POSITIVE_INFINITY;

        Point2D nearest = null;
        for (Point2D p1 : set) {
            if (p1.distanceSquaredTo(p) < distance) {
                nearest = p1;
                distance = p1.distanceSquaredTo(p);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);

            brute.insert(p);
        }

        StdOut.println("size");
        StdOut.println(brute.size());
        // StdOut.println("contains");
        // StdOut.println(brute.contains(p4));
        // Point2D nearest = brute.nearest(new Point2D(0.6, 0.1));
        // StdOut.println(nearest.toString());
        Point2D nearest2 = brute.nearest(new Point2D(1, 1));
        StdOut.println(nearest2.toString());
        brute.draw();

    }
}
