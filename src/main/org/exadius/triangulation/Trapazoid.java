// $Id: Trapazoid.java,v 1.1 2005/02/26 17:19:43 pjschwarz Exp $
//
// Copyright (c) 2005 by Gearworks, Inc.  All Rights Reserved.
package org.exadius.triangulation;

import java.awt.*;

/**
 * Represents a Trapazoid for the QueryStructure
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:29:19 AM
 */
public class Trapazoid
{
    public static final int STATE_VALID = 1;
    public static final int STATE_INVALID = 2;

    Segment lseg, rseg;		/* two adjoining segments */
    Point hi, lo;		/* max/min y-values */
    Trapazoid u0, u1;
    Trapazoid d0, d1;
    Node sink;			/* pointer to corresponding in Q */
    int usave, uside;		/* I forgot what this means */
    int state = STATE_VALID;

}
