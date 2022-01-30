/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 15-Jan-2022
 *  Description: Represents the set of points in the unit square
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

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
        for (Point2D p : set) StdDraw.point(p.x(), p.y());
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();

        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : set) {
            if (containsIn(rect, p)) {
                q.enqueue(p);
            }
        }

        return q;
    }

    private boolean containsIn(RectHV rect, Point2D p) {

        boolean contains = (p.x() >= rect.xmin()) && (p.y() >= rect.ymin()) && (p.x() <= rect
                .xmax()) && (p.y() <= rect.ymax());
        return contains;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();

        double distance = Double.POSITIVE_INFINITY;

        Point2D nearest = null;
        for (Point2D p1 : set) {
            if (p1.distanceTo(p) < distance) {
                nearest = p;
                distance = p1.distanceTo(p);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}
