/*
 * --Created --
 * Author: pschwarz
 * Date: Feb 26, 2005
 * Time: 7:30:30 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/02/27 17:50:31 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.util;

/**
 * Contains some useful math functions for triangulation.
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
 */
public class MathUtil
{
    private static final double NATURAL_LOG_OF_2 = Math.log(2);

    /**
     * Returns the log<sub>2</sub><sup>h</sup>(n)
     *
     * @param n  the value to apply log<sub>2</sub>
     * @param h  the number of times to apply log<sub>2</sub>
     * @return
     */
    public static int logH(int n, int h)
    {
        double v = n;

        for (int i = 0; i < h; i++)
        {
            v = log2(v);
        }

        return (int) Math.ceil(n / v);
    }

    /**
     * Returns the number of times log<sub>2</sub> can be applied, before <code>log*(n)</code> <= 1
     *
     * @param n the number on which to apply log<sub>2</sub>
     * @return
     */
    public static int logStar(final double n)
    {
        double v = n;
        int i = 0;
        for (; v >= 1; i++)
        {
            v = log2(v);
        }

        return (i - 1);
    }

    /**
     * Performs log<sub>2</sub>(v)
     * @param v the number on which to apply log<sub>2</sub>
     * @return
     */
    public static double log2(double v)
    {
        return Math.log(v) / NATURAL_LOG_OF_2;
    }
}
