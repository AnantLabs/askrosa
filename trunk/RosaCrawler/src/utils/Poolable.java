package utils;

/**
 * interface to store the indexed ftp file info
 * 
 * @author elegate
 * @param <T>
 */
public interface Poolable<T extends Object>
{
    boolean pool(T obj);
}
