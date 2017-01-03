package org.wing4j.orm.delete;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface DeleteAndMapper<T,K> {
    /**
     * 根据实体中的非null字段作为条件进行删除
     * @param entity
     * @return
     */
    int deleteAnd(T entity);
}
