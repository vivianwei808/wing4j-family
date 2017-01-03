package org.wing4j.orm.select;

import org.wing4j.orm.Pagination;

import java.util.List;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface SelectOrMapper<T,K> {
    /**
     * 按照实体取值查找记录
     * @param entity 实体条件，只能支持=这种查询
     * @return 记录列表
     */
    List<T> selectOr(T entity);
}
