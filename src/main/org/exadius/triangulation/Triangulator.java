// $Id: Triangulator.java,v 1.1 2005/02/26 17:19:43 pjschwarz Exp $
//
// Copyright (c) 2005 by Gearworks, Inc.  All Rights Reserved.
package org.exadius.triangulation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Comment
 *
 * @author pschwarz
 * @since Feb 23, 2005 10:24:14 AM
 */
public class Triangulator
{
    private static final double NATURAL_LOG_OF_2 = Math.log(2);

    private QueryStructure q = new QueryStructure();

    /**
     * Decomposes the polygon given into a series of triangles.
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
     * @param polygonVertices
     * @return
     */
    public List triangulate(List<Point> polygonVertices)
    {

        int i;
        int nmonpoly, ccount, npoints, genus;
        int n;

        ccount = 0;
        i = 1;


        ArrayList<Segment> segements = initSegements(polygonVertices);

//        while(ccount < contours.size())
//        {
        int j;
        int first, last;

//            npoints = cntr[ccount];
        first = i;
        last = first + polygonVertices.size() - 1;
        for(j = 0; j < polygonVertices.size(); j++, i++)
        {
            segements.get(i).v0 = polygonVertices.get(i);


            if(i == last)
            {
                segements.get(i).next = first;
                segements.get(i).prev = i - 1;
                segements.get(i - 1).v1 = segements.get(i).v0;
            }
            else if(i == first)
            {
                segements.get(i).next = i + 1;
                segements.get(i).prev = last;
                segements.get(last).v1 = segements.get(i).v0;
            }
            else
            {
                segements.get(i).prev = i - 1;
                segements.get(i).next = i + 1;
                segements.get(i - 1).v1 = segements.get(i).v0;
            }

            segements.get(i).is_inserted = true;
        }

//            ccount++;
//        }

        genus = 0; // ncontours - 1;
        n = i - 1;

        constructTrapezoids(segements);
        nmonpoly = monotonate_trapezoids(n);
        return triangulate_monotone_polygons(n, nmonpoly);

    }

    private List triangulate_monotone_polygons(int n, int nmonpoly)
    {
        return null;
    }

    private int monotonate_trapezoids(int n)
    {
        return 0;
    }

    private void constructTrapezoids(List<Segment> segements)
    {
        List<Segment> randomOrderedSegements = new ArrayList<Segment>(segements);
        Collections.shuffle(randomOrderedSegements);

        /* Add the first segment and get the query structure and trapezoid */
        /* list initialised */

        Node root = init_query_structure(randomOrderedSegements.remove(0));

        int nseg = segements.size();
        for(int i = 1; i <= nseg; i++)
        {
            segements.get(i).root0 = segements.get(i).root1 = root;
        }

        for(int h = 1; h <= logStar(nseg); h++)
        {
            for(int i = math_N(nseg, h - 1) + 1; i <= math_N(nseg, h); i++)
            {
                add_segment(randomOrderedSegements.remove(0));
            }

            /* Find a new root for each of the segment endpoints */
            for(int i = 1; i <= nseg; i++)
            {
                find_new_roots(i);
            }
        }

        for(int i = math_N(nseg, logStar(nseg)) + 1; i <= nseg; i++)
        {
            add_segment(randomOrderedSegements.remove(0));
        }

    }

    private static void find_new_roots(int i)
    {

    }

    private void add_segment(Segment segment)
    {
        q.add(segment);
    }

    private Node init_query_structure(Segment segment)
    {
        return q.add(segment);
    }

    /**
     * No idea what this is for.
     *
     * @param n
     * @param h
     * @return
     */
    private static int math_N(int n, int h)
    {
        double v = n;

        for(int i = 0; i < h; i++)
        {
            v = log2(v);
        }

        return (int) Math.ceil(n / v);
    }

    /**
     * Returns <code>log*(n)</code>
     *
     * @param n
     * @return
     */
    private static int logStar(final double n)
    {
        double v = n;
        int i = 0;
        for(; v >= 1; i++)
        {
            v = log2(v);
        }

        return (i - 1);
    }

    private static double log2(double v)
    {
        return Math.log(v) / NATURAL_LOG_OF_2;
    }

    private static ArrayList<Segment> initSegements(List<Point> polygonVertices)
    {
        ArrayList<Segment> segements = new ArrayList<Segment>(polygonVertices.size());
        for(int j = 0; j < polygonVertices.size(); j++)
        {
            segements.add(new Segment());
        }
        return segements;
    }

}
