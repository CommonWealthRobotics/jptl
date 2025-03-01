// $Id: Segment.java,v 1.5 2005/03/10 19:08:20 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import org.exadius.jptl.util.PointUtil;

import java.awt.geom.Point2D;

/**
 * Represents a segment, (p0, p1).
 *
 * @author pschwarz
 * @version $Revision: 1.5 $
 * @since Feb 23, 2005 10:28:24 AM
 */
public class Segment
{
    Point2D v0, v1;		/* two endpoints */
    boolean isInserted;		/* inserted in trapezoidation yet ? */
    Node root0, root1;		/* root nodes in Q */
    Segment next;			/* Next logical segment */
    Segment prev;			/* Previous segment */



    /**
     * Default
     */
    public Segment()
    {
    }

    /**
     * Constructs a segment with
     * @param v0
     * @param v1
     */
    public Segment(Point2D v0, Point2D v1)
    {
        this.v0 = v0;
        this.v1 = v1;
    }

    /**
     * Retun <code>true</code> if the vertex v is to the left of this line segment.
     * Takes care of the degenerate cases when both the vertices
     * have the same y--cood, etc.

     * @param vo
     * @return
     */
    public boolean isLeftOf(Point2D vo)
    {
        double area;

        if (PointUtil.greaterThan(this.v1, this.v0)) /* seg. going upwards */
        {
            if (Double.compare(this.v1.getY(), vo.getY()) == 0)
            {
                if (vo.getX() < this.v1.getX())
                    area = 1.0;
                else
                    area = -1.0;
            }
            else if (Double.compare(this.v0.getY(), vo.getY()) == 0)
            {
                if (vo.getX() < this.v0.getX())
                    area = 1.0;
                else
                    area = -1.0;
            }
            else
                area = PointUtil.crossProduct(this.v0, this.v1, vo);
        }
        else				/* v0 > v1 */
        {
            if (Double.compare(this.v1.getY(), vo.getY()) == 0)
            {
                if (vo.getX() < this.v1.getX())
                    area = 1.0;
                else
                    area = -1.0;
            }
            else if (Double.compare(this.v0.getY(), vo.getY()) == 0)
            {
                if (vo.getX() < this.v0.getX())
                    area = 1.0;
                else
                    area = -1.0;
            }
            else
                area = PointUtil.crossProduct(this.v1, this.v0, vo);
        }

        return (area > 0.0);

    }

    public String toString()
    {
        return "[" + v0 + ", " + v1 + "]";
    }
}
