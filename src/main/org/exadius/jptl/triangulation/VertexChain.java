// $Id: VertexChain.java,v 1.1 2005/02/26 17:19:44 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import java.awt.*;

/**
 * TODO: Comment
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:30:08 AM
 */
public class VertexChain
{
    Point pt;
    int[] vnext = new int[4];			/* next vertices for the 4 chains */
    int[] vpos = new int[4];			/* position of v in the 4 chains */
    int nextfree;
}
