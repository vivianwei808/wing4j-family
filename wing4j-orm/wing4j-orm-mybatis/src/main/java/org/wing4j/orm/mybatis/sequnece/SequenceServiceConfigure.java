package org.wing4j.orm.mybatis.sequnece;

import org.wing4j.common.sequence.SequenceService;

/**
 * Created by wing4j on 2017/1/8.
 * 序号服务配置接口
 */
public interface SequenceServiceConfigure {
    /**
     * 根据表名获取序号服务
     * @param tableName 表名
     * @return 序号服务
     */
    SequenceService getSequenceService(String tableName);

    /**
     * 初始化序号服务的回调方法
     */
    SequenceService init(String tableName, Class clazz);
}
