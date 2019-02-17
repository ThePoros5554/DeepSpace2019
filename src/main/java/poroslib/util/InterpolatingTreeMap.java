package poroslib.util;

/**
 * Interpolating Tree Maps are used to get values at points that are not defined
 * by making a guess from points that are defined. This uses linear
 * interpolation.
 * 
 * @param <K>
 *            The type of the key (must implement InverseInterpolable)
 * @param <V>
 *            The type of the value (must implement Interpolable)
 */
public class InterpolatingTreeMap<K extends InverseInterpolable<K> & Comparable<K>, V extends Interpolable<V>>
        extends MaxedTreeMap<K, V> 
    {
    private static final long serialVersionUID = 8347275262778054124L;

    public InterpolatingTreeMap()
    {
        super();
    }

    public InterpolatingTreeMap(int maxSlots)
    {
        super(maxSlots);
    }

    /**
     *
     * @param key
     *            Lookup for a value (does not have to exist)
     * @return V or null; V if it is Interpolable or exists, null if it is at a
     *         bound and cannot average
     */
    public V getInterpolated(K key) 
    {
        if (!this.containsKey(key)) 
        {
            /** Get surrounding keys for interpolation */
            K topBound = ceilingKey(key);
            K bottomBound = floorKey(key);

            /**
             * If attempting interpolation at ends of tree, return the nearest
             * data point
             */
            if (topBound == null && bottomBound == null) 
            {
                return null;
            }
            else if (topBound == null) 
            {
                return get(bottomBound);
            }
            else if (bottomBound == null) 
            {
                return get(topBound);
            }

            /** Get surrounding values for interpolation */
            V topElem = get(topBound);
            V bottomElem = get(bottomBound);
            return bottomElem.interpolate(topElem, bottomBound.inverseInterpolate(topBound, key));
        }
        else 
        {
            return get(key);
        }
    }
}