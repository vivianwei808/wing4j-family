package org.wing4j.orm.lock;

import java.util.List;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface LockByForUpdateByPrimaryKeyMapper<T,K> {
    /**
     * 通过物理主键锁定记录
     * @param pk
     * @return
     */
    T lockByForUpdateByPrimaryKey(K pk);
}
