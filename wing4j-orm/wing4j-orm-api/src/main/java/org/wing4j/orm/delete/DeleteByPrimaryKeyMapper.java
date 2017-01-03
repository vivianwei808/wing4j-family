package org.wing4j.orm.delete;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface DeleteByPrimaryKeyMapper<T,K> {
    /**
     * 根据物理主键进行删除
     * @param pk
     * @return
     */
    int deleteByPrimaryKey(K pk);
}
