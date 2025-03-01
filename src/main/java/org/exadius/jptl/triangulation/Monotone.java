// $Id: Monotone.java,v 1.4 2005/05/12 17:25:44 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

/**
 *  A monotone polygon list holder.
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:29:50 AM
 */
public class Monotone
{
    Segment vnum;
    Segment next;			/* Circularly linked list  */
    Segment prev;			/* describing the monotone */
    int marked;			/* polygon */
}
