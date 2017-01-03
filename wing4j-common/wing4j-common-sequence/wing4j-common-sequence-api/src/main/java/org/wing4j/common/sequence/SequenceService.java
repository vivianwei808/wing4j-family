package org.wing4j.common.sequence;

/**
 * Created by wing4j on 2016/12/28.
 * 序号服务，用于提供连续自增序列
 */
public interface SequenceService {
    /**
     * 获取下一个序号值
     * @param schema 模式名
     * @param prefix 前缀
     * @param sequenceName 序列名称
     * @param feature 特征，如果固定的则是自增序列，如果是变化特征，则会从0开始自增
     * @return 序号值
     */
    int nextval(String schema, String prefix, String sequenceName, String feature);

    /**
     * 当前值
     * @param schema 模式名
     * @param prefix 前缀
     * @param sequenceName 序列名称
     * @param feature 特征，如果固定的则是自增序列，如果是变化特征，则会从0开始自增
     * @return 序号值
     */
    int curval(String schema, String prefix, String sequenceName, String feature);
}
