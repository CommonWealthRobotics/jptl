/*
 * --Created --
 * Author: pschwarz
 * Date: Mar 5, 2005
 * Time: 11:59:05 AM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/05/12 17:25:44 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.triangulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Monotonates Trapazoids.
 *
 * @author pschwarz
 * @version $Revision: 1.3 $
 */
public class Monotonator
{
    Set<Trapezoid> visited;

    public List<Monotone> monotonateTrapazoids(List<Trapezoid> trapezoids, List<Segment> segments)
    {
        Trapezoid startingTrapezoid = null;

        List<VertexChain> vertexChains = new ArrayList<VertexChain>();
        visited = new HashSet<Trapezoid>();
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
            vertexChain.vpos[0] = monotone;	/* locn. of next vertex */
            vertexChain.nextfree = 1;
            vertexChains.add(vertexChain);

        }

        if (startingTrapezoid.u0 != null)
            traversePolygon(monotones.get(0), startingTrapezoid, startingTrapezoid.u0, true);
        else if (startingTrapezoid.d0 != null)
            traversePolygon(monotones.get(0), startingTrapezoid, startingTrapezoid.d0, false);


        return monotones;
    }

    private void traversePolygon(Monotone mcur, Trapezoid t, Trapezoid from, boolean fromUpper)
    {


        if (t == null || visited.contains(t))
        {
            return;
        }

        visited.add(t);


        Segment v0 = null;
        Segment v1 = null;
        // We have much more information available here.
        // rseg: goes upwards
        // lseg: goes downwards

        // Initially assume that dir = TR_FROM_DN (from the left)
        // Switch v0 and v1 if necessary afterwards

        // Special cases for triangles with cusps at the opposited ends.
        // Take care of this first
        if (t.u0 == null && t.u1 == null)
        {
            if (t.d0 != null && t.d1 != null)
            {
                v0 = t.d1.lseg;
                v1 = t.lseg;

                if (from == t.d1)
                {
                    Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                    traversePolygon(mcur, t.d1, t, true);
                    traversePolygon(mnew, t.d0, t, true);
                }
                else
                {
                    Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                    traversePolygon(mcur, t.d0, t, true);
                    traversePolygon(mnew, t.d1, t, true);
                }
            }
            else
            {
                // retval = SP_NOSPLIT;
                traversePolygon(mcur, t.u0, t, false);
                traversePolygon(mcur, t.u1, t, false);
                traversePolygon(mcur, t.d0, t, true);
                traversePolygon(mcur, t.d1, t, true);
            }
        }
        else if(t.d0 == null && t.d1 == null)
        {
            if(t.u0 != null && t.u1 != null)
            {
                v0 = t.rseg;
                v1 = t.u0.rseg;

                if(from == t.u1)
                {
                    Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                    traversePolygon(mcur, t.u1, t, false);
                    traversePolygon(mnew, t.u0, t, false);
                }
                else
                {
                    Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                    traversePolygon(mcur, t.u0, t, false);
                    traversePolygon(mnew, t.u1, t, false);
                }
            }
            else
            {
                // retval = SP_NOSPLIT;
                traversePolygon(mcur, t.u0, t, false);
                traversePolygon(mcur, t.u1, t, false);
                traversePolygon(mcur, t.d0, t, true);
                traversePolygon(mcur, t.d1, t, true);
            }
        }
        else if(t.u0 != null && t.u1 != null)
        {
            if(t.d0 != null && t.d1 != null) // downward and upward cusps
            {
                v0 = t.d1.lseg;
                v1 = t.u0.rseg;

                // retval = SP_2UP_2DN;
                if((!fromUpper  && t.d1 == from)
                        || (fromUpper && t.u1 == from ))
                {
                    Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                    traversePolygon(mcur, t.u1, t, false);
                    traversePolygon(mcur, t.d1, t, true);
                    traversePolygon(mnew, t.u0, t, false);
                    traversePolygon(mnew, t.d0, t, true);
                }
                else
                {
                    Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                    traversePolygon(mcur, t.u0, t, false);
                    traversePolygon(mcur, t.d0, t, true);
                    traversePolygon(mnew, t.u1, t, false);
                    traversePolygon(mnew, t.d1, t, true);
                }
            }
            else // only downward cusp
            {
                if(t.lo.equals(t.lseg.v1))
                {
                    v0 = t.u0.rseg;
                    v1 = t.lseg.next;

                    // retval = SP_2UP_LEFT;
                    if(fromUpper && t.u0 == from)
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                        traversePolygon(mcur, t.u0, t, false);
                        traversePolygon(mnew, t.d0, t, true);
                        traversePolygon(mnew, t.u1, t, false);
                        traversePolygon(mnew, t.d1, t, true);
                    }
                    else
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                        traversePolygon(mcur, t.u1, t, false);
                        traversePolygon(mcur, t.d0, t, true);
                        traversePolygon(mcur, t.d1, t, true);
                        traversePolygon(mnew, t.u0, t, false);
                    }
                }
                else
                {
                    v0 = t.rseg;
                    v1 = t.u0.rseg;
                    // retval = SP_2UP_RIGHT;
	                if(fromUpper && t.u1 == from)
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                        traversePolygon(mcur, t.u1, t, false);
                        traversePolygon(mnew, t.d1, t, true);
                        traversePolygon(mnew, t.d0, t, true);
                        traversePolygon(mnew, t.u0, t, false);
                    }
                    else
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                        traversePolygon(mcur, t.u0, t, false);
                        traversePolygon(mcur, t.d0, t, true);
                        traversePolygon(mcur, t.d1, t, true);
                        traversePolygon(mnew, t.u1, t, false);
                    }
                }
            }
        }
        else if (t.u0 != null || t.u1 != null)  // no downward cusp
        {
            if(t.d0 != null && t.d1 != null)  // only upward cusp
            {
                if(t.hi.equals(t.lseg.v0))
                {
                    v0 = t.d1.lseg;
                    v1 = t.lseg;

                    // retval = SP_2DN_LEFT;
                    if(!(!fromUpper && t.d0 == from) )
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                        traversePolygon(mcur, t.u1, t, false);
                        traversePolygon(mcur, t.d1, t, true);
                        traversePolygon(mcur, t.u0, t, false);
                        traversePolygon(mnew, t.d0, t, true);
                    }
                    else
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                        traversePolygon(mcur, t.d0, t, true);
                        traversePolygon(mnew, t.u0, t, false);
                        traversePolygon(mnew, t.u1, t, false);
                        traversePolygon(mnew, t.d1, t, true);
                    }
                }
                else
                {
                    v0 = t.d1.lseg;
                    v1 = t.rseg.next;

//                    retval = SP_2DN_RIGHT;
                    if(!fromUpper && t.d1 == from)
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                        traversePolygon(mcur, t.d1, t, true);
                        traversePolygon(mnew, t.u1, t, false);
                        traversePolygon(mnew, t.u0, t, false);
                        traversePolygon(mnew, t.d0, t, true);
                    }
                    else
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                        traversePolygon(mcur, t.u0, t, false);
                        traversePolygon(mcur, t.d0, t, true);
                        traversePolygon(mcur, t.u1, t, false);
                        traversePolygon(mnew, t.d1, t, true);
                    }
                }
            }
            else  // no cusp
            {
                if(t.hi.equals(t.lseg.v0) && t.lo.equals(t.rseg.v0))
                {
                    v0 = t.rseg;
                    v1 = t.lseg;
//                    retval = SP_SIMPLE_LRDN;
                    if(fromUpper)
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                        traversePolygon(mcur, t.u0, t, false);
                        traversePolygon(mcur, t.u1, t, false);
                        traversePolygon(mnew, t.d1, t, true);
                        traversePolygon(mnew, t.d0, t, true);
                    }
                    else
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                        traversePolygon(mcur, t.d1, t, true);
                        traversePolygon(mcur, t.d0, t, true);
                        traversePolygon(mnew, t.u0, t, false);
                        traversePolygon(mnew, t.u1, t, false);
                    }
                }
                else if(t.hi.equals(t.rseg.v1) && t.lo.equals(t.lseg.v1))
                {
                    v0 = t.rseg.next;
                    v1 = t.lseg.next;
                    // retval = SP_SIMPLE_LRUP;

                    if(fromUpper)
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v1, v0);
                        traversePolygon(mcur, t.u0, t, false);
                        traversePolygon(mcur, t.u1, t, false);
                        traversePolygon(mnew, t.d1, t, true);
                        traversePolygon(mnew, t.d0, t, true);
                    }
                    else
                    {
                        Monotone mnew = makeNewMonotonePolygon(mcur, v0, v1);
                        traversePolygon(mcur, t.d1, t, true);
                        traversePolygon(mcur, t.d0, t, true);
                        traversePolygon(mnew, t.u0, t, false);
                        traversePolygon(mnew, t.u1, t, false);
                    }
                }
                else // no split possible
                {
                    // retval = SP_NOSPLIT;
                    traversePolygon(mcur, t.u0, t, false);
                    traversePolygon(mcur, t.d0, t, true);
                    traversePolygon(mcur, t.u1, t, false);
                    traversePolygon(mcur, t.d1, t, true);
                }
            }
        }
    }

    /**
     * Constructs a new Monotone, using the current and the given Segments.
     * @param mcur
     * @param v0
     * @param v1
     * @return
     */
    private Monotone makeNewMonotonePolygon(Monotone mcur, Segment v0, Segment v1)
    {
        /*VertexChain vp0;
        VertexChain vp1;

        Monotone mp = vp0.vpos[0];
        Monotone mq = vp1.vpos[0];

        Monotone monotone1 = new Monotone();
        Monotone monotone2 = new Monotone();
        monotone1.vnum = v0;
        monotone2.vnum = v1;



        monotone1.prev = mp.next;
        mp.next.prev = monotone1;
        mon
*/
        return null;
    }


}
