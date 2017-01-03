package org.wing4j.orm.update;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface UpdateMapper<T, K> extends
        UpdateByPrimaryKeyMapper<T, K>,
        UpdateByPrimaryKeySelectiveMapper<T, K>
{
}
