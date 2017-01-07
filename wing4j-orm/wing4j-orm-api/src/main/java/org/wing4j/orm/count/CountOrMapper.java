package org.wing4j.orm.count;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface CountOrMapper<T,K> {
    int countOr(T entity);
}
