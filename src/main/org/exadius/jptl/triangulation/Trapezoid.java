// $Id: Trapezoid.java,v 1.3 2005/03/05 17:03:01 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import java.awt.geom.Point2D;

/**
 * Represents a Trapezoid for the TrapezoidTree.
 * Todo: rename the variables to something more meaningful
 *
 * @author pschwarz
 * @version $Revision: 1.3 $
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

    public String toString()
    {
        return "[Trapezoid: hi=" + hi + ", lo=" + lo + "]";
    }

}
