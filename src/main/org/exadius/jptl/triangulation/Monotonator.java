/*
 * --Created --
 * Author: pschwarz
 * Date: Mar 5, 2005
 * Time: 11:59:05 AM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/03/10 19:08:18 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.triangulation;

import org.exadius.jptl.util.PointUtil;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Monotonates Trapazoids.
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
 */
public class Monotonator
{
    public List<Monotone> monotonateTrapazoids(List<Trapezoid> trapezoids, List<Segment> segments)
    {
        Trapezoid startingTrapezoid = null;

        List<VertexChain> vertexChains = new ArrayList<VertexChain>();
        Set<Trapezoid> visitedTraps = new HashSet<Trapezoid>();
        List<MonotoneChain> monotoneChains = new ArrayList<MonotoneChain>();
        List<Monotone> monotones = new ArrayList<Monotone>();

        for (int i = 0; i < trapezoids.size(); i++)
        {
            startingTrapezoid = trapezoids.get(i);
            if (startingTrapezoid.insidePolygon())
            {
                break;
            }
        }

        for (Segment segment : segments)
        {
            Monotone monotone = new Monotone();
            monotone.next = segment.next;
            monotone.prev = segment.prev;
            monotone.vnum = segment;
            monotones.add(monotone);

            VertexChain vertexChain = new VertexChain();
            vertexChain.pt = segment.v0;
            vertexChain.vnext[0] = segment.next; /* next vertex */
            vertexChain.vpos[0] = segment;	/* locn. of next vertex */
            vertexChain.nextfree = 1;
            vertexChains.add(vertexChain);

        }

        if (startingTrapezoid.u0 != null)
            traversePolygon(0, startingTrapezoid, startingTrapezoid.u0, true);
        else if (startingTrapezoid.d0 != null)
            traversePolygon(0, startingTrapezoid, startingTrapezoid.d0, false);


        return monotones;
    }

    private void traversePolygon(int i, Trapezoid startingTrapezoid, Trapezoid from, boolean fromUpper)
    {

    }

}
