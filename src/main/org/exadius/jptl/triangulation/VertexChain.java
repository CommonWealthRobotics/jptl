// $Id: VertexChain.java,v 1.3 2005/03/10 19:08:20 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import java.awt.geom.Point2D;

/**
 * An element in the Vertex chain.
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:30:08 AM
 */
public class VertexChain
{
    Point2D pt;
    Segment[] vnext = new Segment[4];			/* next vertices for the 4 chains */
    Segment[] vpos = new Segment[4];			/* position of v in the 4 chains */
    int nextfree;
}
