// $Id: TriangulationTest.java,v 1.1 2005/02/26 17:19:43 pjschwarz Exp $
//
// Copyright (c) 2005 by Gearworks, Inc.  All Rights Reserved.
package org.exadius.triangulation;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * TODO: Comment
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:19:41 AM
 */
public class TriangulationTest extends TestCase
{
    public void testTriangulation() throws Exception
    {
        Triangulator triangulator = new Triangulator();
        List<Point> polygonVertices = new ArrayList<Point>();
        polygonVertices.add(new Point(1, 1));
        polygonVertices.add(new Point(2, 3));
        polygonVertices.add(new Point(4, 4));
        polygonVertices.add(new Point(3, 2));

        List triangles = triangulator.triangulate(polygonVertices);

        assertEquals("Incorrect number of triangles", 2, triangles.size());
    }
}
