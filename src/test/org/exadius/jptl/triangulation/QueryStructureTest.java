/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 25, 2005
 * Time: 1:06:52 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/02/27 17:50:32 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.triangulation;

import junit.framework.TestCase;

import java.awt.Point;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;

/**
 * Tests for the query structure.
 *
 * @author pschwarz
 * @version $Revision: 1.2 $
 */
public class QueryStructureTest extends TestCase
{
    public QueryStructureTest(String test)
    {
        super(test);
    }

    public void testInitQueryStructure()
    {
        List<Point> list=  Arrays.asList(new Point[]{new Point(1, 2), new Point(2, 2), new Point(4, 4), new Point(3, 4) });

        List<Segment> segments = SegmentBuilder.buildSegmentList(list);
        Collections.shuffle(segments);
        TrapezoidTree q = new TrapezoidTree();
        q.add(segments.get(0));

        assertEquals(1, q.size());


        for (int i = 1; i < segments.size(); i++)
        {
            Segment segment = (Segment) segments.get(i);
            q.add(segment);
            assertEquals(i, q.size());
        }


        for(Trapezoid t : q.getTrapezoids())
        {
            System.out.println("t = " + t);
        }
    }
}
