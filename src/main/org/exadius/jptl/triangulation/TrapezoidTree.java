/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 25, 2005
 * Time: 1:06:00 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/02/27 17:50:31 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 */
package org.exadius.jptl.triangulation;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Trapezoid query structure as discribed in Seidel'91
 * Works best with a random ordering of segments.
 * TODO: Need to convert this to use float coordinates.
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
 */
public class TrapezoidTree
{
    private Node root;
    private List<Trapezoid> trapezoids = new ArrayList<Trapezoid>();
    private List<Segment> segments = new ArrayList<Segment>();

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
        return segments.size();
    }

    public void add(Segment s)
    {
        segments.add(s);
        if (root == null)
        {
            initQueryStructure(s);
            s.root0 = s.root1 = root;  // initialize this.
        }
        else
        {
            s.root0 = s.root1 = root;  // this may change after insertion.
            addSegment(s);
        }

    }

    public void updateRoots()
    {
        for (int i = 0; i < segments.size(); i++)
        {
            Segment s = (Segment) segments.get(i);
            if (s.isInserted)
                return;

            Trapezoid t = locateEndpoint(s.v0, s.v1, s.root0);
            s.root0 = t.sink;

            t = locateEndpoint(s.v1, s.v0, s.root1);
            s.root1 = t.sink;
        }
    }

    public Iterator<Trapezoid> iterateTrapazoids()
    {
        return trapezoids.iterator();
    }

    public List<Trapezoid> getTrapezoids()
    {
        return trapezoids;
    }

    private void addSegment(Segment s)
    {
        Trapezoid topmostTrapezoid = null;
        Trapezoid lowermostTrapezoid = null;
        Trapezoid firstLeft = null;
        Trapezoid firstRight = null;
        Trapezoid lastLeft = null;
        Trapezoid lastRight = null;

        boolean tribot = false;
        boolean isSwapped = false;

        if (greaterThan(s.v1, s.v0))
        {
            Point tmp = s.v0;
            s.v0 = s.v1;
            s.v1 = tmp;

            Node tmpRoot = s.root0;
            s.root0 = s.root1;
            s.root1 = tmpRoot;

            isSwapped = true;
        }

        if ((isSwapped) ?
                !inserted(s, LASTPT) :
                !inserted(s, FIRSTPT)) /* insert v0 in the tree */
        {
            topmostTrapezoid = createNewBottom(s, true);
        }
        else				/* v0 already present */
        {
            topmostTrapezoid = locateEndpoint(s.v0, s.v1, s.root0);
        }

        if ((isSwapped) ? !inserted(s, FIRSTPT) :
                !inserted(s, LASTPT))     /* insert v1 in the tree */
        {
            lowermostTrapezoid = createNewBottom(s, false);
        }
        else     	/* v1 already present */
        {   /* Get the lowermost intersecting trapezoid */
            lowermostTrapezoid = locateEndpoint(s.v1, s.v0, s.root1);
            tribot = true;
        }

        // Thread the segment into the query tree creating a new X-node
        // First, split all the trapezoids which are intersected by s into
        // two
        Trapezoid t = topmostTrapezoid;

        while (t != null &&
                greaterThanEqualTo(t.lo, lowermostTrapezoid.lo))
        {   /* traverse from top to bottom */
            Trapezoid save, nSave; // not sure what the difference is.
            Node newXNode = t.sink;
            newXNode.type = Node.TYPE_X;
            newXNode.segment = s;

            newXNode.left = createSinkNode(newXNode);
            newXNode.left.trapezoid = t;

            newXNode.right = createSinkNode(newXNode);
            Trapezoid tn = createTrapezoid();
            newXNode.right.trapezoid = tn;

            if (t == topmostTrapezoid)
                firstRight = tn;
            if (t.lo.equals(lowermostTrapezoid.lo))
                lastRight = tn;

            copyInto(t, tn);
            t.sink = newXNode.left;
            tn.sink = newXNode.right;
            save = t;
            nSave = tn;

            // Sanity check
            if ((t.d0 == null) && (t.d1 == null)) /* case cannot arise */
            {
                throw new RuntimeException("Sanity check with the trapazoid's lower neighbors.");
            }
            // only one trapezoid below. partition t into two and make the
            // two resulting trapezoids t and tn as the upper neighbours of
            // the sole lower trapezoid
            else if (t.d0 != null && t.d1 == null)  // Only one trapezoid below
            {
                if (t.u0 != null && t.u1 != null)   // continuation of a chain from abv.
                {
                    if (t.usave != null)            // three upper neighbours
                    {
                        if (t.uside == Trapezoid.SIDE_LEFT)
                        {
                            tn.u0 = t.u1;
                            t.u1 = null;
                            tn.u1 = t.usave;

                            t.u0.d0 = t;
                            tn.u0.d0 = tn;
                            tn.u1.d0 = tn;
                        }
                        else                        // intersects in the right
                        {
                            tn.u1 = null;
                            tn.u0 = t.u1;
                            t.u1 = t.u0;
                            t.u0 = t.usave;

                            t.u0.d0 = t;
                            t.u1.d0 = t;
                            tn.u0.d0 = tn;
                        }

                        t.usave = tn.usave = null;
                    }
                    else // No usave.... simple case
                    {
                        tn.u0 = t.u1;
                        t.u1 = tn.u1 = null;
                        tn.u0.d0 = tn;
                    }
                }
                else        // fresh seg. or upward cusp
                {
                    Trapezoid tmpU = t.u0;
                    Trapezoid d0 = null;
                    Trapezoid d1 = null;

                    if((d0 = tmpU.d0) != null && (d1 = tmpU.d1) != null)
                    // upward cusp
                    {
                        if (d0.rseg != null && !isleftOf(d0.rseg, s.v1))
                        {
                            t.u0 = t.u1 = tn.u1 = null;
                            tn.u0.d1 = tn;
                        }
                        else // cusp going leftwards
                        {
                            tn.u0 = tn.u1 = t.u1 = null;
                            t.u0.d0 = t;
                        }
                    }
                    else // Fresh segment
                    {
                        t.u0.d0 = t;
                        t.u0.d1 = tn;
                    }
                }
                if (t.lo.equals(lowermostTrapezoid) && tribot)
                {   // Bottom forms a triangle
                    Segment tmpTriSeg = isSwapped ? s.prev : s.next;

                    if (tmpTriSeg != null && isleftOf(tmpTriSeg, s.v0))
                    {
                        // L-R downward cusp
                        t.d0.u0 = t;
                        tn.d0 = tn.d1 = null;
                    }
                    else
                    {
                        // R-L downward cusp
                        tn.d0.u1 = tn;
                        t.d0 = t.d1 = null;
                    }
                }
                else
                {
                    if (t.d0.u0 != null && t.d0.u1 != null)
                    {
                        if (t.d0.u0 == t) // passes thru LHS
                        {
                            t.d0.usave = t.d0.u1;
                            t.d0.uside = Trapezoid.SIDE_LEFT;
                        }
                        else
                        {
                            t.d0.usave = t.d0.u0;
                            t.d0.uside = Trapezoid.SIDE_RIGHT;
                        }
                    }

                    t.d0.u0 = t;
                }

                t = t.d0;
            }
            else if (t.d0 == null && t.d1 != null) // only one trapezoid below
            {
                if(t.u0 != null && t.u1 != null)   // continuation of a chain from abv.
                {
                    if (t.usave != null)            // three upper neighbours
                    {
                        if(t.uside == Trapezoid.SIDE_LEFT)
                        {
                            tn.u0 = t.u1;
                            t.u1 = null;
                            tn.u1 = t.usave;

                            t.u0.d0 = t;
                            tn.u0.d0 = tn;
                            tn.u1.d0 = tn;
                        }
                        else                        // intersects in the right
                        {
                            tn.u1 = null;
                            tn.u0 = t.u1;
                            t.u1 = t.u0;
                            t.u0 = t.usave;

                            t.u0.d0 = t;
                            t.u1.d0 = t;
                            tn.u0.d0 = tn;
                        }

                        t.usave = tn.usave = null;
                    }
                    else // No usave.... simple case
                    {
                        tn.u0 = t.u1;
                        t.u1 = tn.u1 = null;
                        tn.u0.d0 = tn;
                    }
                }
                else    // fresh seg. or upward cusp
                {
                    Trapezoid tmpU = t.u0;
                    Trapezoid d0 = null;
                    Trapezoid d1 = null;

                    if((d0 = tmpU.d0) != null && (d1 = tmpU.d1) != null)
                    // upward cusp
                    {
                        if(d0.rseg != null  && !isleftOf(d0.rseg, s.v1))
                        {
                            t.u0 = t.u1 = tn.u1 = null;
                            tn.u0.d1 = tn;
                        }
                        else // cusp going leftwards
                        {
                            tn.u0 = tn.u1 = t.u1 = null;
                            t.u0.d0 = t;
                        }
                    }
                    else // Fresh segment
                    {
                        t.u0.d0 = t;
                        t.u0.d1 = tn;
                    }
                }

                if(t.lo.equals(lowermostTrapezoid.lo) && tribot)  // bottom form a triangle
                {
                    Segment tmpTriSeg = isSwapped ? s.prev : s.next;

                    if(tmpTriSeg != null && isleftOf(tmpTriSeg, s.v0))
                    {
                        // L-R downward cusp
                        tn.d1.u0 = t;
                        tn.d0 = tn.d1 = null;
                    }
                    else
                    {
                        // R-L downward cusp
                        tn.d1.u1 = tn;
                        t.d0 = t.d1 = null;
                    }
                }
                else
                {
                    if(t.d1.u0 != null && t.d1.u1 != null)
                    {
                        if(t.d1.u0 ==t) // passes thru LHS
                        {
                            t.d1.usave = t.d1.u1;
                            t.d1.uside = Trapezoid.SIDE_LEFT;
                        }
                        else
                        {
                            t.d1.usave = t.d1.u0;
                            t.d1.uside = Trapezoid.SIDE_RIGHT;
                        }
                    }
                    t.d1.u0 = t;
                    t.d1.u1 = tn;
                }

                t = t.d1;
            }

            // two trapezoids below. Find out which one is intersected by
            // this segment and proceed down that one
            else
            {
//                Segment tmpSeg = t.d0.rseg;
                double y0, yt;
                Point tmpPt;

                Trapezoid next = null;
                boolean intersectsD0 = false;
//                boolean isD1 = false;

                if(t.lo.y == s.v0.y) // TODO: FP
                {
                    if(t.lo.x > s.v0.x)
                        intersectsD0 = true;
//                    else
//                        isD1 = true;
                }
                else
                {
                    y0 = t.lo.y;
                    yt = (y0 - s.v0.y)/(s.v1.y - s.v0.y);
                    tmpPt = new Point((int)(s.v0.x + yt * (s.v1.x - s.v0.x)), (int)y0);   // TODO: FP

                    if(lessThan(tmpPt, t.lo))
                        intersectsD0 = true;
//                    else
//                        isD1 = true;
                }
                // check continuity from the top so that the lower-neighbour
                // values are properly filled for the upper trapezoid
                if(t.u0 != null && t.u1 != null)
                {
                    if(t.usave != null)
                    {
                        if(t.uside == Trapezoid.SIDE_LEFT)
                        {
                            tn.u0 = t.u1;
                            t.u1 = null;
                            tn.u1 = t.usave;

                            t.u0.d0 = t;
                            tn.u0.d0 = tn;
                            tn.u1.d0 = tn;
                        }
                        else
                        {
                            tn.u1 = null;
                            tn.u0 = t.u1;
                            t.u1 = t.u0;
                            t.u0 = t.usave;

                            t.u0.d0 = t;
                            t.u1.d0 = t;
                            tn.u0.d0 = tn;
                        }

                        t.usave = tn.usave = null;
                    }
                    else
                    {
                        tn.u0 = t.u1;
                        tn.u1 = null;
                        t.u1 = null;
                        tn.u0.d0 = tn;
                    }
                }
                else
                {
                    Trapezoid tmpU = t.u0;
                    Trapezoid d0 = null;
                    Trapezoid d1 = null;

                    if((d0 = tmpU.d0) != null && (d1 = tmpU.d1) != null)
                    {
                        if(d0.rseg != null && !isleftOf(d0.rseg, s.v1))
                        {
                            t.u0 = t.u1 = tn.u1 = null;
                            tn.u0.d1 = tn;
                        }
                        else
                        {
                            tn.u0 = tn.u1 = tn.u1 = null;
                            t.u0.d0 = t;
                        }
                    }
                    else
                    {
                        t.u0.d0 = t;
                        t.u0.d1 = tn;
                    }
                }

                if (t.lo.equals(lowermostTrapezoid.lo) && tribot)
                {
                   // this case arises only at the lowest trapezoid.. i.e.
                   // tlast, if the lower endpoint of the segment is
                   // already inserted in the structure

                    t.d0.u0 = t;
                    t.d0.u1 = null;
                    t.d1.u0 = tn;
                    t.d1.u1 = null;

                    tn.d0 = t.d1;
                    t.d1 = tn.d1 = null;

                    next = t.d1;
                }
                else if(intersectsD0)
                {
                    t.d0.u0 = t;
                    t.d0.u1 = tn;
                    t.d1.u0 = tn;
                    t.d1.u1 = null;

                    // new code to determine the bottom neighbours of the
                    // newly partitioned trapezoid
                    t.d1 = null;

                    next = t.d0;
                }
                else  // intersects d1
                {
                    t.d0.u0 = t;
                    t.d0.u1 = null;
                    t.d1.u0 = t;
                    t.d1.u1 = tn;

                    // new code to determine the bottom neighbours of the
                    // newly partitioned trapezoid
                    tn.d0 = t.d1;
                    tn.d1 = null;

                    next = t.d1;
                }

                t = next;
            }
            save.rseg = nSave.lseg = s;

        }

        firstLeft = topmostTrapezoid;
        lastLeft = lowermostTrapezoid;

        mergeTrapezoids(s, firstLeft, lastLeft, Trapezoid.SIDE_LEFT);
        mergeTrapezoids(s, firstRight, lastRight, Trapezoid.SIDE_RIGHT);
        s.isInserted = true;
    }

    /**
     * Copies the Trapezoid values from src to dest.
     * TODO: move into a clone() method.
     * @param src
     * @param dest
     */
    private void copyInto(Trapezoid src, Trapezoid dest)
    {
        dest.rseg = src.rseg;
        dest.sink = src.sink;
        dest.u0 = src.u0;
        dest.u1 = src.u1;
        dest.d0 = src.d0;
        dest.d1 = src.d1;
        dest.hi = src.hi;
        dest.lo = src.lo;
    }

    private Trapezoid createNewBottom(Segment s, boolean leftToRight)
    {
        Trapezoid endPoint;
        if (leftToRight)
        {
            endPoint = locateEndpoint(s.v0, s.v1, s.root0);
        }
        else
        {
            endPoint = locateEndpoint(s.v1, s.v0, s.root1);
        }

        Trapezoid newTrapezoid = createTrapezoid();
        newTrapezoid.hi = endPoint.lo = s.v0;
        endPoint.d0 = newTrapezoid;
        endPoint.d1 = null;
        newTrapezoid.u0 = endPoint;
        endPoint.u1 = null;

        Trapezoid tmp;
        if (((tmp = newTrapezoid.d0) != null) && (tmp.u0 == endPoint))
            tmp.u0 = newTrapezoid;
        if (((tmp = newTrapezoid.d0) != null) && (tmp.u1 == endPoint))
            tmp.u1 = newTrapezoid;

        if (((tmp = newTrapezoid.d1) != null) && (tmp.u0 == endPoint))
            tmp.u0 = newTrapezoid;
        if (((tmp = newTrapezoid.d1) != null) && (tmp.u1 == endPoint))
            tmp.u1 = newTrapezoid;

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
            link(newTrapezoid, newYNode.right);
        }
        else
        {
            link(endPoint, newYNode.right);
            link(newTrapezoid, newYNode.left);
        }

        return newTrapezoid;
    }

    private Trapezoid createTrapezoid()
    {
        Trapezoid trapezoid = new Trapezoid();
        trapezoids.add(trapezoid);
        return trapezoid;
    }

    /**
     * Thread in the segment into the existing trapezoidation. The
     * limiting trapezoids are given by tfirst and tlast (which are the
     * trapezoids containing the two endpoints of the segment. Merges all
     * possible trapezoids which flank this segment and have been recently
     * divided because of its insertion
     *
     * @param s
     * @param first
     * @param last
     * @param side
     */
    private void mergeTrapezoids(Segment s, Trapezoid first, Trapezoid last, int side)
    {

        boolean goodNeighbors;
        Trapezoid next;
        Node parentNext;

        /* First merge polys on the LHS */
        Trapezoid t = first;
        while ((t != null) && greaterThanEqualTo(t.lo, last.lo))
        {
            if (side == Trapezoid.SIDE_LEFT)
                goodNeighbors = ((((next = t.d0) != null) && (next.rseg == s)) ||
                        (((next = t.d1) != null) && (next.rseg == s)));
            else
                goodNeighbors = ((((next = t.d0) != null) && (next.lseg == s)) ||
                        (((next = t.d1) != null) && (next.lseg == s)));

            if (goodNeighbors)
            {
                if ((t.lseg == next.lseg) &&
                        (t.rseg == next.rseg)) /* good neighbours */
                {			              /* merge them */
                    /* Use the upper node as the new node i.e. t */

                    parentNext = next.sink.parent;

                    if (parentNext.left == next.sink)
                        parentNext.left = t.sink;
                    else
                        parentNext.right = t.sink;	/* redirect parent */


                    /* Change the upper neighbours of the lower trapezoids */

                    if ((t.d0 = next.d0) != null)
                        if (t.d0.u0 == next)
                            t.d0.u0 = t;
                        else if (t.d0.u1 == next)
                            t.d0.u1 = t;

                    if ((t.d1 = next.d1) != null)
                        if (t.d1.u0 == next)
                            t.d1.u0 = t;
                        else if (t.d1.u1 == next)
                            t.d1.u1 = t;

                    t.lo = next.lo;
                    next.state = Trapezoid.STATE_INVALID; /* invalidate the lower */
                    /* trapezium */
                }
                else		    /* not good neighbours */
                    t = next;
            }
            else		    /* do not satisfy the outer if */
                t = next;

        } /* end-while */

    }

    private void link(Trapezoid trapezoid, Node node)
    {
        node.trapezoid = trapezoid;
        trapezoid.sink = node;
    }

    /**
     * This is query routine which determines which trapezoid does the
     * point v lie in.
     *
     * @param v
     * @param vo
     * @param node
     * @return the trapezoid
     */
    private Trapezoid locateEndpoint(Point v, Point vo, Node node)
    {
        switch (node.type)
        {
            case Node.TYPE_SINK:
                return node.trapezoid;

            case Node.TYPE_Y:
                if (greaterThan(v, node.yval)) /* above */
                    return locateEndpoint(v, vo, node.right);
                else if (v.equals(node.yval)) /* the point is already */
                {			                     /* inserted. */
                    if (greaterThan(vo, node.yval)) /* above */
                        return locateEndpoint(v, vo, node.right);
                    else
                        return locateEndpoint(v, vo, node.left); /* below */
                }
                else
                    return locateEndpoint(v, vo, node.left); /* below */

            case Node.TYPE_X:
                if (v.equals(node.segment.v0) ||
                        v.equals(node.segment.v1))
                {
                    if (v.y == vo.y) /* horizontal segment; TODO: Float */
                    {
                        if (vo.x < v.x)
                            return locateEndpoint(v, vo, node.left); /* left */
                        else
                            return locateEndpoint(v, vo, node.right); /* right */
                    }

                    else if (isleftOf(node.segment, vo))
                        return locateEndpoint(v, vo, node.left); /* left */
                    else
                        return locateEndpoint(v, vo, node.right); /* right */
                }
                else if (isleftOf(node.segment, v))
                    return locateEndpoint(v, vo, node.left); /* left */
                else
                    return locateEndpoint(v, vo, node.right); /* right */

            default:
                throw new RuntimeException("Something is amiss.");
        }
    }

    /**
     * Retun <code>true</code> if the vertex v is to the left of the given line segment.
     * Takes care of the degenerate cases when both the vertices
     * have the same y--cood, etc.
     *
     * @param segment
     * @param vo
     * @return
     */
    private boolean isleftOf(Segment segment, Point vo)
    {
        double area;

        if (greaterThan(segment.v1, segment.v0)) /* seg. going upwards */
        {
            if (segment.v1.y == vo.y)
            {
                if (vo.x < segment.v1.x)
                    area = 1.0;
                else
                    area = -1.0;
            }
            else if (segment.v0.y == vo.y)
            {
                if (vo.x < segment.v0.x)
                    area = 1.0;
                else
                    area = -1.0;
            }
            else
                area = crossProduct(segment.v0, segment.v1, vo);
        }
        else				/* v0 > v1 */
        {
            if (segment.v1.y == vo.y)
            {
                if (vo.x < segment.v1.x)
                    area = 1.0;
                else
                    area = -1.0;
            }
            else if (segment.v0.y == vo.y)
            {
                if (vo.x < segment.v0.x)
                    area = 1.0;
                else
                    area = -1.0;
            }
            else
                area = crossProduct(segment.v1, segment.v0, vo);
        }

        return (area > 0.0);
    }

    private double crossProduct(Point v0, Point v1, Point v2)
    {
        return ((v1).x - (v0).x) * ((v2).y - (v0).y) - ((v1).y - (v0).y) * ((v2).x - (v0).x);
    }

    private boolean inserted(Segment s, int lastOrFirst)
    {
        if (lastOrFirst == FIRSTPT)
            return s.prev.isInserted;
        else
            return s.next.isInserted;
    }

    /**
     * Initilialise the query structure (Q) and the trapezoid table (T)
     * when the first segment is added to start the trapezoidation. The
     * query-tree starts out with 4 trapezoids, one S-node and 2 Y-nodes
     *
     *                4
     *   -----------------------------------
     *  		  \
     *  	1	   \        2
     *  		    \
     *   -----------------------------------
     *                3
     * @param s
     */
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

        Trapezoid middleLeft = createTrapezoid();
        Trapezoid middleRight = createTrapezoid();
        Trapezoid bottomMost = createTrapezoid();
        Trapezoid topMost = createTrapezoid();

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

        root.right.trapezoid = topMost;
        yNode.left.trapezoid = bottomMost;
        xNode.left.trapezoid = middleLeft;
        xNode.right.trapezoid = middleRight;

        s.isInserted = true;
    }

    private Node createSinkNode(Node parent)
    {
        return new Node(Node.TYPE_SINK, parent);
    }

    /**
     * Computes the y-max of two points
     *
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
     *
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

    //
    // TODO:  Replace the following with a Comparable.compareTo call.
    private boolean greaterThan(Point v1, Point v0)
    {
        if (v0.y > v1.y + C_EPS)
            return true;
        else if (v0.y < v1.y - C_EPS)
            return false;
        else
            return (v0.x > v1.x);
    }

    private boolean greaterThanEqualTo(Point p0, Point p1)
    {
        if (p0.y > p1.y + C_EPS)
            return true;
        else if (p0.y < p1.y - C_EPS)
            return false;
        else
            return (p0.x >= p1.x);
    }

    private boolean lessThan(Point v0, Point v1)
    {
        if (v0.y < v1.y - C_EPS)
            return true;
        else if (v0.y > v1.y + C_EPS)
            return false;
        else
            return (v0.x < v1.x);
    }

}
