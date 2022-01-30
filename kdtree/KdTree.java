/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 15-jan-2022
 *  Description:Build BST with points in the nodes, using x and y crdinates of the points as keys
 * in strictly alternating sequence
 *  Search and insert
 *  Draw
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    public boolean isEmpty()                      // is the set empty?
    {
        return false;
    }

    public int size()                         // number of points in the set
    {
        return 0;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {

    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        return false;
    }

    public void draw()                         // draw all points to standard draw
    {

    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        return null;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        return null;
    }

    public static void main(String[] args) {

    }

}
