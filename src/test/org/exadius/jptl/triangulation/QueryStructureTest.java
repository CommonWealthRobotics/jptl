/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 25, 2005
 * Time: 1:06:52 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/03/05 17:03:03 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.triangulation;

import junit.framework.TestCase;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the query structure.
 *
 * @author pschwarz
 * @version $Revision: 1.3 $
 */
public class QueryStructureTest extends TestCase
{
    public QueryStructureTest(String test)
    {
        super(test);
    }

    public void testBuildTree()
    {
        List<Point2D> list=  Arrays.asList(new Point2D[]{ new Point2D.Float(3, 4),new Point2D.Float(1, 2), new Point2D.Float(2, 2), new Point2D.Float(4, 4) });

        List<Segment> segments = SegmentBuilder.buildSegmentList(list);
//        Collections.shuffle(segments);
        TrapezoidTree q = new TrapezoidTree();
        q.add(segments.get(0));

        assertEquals(1, q.size());


        for (int i = 1; i < segments.size(); i++)
        {
            Segment segment = (Segment) segments.get(i);
            q.add(segment);
            assertEquals(i + 1, q.size());
        }


        for(Trapezoid t : q.getTrapezoids())
        {
            System.out.println("t = " + t);
        }
    }
}
