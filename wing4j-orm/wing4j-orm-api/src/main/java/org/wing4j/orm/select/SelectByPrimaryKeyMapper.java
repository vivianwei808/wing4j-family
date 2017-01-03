package org.wing4j.orm.select;

import org.wing4j.orm.Pagination;

import java.util.List;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface SelectByPrimaryKeyMapper<T,K> {
    /**
     * 根据物理主键查询
     * @param pk
     * @return
     */
    T selectByPrimaryKey(K pk);
}
