/*
 * --Created --
 * Author: pschwarz
 * Date: Mar 5, 2005
 * Time: 12:04:43 PM
 *
 * --Last Modified--
 * User: $Author: pjschwarz $
 * When: $Date: 2005/03/10 19:08:23 $
 *
 * Copyright (c) 2005, by Exadius Labs.  All Rights Reserved.
 * Licensed under the the BSD License.
 */
package org.exadius.jptl.util;

import java.awt.geom.Point2D;

/**
 * Contians utility methods for Points.
 *
 * @author pschwarz
 * @version $Revision: 1.1 $
 */
public class PointUtil
{
    private static final float C_EPS = 1.0e-7f;

    /**
     * Computes the y-max of two points
     *
     * @param v0
     * @param v1
     * @return
     */
    public static Point2D max(Point2D v0, Point2D v1)
    {
        if (v0.getY() > v1.getY() + C_EPS)
        {
            return v0;
        }
        else if (Double.compare(v0.getY(), v1.getY()) == 0)
        {
            if (v0.getX() > v1.getX() + C_EPS)
            {
                return v0;
            }
            else
            {
                return v1;
            }
        }
        else
        {
            return v1;
        }
    }

    /**
     * Computes the y-min of two points
     *
     * @param v0
     * @param v1
     * @return
     */
    public static Point2D min(Point2D v0, Point2D v1)
    {
        if (v0.getY() < v1.getY() - C_EPS)
        {
            return v0;
        }
        else if (Double.compare(v0.getY(), v1.getY()) == 0)
        {
            if (v0.getX() < v1.getX())
            {
                return v0;
            }
            else
            {
                return v1;
            }
        }
        else
        {
            return v1;
        }

    }

    //
    // TODO:  Replace the following with a Comparable.compareTo call.
    public static boolean greaterThan(Point2D v1, Point2D v0)
    {
        if (v0.getY() > v1.getY() + C_EPS)
            return true;
        else if (v0.getY() < v1.getY() - C_EPS)
            return false;
        else
            return (v0.getX() > v1.getX());
    }

    public static boolean greaterThanEqualTo(Point2D p0, Point2D p1)
    {
        if (p0.getY() > p1.getY() + C_EPS)
            return true;
        else if (p0.getY() < p1.getY() - C_EPS)
            return false;
        else
            return (p0.getX() >= p1.getX());
    }

    public static boolean lessThan(Point2D v0, Point2D v1)
    {
        if (v0.getY() < v1.getY() - C_EPS)
            return true;
        else if (v0.getY() > v1.getY() + C_EPS)
            return false;
        else
            return (v0.getX() < v1.getX());
    }

    /**
     * Returns the cross product of the vectors (v1, v0) and (v2, v0)
     * TODO:  I'm not sure that this is correctly identified as a cross product.
     * @param v0
     * @param v1
     * @param v2
     * @return
     */
    public static double crossProduct(Point2D v0, Point2D v1, Point2D v2)
    {
        return ((v1).getX() - (v0).getX()) * ((v2).getY() - (v0).getY()) -
                ((v1).getY() - (v0).getY()) * ((v2).getX() - (v0).getX());
    }
}
