/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 25, 2005
 * Time: 1:06:52 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/03/10 19:08:23 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.triangulation;

import junit.framework.TestCase;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * Tests for the query structure.
 *
 * @author pschwarz
 * @version $Revision: 1.4 $
 */
public class QueryStructureTest extends TestCase
{
    public QueryStructureTest(String test)
    {
        super(test);
    }

    public void testBuildTree()
    {
//        List<Point2D> list=  Arrays.asList(new Point2D[]{ new Point2D.Float(3, 4),new Point2D.Float(1, 2), new Point2D.Float(2, 2), new Point2D.Float(4, 4) });
        List<Point2D> list=  Arrays.asList(new Point2D[]{ new Point2D.Float(0.0f, 0.0f),new Point2D.Float(6.0f, 0),
                                                          new Point2D.Float(6.0f, 6.0f), new Point2D.Float(0.0f, 6.0f) });

        List<Segment> segments = SegmentBuilder.buildSegmentList(list);
        Collections.shuffle(segments);
        TrapezoidTree q = new TrapezoidTree();
        q.add(segments.get(0));

        assertEquals(1, q.size());

        for (int i = 1; i < segments.size(); i++)
        {
            Segment segment = (Segment) segments.get(i);
            q.add(segment);
            assertEquals(i + 1, q.size());
        }

        System.out.println(q.getTrapezoids().size() + " trapezoids generated." );
        for(Trapezoid t : q.getTrapezoids())
        {
            System.out.println("t = " + t);
        }
    }
}
