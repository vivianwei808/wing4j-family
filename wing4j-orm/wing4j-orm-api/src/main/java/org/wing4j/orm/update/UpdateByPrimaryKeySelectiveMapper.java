package org.wing4j.orm.update;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface UpdateByPrimaryKeySelectiveMapper<T,K> {
    /**
     * 按照物理主键进行非null字段的更新
     * @param entity
     * @return
     */
    int updateByPrimaryKeySelective(T entity);
}
