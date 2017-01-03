package org.wing4j.orm.count;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface CountAndMapper<T,K> {
    /**
     * 根据实体中的非null字段作为条件进行统计
     * @param entity
     * @return
     */
    int countAnd(T entity);
}
