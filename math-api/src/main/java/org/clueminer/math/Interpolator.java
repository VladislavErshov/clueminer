package org.clueminer.math;

import java.util.List;

/**
 *
 * @author Tomas Barton
 */
public interface Interpolator {

    /**
     * Unique identifier for interpolator
     *
     * @return human readable name
     */
    public String getName();

    /**
     * Estimates value on axis Y for given x
     *
     * @param axisX - values on axis X
     * @param axisY - values on axis Y
     * @param x     - point on X (time) axis for which we're trying to find out
     *              value
     * @param lower neighbour on left
     * @param upper neighbour on right
     * @return
     */
    public double getValue(Numeric[] axisX, Numeric[] axisY, double x, int lower, int upper);

    public double getValue(double[] x, double[] y, double z, int lower, int upper);

    public double getValue(Numeric[] x, List<? extends Number> y, double z, int lower, int upper);
}
