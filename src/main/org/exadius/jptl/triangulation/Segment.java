// $Id: Segment.java,v 1.4 2005/03/05 17:03:01 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import java.awt.geom.Point2D;

/**
 * Represents a segment, (p0, p1).
 *
 * @author pschwarz
 * @version $Revision: 1.4 $
 * @since Feb 23, 2005 10:28:24 AM
 */
public class Segment
{
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

    Point2D v0, v1;		/* two endpoints */
    boolean isInserted;		/* inserted in trapezoidation yet ? */
    Node root0, root1;		/* root nodes in Q */
    Segment next;			/* Next logical segment */
    Segment prev;			/* Previous segment */

    public String toString()
    {
        return "[" + v0 + ", " + v1 + "]";
    }
}
