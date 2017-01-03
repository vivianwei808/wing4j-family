package org.wing4j.orm.insert;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface InsertAllMapper<T, K> {
    /**
     * 按照输入实体的每一个字段插入，如果为null则插入NULL
     * @param entity
     * @return
     */
    int insert(T entity);
}
