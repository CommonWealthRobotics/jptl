// $Id: Monotone.java,v 1.2 2005/03/05 17:03:01 pjschwarz Exp $
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
    int vnum;
    int next;			/* Circularly linked list  */
    int prev;			/* describing the monotone */
    int marked;			/* polygon */
}
