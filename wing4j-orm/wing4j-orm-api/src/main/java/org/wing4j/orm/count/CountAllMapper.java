package org.wing4j.orm.count;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface CountAllMapper<T,K> {
    /**
     * 统计表记录数
     * @return
     */
    int countAll();
}
