// $Id: Monotone.java,v 1.1 2005/02/26 17:19:43 pjschwarz Exp $
//
// Copyright (c) 2005 by Gearworks, Inc.  All Rights Reserved.
package org.exadius.triangulation;

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
