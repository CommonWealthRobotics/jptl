// $Id: TriangulationTest.java,v 1.2 2005/03/05 17:03:03 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import junit.framework.TestCase;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Triangulator.
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:19:41 AM
 */
public class TriangulationTest extends TestCase
{
    public void testTriangulation() throws Exception
    {
        Triangulator triangulator = new Triangulator();
        List<Point2D> polygonVertices = new ArrayList<Point2D>();
        polygonVertices.add(new Point2D.Float(1, 1));
        polygonVertices.add(new Point2D.Float(2, 3));
        polygonVertices.add(new Point2D.Float(4, 4));
        polygonVertices.add(new Point2D.Float(3, 2));

        List triangles = triangulator.triangulate(polygonVertices);

        assertEquals("Incorrect number of triangles", 2, triangles.size());
    }
}
