// $Id: Monotone.java,v 1.1 2005/02/26 17:19:44 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

/**
 * TODO: Comment
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
