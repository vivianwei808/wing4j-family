package org.wing4j.orm.insert;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface InsertMapper<T, K> extends
        InsertAllMapper<T, K>,
        InsertSelectiveMapper<T, K> {
}
