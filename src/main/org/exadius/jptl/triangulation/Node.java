// $Id: Node.java,v 1.2 2005/02/26 22:04:34 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

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
    Trapezoid trapezoid;
    Node parent;			/* doubly linked DAG */
    Node left, right;		/* children */
    Segment segment;
}
