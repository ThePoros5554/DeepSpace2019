package poroslib.util;

import java.util.TreeMap;

public class MaxedTreeMap<T, V> extends TreeMap<T, V>
{

    private static final long serialVersionUID = -3850749644811220113L;

    int maxSlots;

    public MaxedTreeMap(int maxSlots) 
    {
        this.maxSlots = maxSlots;
    }

    public MaxedTreeMap() 
    {
        this(0);
    }

    /**
     * Inserts a key value pair, and trims the tree if a max size is specified
     * 
     * @param key   Key for inserted data
     * @param value Value for inserted data
     * @return the value
     */
    @Override
    public V put(T key, V value) 
    {
        if (maxSlots > 0 && maxSlots <= size())
        {
            // "Prune" the tree if it is oversize
            T first = firstKey();
            remove(first);
        }

        super.put(key, value);

        return value;
    }
}