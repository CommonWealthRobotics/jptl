/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 26, 2005
 * Time: 7:04:53 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/02/27 17:50:31 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.triangulation;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * Given a list of points, it builds a list of Segments.
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
 */
public class SegmentBuilder
{
    /**
     * Takes a given list of polygonVertices and builds a list of Segments.
     * For correct behaviour, the vertices need to be in couter-clockwise order.
     *
     * @param polygonVertices
     * @return
     */
    public static List<Segment> buildSegmentList(List<Point> polygonVertices)
    {
        ArrayList<Segment> segments = initSegements(polygonVertices);

        int i = 0;
        int j;
        int first, last;

        first = i;
        last = first + polygonVertices.size() - 1;
        for (j = 0; j < polygonVertices.size(); j++, i++)
        {
            segments.get(i).v0 = polygonVertices.get(i);

            if (i == last)
            {
                segments.get(i).next = segments.get(first);
                segments.get(i).prev = segments.get(i - 1);
                segments.get(i - 1).v1 = segments.get(i).v0;
            }
            else if (i == first)
            {
                segments.get(i).next = segments.get(i + 1);
                segments.get(i).prev = segments.get(last);
                segments.get(last).v1 = segments.get(i).v0;
            }
            else
            {
                segments.get(i).prev = segments.get(i - 1);
                segments.get(i).next = segments.get(i + 1);
                segments.get(i - 1).v1 = segments.get(i).v0;
            }

            segments.get(i).isInserted = true;
        }

        return segments;
    }

    private static ArrayList<Segment> initSegements(List<Point> polygonVertices)
    {
        ArrayList<Segment> segments = new ArrayList<Segment>(polygonVertices.size());
        for(int j = 0; j < polygonVertices.size(); j++)
        {
            segments.add(new Segment());
        }
        return segments;
    }
}
