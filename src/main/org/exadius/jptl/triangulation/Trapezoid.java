// $Id: Trapezoid.java,v 1.2 2005/02/27 17:50:31 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import java.awt.*;

/**
 * Represents a Trapezoid for the TrapezoidTree.
 * Todo: rename the variables to something more meaningful
 *
 * @author pschwarz
 * @version $Revision: 1.2 $
 * @since Feb 23, 2005 10:29:19 AM
 */
public class Trapezoid
{
    public static final int STATE_VALID = 1;
    public static final int STATE_INVALID = 2;

    public static final int SIDE_LEFT = 1;
    public static final int SIDE_RIGHT = 2;

    Segment lseg, rseg;		/* two adjoining segments */
    Point hi, lo;		/* max/min y-values */
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
