package org.wing4j.orm.select;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface SelectMapper<T, K> extends
        SelectAllMapper<T, K>,
        SelectAndMapper<T, K>,
        SelectOrMapper<T, K>,
        SelectByPrimaryKeyMapper<T, K>,
        SelectPageOrMapper<T, K>
{
}
