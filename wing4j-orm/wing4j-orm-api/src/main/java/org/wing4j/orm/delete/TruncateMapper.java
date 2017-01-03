package org.wing4j.orm.delete;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface TruncateMapper<T,K> {
    /**
     * 清空表
     * @return
     */
    int truncate();
}
