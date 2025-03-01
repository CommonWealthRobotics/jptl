// $Id: Trapezoid.java,v 1.4 2005/03/10 19:08:20 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import org.exadius.jptl.util.PointUtil;

import java.awt.geom.Point2D;

/**
 * Represents a Trapezoid for the TrapezoidTree.
 * Todo: rename the variables to something more meaningful
 *
 * @author pschwarz
 * @version $Revision: 1.4 $
 * @since Feb 23, 2005 10:29:19 AM
 */
public class Trapezoid
{
    public static final int STATE_VALID = 1;
    public static final int STATE_INVALID = 2;

    public static final int SIDE_LEFT = 1;
    public static final int SIDE_RIGHT = 2;

    Segment lseg, rseg;		/* two adjoining segments */
    Point2D hi, lo;		/* max/min y-values */
    Trapezoid u0, u1;   // Upper neighbors.
    Trapezoid d0, d1;   // lower neighbors.
    Node sink;			/* pointer to corresponding in Q */
    Trapezoid usave;
    int uside;		// Side on which the trapezoid is intersected.
    int state = STATE_VALID;

    public boolean insidePolygon()
    {
        if (this.state == Trapezoid.STATE_INVALID)
        {
            return false;
        }

        if (this.rseg == null || this.lseg == null)
        {
            return false;
        }

        if (((this.u0 == null) && (this.u1 == null)) ||
                ((this.d0 == null) && (this.d1 == null))) /* triangle */
        {
            return (PointUtil.greaterThan(this.rseg.v1, this.rseg.v0));
        }

        return false;

    }

    public String toString()
    {
        return "[Trapezoid: hi=" + hi + ", lo=" + lo + "]";
    }

}
