// $Id: Node.java,v 1.1 2005/02/26 17:19:43 pjschwarz Exp $
//
// Copyright (c) 2005 by Gearworks, Inc.  All Rights Reserved.
package org.exadius.triangulation;

import java.awt.*;

/**
 * A query structure node.
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:30:31 AM
 */
class Node
{
    public static final int TYPE_X = 1;
    public static final int TYPE_Y = 2;
    public static final int TYPE_SINK = 3;

    public Node(int type)
    {
        this.type = type;
    }

    public Node(int type, Node parent)
    {
        this.type = type;
        this.parent = parent;
    }


    int type;			/* Y-node or S-node */
    Point yval;
    Trapazoid trapazoid;
    Node parent;			/* doubly linked DAG */
    Node left, right;		/* children */
    Segment segment;
}
