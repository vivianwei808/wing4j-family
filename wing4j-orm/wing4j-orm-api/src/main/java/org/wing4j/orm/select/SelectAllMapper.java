package org.wing4j.orm.select;

import org.wing4j.orm.Pagination;

import java.util.List;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface SelectAllMapper<T,K> {
    /**
     * 查询表所有记录
     * @return
     */
    List<T> selectAll();
}
