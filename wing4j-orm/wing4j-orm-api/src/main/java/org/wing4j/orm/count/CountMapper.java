package org.wing4j.orm.count;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface CountMapper<T, K>extends
        CountAndMapper<T, K>,
        CountOrMapper<T, K>,
        CountAllMapper<T, K> {
}
