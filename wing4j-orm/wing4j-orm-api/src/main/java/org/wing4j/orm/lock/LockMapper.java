package org.wing4j.orm.lock;


/**
 * Created by wing4j on 2016/12/18.
 */
public interface LockMapper<T,K> extends
        LockByForUpdateByPrimaryKeyMapper<T,K>,
        LockByForUpdateOrMapper<T,K>,
        LockByUpdateSetPrimaryKeyMapper<T,K>
{
}
