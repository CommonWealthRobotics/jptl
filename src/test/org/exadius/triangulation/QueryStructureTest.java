/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 25, 2005
 * Time: 1:06:52 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/02/26 17:19:43 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 */
package org.exadius.triangulation;

import junit.framework.TestCase;

import java.awt.*;

/**
 * Tests for the query structure.
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
 */
public class QueryStructureTest extends TestCase
{
    public QueryStructureTest(String test)
    {
        super(test);
    }

    public void testInitQueryStructure()
    {
        Segment s = new Segment();
        s.v0  = new Point(1, 2);
        s.v1 = new Point(2, 2);

        QueryStructure q = new QueryStructure();
        q.add(s);

        assertEquals(1, q.size());

        Segment s2 = new Segment();
    }
}
