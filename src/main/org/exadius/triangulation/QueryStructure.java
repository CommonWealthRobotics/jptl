/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 25, 2005
 * Time: 1:06:00 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/02/26 17:19:44 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 */
package org.exadius.triangulation;

import java.awt.*;

/**
 * The Trapazoid query structure as discribed in Seidel'91
 * TODO: Need to convert this to use float coordinates.
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
 */
public class QueryStructure
{
    private int segementCount = 0;

    private Node root;
    private static final float C_EPS = 1.0e-7f;

    private static final int FIRSTPT = 1;
    private static final int LASTPT = 2;

    /**
     * Returns the size of the tree, whatever that might mean.
     *
     * @return
     */
    public int size()
    {
        return segementCount;
    }

    public Node add(Segment s)
    {
        segementCount++;
        if (root == null)
        {
            initQueryStructure(s);
            return root;
        }
        else
        {
            addSegment(s);
            return null; // TODO: What to return?
        }

    }

    private void addSegment(Segment s)
    {
        Trapazoid firstTrap = null;
        Trapazoid lastTrap = null;
        int tritop = 0;
        int tribot = 0;
        boolean isSwapped = false;

        if(greaterThan(s.v1,  s.v0))
        {
            Point tmp = s.v0;
            s.v0 = s.v1;
            s.v1 = tmp;

            Node tmpRoot = s.root0;
            s.root0 = s.root1;
            s.root1 = tmpRoot;

            isSwapped = true;
        }

        if((isSwapped) ?
                !inserted(s, LASTPT) :
                !inserted(s, FIRSTPT)) /* insert v0 in the tree */
        {

            firstTrap = createNewBottom(s, true);

        }
        else				/* v0 already present */
        {
            firstTrap = locateEndpoint(s.v0, s.v1, s.root0);
            tritop = 1;
        }

        if ((isSwapped) ? !inserted(s, FIRSTPT) :
                !inserted(s, LASTPT))     /* insert v1 in the tree */
        {
            lastTrap = createNewBottom(s, false);
        }
        else     	/* v1 already present */
        {   /* Get the lowermost intersecting trapezoid */
            lastTrap  = locateEndpoint(s.v1, s.v0, s.root1);
            tribot = 1;
        }

        // Thread the segment into the query tree creating a new X-node
        // First, split all the trapezoids which are intersected by s into
        // two
        Trapazoid t = firstTrap;

        while(t != null &&
                greaterThanEqualTo(t.lo, lastTrap.lo))
        {   /* traverse from top to bottom */

        }
    }

    private Trapazoid createNewBottom(Segment s, boolean leftToRight)
    {
        Trapazoid endPoint;
        if (leftToRight)
        {
            endPoint = locateEndpoint(s.v0, s.v1, s.root0);
        }
        else
        {
            endPoint = locateEndpoint(s.v1, s.v0, s.root1);
        }

        Trapazoid newTrapazoid = new Trapazoid();
        newTrapazoid.hi = endPoint.lo = s.v0;
        endPoint.d0 = newTrapazoid;
        endPoint.d1 = null;
        newTrapazoid.u0 = endPoint;
        endPoint.u1 = null;

        Trapazoid tmp;
        if (((tmp = newTrapazoid.d0) != null) && (tmp.u0 == endPoint))
            tmp.u0 = newTrapazoid;
        if (((tmp = newTrapazoid.d0)!= null) && (tmp.u1 == endPoint))
            tmp.u1 = newTrapazoid;

        if (((tmp = newTrapazoid.d1) != null) && (tmp.u0 == endPoint))
            tmp.u0 = newTrapazoid;
        if (((tmp = newTrapazoid.d1) != null) && (tmp.u1 == endPoint))
            tmp.u1 = newTrapazoid;

        // Now update the query structure and obtain the
        // sinks for the two trapezoids
        Node newYNode = endPoint.sink;
        newYNode.type = Node.TYPE_Y;
        newYNode.yval = s.v0;
        newYNode.segment = s;
        newYNode.left = createSinkNode(newYNode);
        newYNode.right = createSinkNode(newYNode);

        if (leftToRight)
        {
            link(endPoint, newYNode.left);
            link(newTrapazoid, newYNode.right);
        }
        else
        {
            link(endPoint, newYNode.right);
            link(newTrapazoid, newYNode.left);
        }

        return newTrapazoid;
    }

    private void link(Trapazoid trapazoid, Node node)
    {
        node.trapazoid = trapazoid;
        trapazoid.sink = node;
    }

    private Trapazoid locateEndpoint(Point v0, Point v1, Node root0)
    {
        return null;
    }

    private boolean inserted(Segment s, int lastOrFirst)
    {
        return false;
    }

    private boolean greaterThan(Point v1, Point v0)
    {
        return false;
    }

    private void initQueryStructure(Segment s)
    {
        root = new Node(Node.TYPE_Y);
        root.yval = max(s.v0, s.v1);

        root.right = createSinkNode(root);

        Node yNode = new Node(Node.TYPE_Y, root);
        yNode.yval = min(s.v0, s.v1);
        root.left = yNode;

        yNode.left = createSinkNode(yNode);

        Node xNode = new Node(Node.TYPE_X, yNode);
        xNode.segment = s;
        yNode.right = xNode;

        xNode.right = createSinkNode(xNode);
        xNode.left = createSinkNode(xNode);

        Trapazoid middleLeft = new Trapazoid();
        Trapazoid middleRight = new Trapazoid();
        Trapazoid bottomMost = new Trapazoid();
        Trapazoid topMost = new Trapazoid();

        middleLeft.hi = middleRight.hi = topMost.lo = root.yval;
        middleLeft.lo = middleRight.lo = bottomMost.hi = yNode.yval;
        topMost.hi = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);  // TODO: Replace MAX_VALUE with Infinity
        bottomMost.lo = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE); //TODO: Was (double) -1 * (INFINITY);
        middleLeft.rseg = middleRight.lseg = s;
        middleLeft.u0 = middleRight.u0 = topMost;
        middleLeft.d0 = middleRight.d0 = bottomMost;
        topMost.d0 = bottomMost.u0 = middleLeft;
        topMost.d1 = bottomMost.u1 = middleRight;

        middleLeft.sink = xNode.left;
        middleRight.sink = xNode.right;
        bottomMost.sink = yNode.left;
        topMost.sink = root.right;

        root.right.trapazoid = topMost;
        yNode.left.trapazoid = bottomMost;
         xNode.left.trapazoid = middleLeft;
        xNode.right.trapazoid = middleRight;

        s.is_inserted = true;
    }


    private Node createSinkNode(Node parent)
    {
        return new Node(Node.TYPE_SINK, parent);
    }

    /**
     * Computes the y-max of two points
     * @param v0
     * @param v1
     * @return
     */
    private Point max(Point v0, Point v1)
    {
        if (v0.y > v1.y + C_EPS)
        {
            return v0;
        }
        else if (v0.y == v1.y)
        {
            if (v0.x > v1.x + C_EPS)
            {
                return v0;
            }
            else
            {
                return v1;
            }
        }
        else
        {
            return v1;
        }
    }

    /**
     * Computes the y-min of two points
     * @param v0
     * @param v1
     * @return
     */
    private Point min(Point v0, Point v1)
    {
        if (v0.y < v1.y - C_EPS)
        {
            return v0;
        }
        else if (v0.y == v1.y)
        {
            if (v0.x < v1.x)
            {
                return v0;
            }
            else
            {
                return v1;
            }
        }
        else
        {
            return v1;
        }

    }

}
