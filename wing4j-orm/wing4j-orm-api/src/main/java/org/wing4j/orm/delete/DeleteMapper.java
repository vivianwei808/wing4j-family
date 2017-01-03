package org.wing4j.orm.delete;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface DeleteMapper<T, K> extends
        DeleteAndMapper<T, K>,
        DeleteOrMapper<T, K>,
        DeleteByPrimaryKeyMapper<T, K> {
}
