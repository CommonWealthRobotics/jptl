// $Id: Triangulator.java,v 1.3 2005/02/27 17:50:32 pjschwarz Exp $
//
// Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
// Licensed under the the BSD License.
package org.exadius.jptl.triangulation;

import org.exadius.jptl.util.MathUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Given a set of vertices, the triangulator will decompose them into a
 * set of triangles.  It uses the following as a reference:
 * <pre>
 * article{Sei91,
 * AUTHOR = "R. Seidel",
 * TITLE = "A simple and Fast Randomized Algorithm for Computing Trapezoidal Decompositions and for Triangulating Polygons",
 * JOURNAL = "Computational Geometry Theory &amp; Applications",
 * PAGES = "51-64",
 * NUMBER = 1,
 * YEAR = 1991,
 * VOLUME = 1 }
 * </pre>
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:24:14 AM
 */
public class Triangulator
{

    private TrapezoidTree q = new TrapezoidTree();

    /**
     * Decomposes the polygon given into a series of triangles.
     *
     * @param polygonVertices
     * @return
     */
    public List triangulate(List<Point> polygonVertices)
    {
        int nmonpoly;
        int n;

        List<Segment> segements = SegmentBuilder.buildSegmentList(polygonVertices);

        n = segements.size();

        constructTrapezoids(segements);
        nmonpoly = monotonateTrapezoids(n);
        return triangulateMonotonePolygons(n, nmonpoly);
    }

    private List triangulateMonotonePolygons(int n, int nmonpoly)
    {
        throw new RuntimeException("Not implemented");
    }

    private int monotonateTrapezoids(int n)
    {
        throw new RuntimeException("Not implemented");
    }

    private void constructTrapezoids(List<Segment> segements)
    {
        List<Segment> randomOrderedSegements = new ArrayList<Segment>(segements);
        Collections.shuffle(randomOrderedSegements);

        // Add the first segment and get the query structure and trapezoid
        // list initialised

        q.add(randomOrderedSegements.remove(0));

        int nseg = segements.size();

        for (int h = 1; h <= MathUtil.logStar(nseg); h++)
        {
            for (int i = MathUtil.logH(nseg, h - 1) + 1; i <= MathUtil.logH(nseg, h); i++)
            {
                q.add(randomOrderedSegements.remove(0));
            }

            q.updateRoots();
        }

        for (int i = MathUtil.logH(nseg, MathUtil.logStar(nseg)) + 1; i <= nseg; i++)
        {
            q.add(randomOrderedSegements.remove(0));
        }

    }

}
