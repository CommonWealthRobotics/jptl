// $Id: Segment.java,v 1.1 2005/02/26 17:19:44 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import java.awt.*;

/**
 * TODO: Comment
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
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
    public Segment(Point v0, Point v1)
    {
        this.v0 = v0;
        this.v1 = v1;
    }

    Point v0, v1;		/* two endpoints */
    boolean is_inserted;		/* inserted in trapezoidation yet ? */
    Node root0, root1;		/* root nodes in Q */
    int next;			/* Next logical segment */
    int prev;			/* Previous segment */

}
