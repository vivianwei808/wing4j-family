package org.wing4j.orm.mybatis.sequnece;

import lombok.Setter;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.common.sequence.SequenceService;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wing4j on 2017/1/8.
 * 抽象的序号服务配置对象
 */
public abstract class AbstractSequenceServiceConfigure implements SequenceServiceConfigure {
    @Setter
    protected Properties mappings;
    @Setter
    protected Properties parameters;
    protected static Map<String, SequenceService> TABLE_INSTANCES = new ConcurrentHashMap<>();
    @Override
    public SequenceService getSequenceService(String tableName) {
        SequenceService sequenceService = null;
        if ((sequenceService = TABLE_INSTANCES.get(tableName.toUpperCase())) != null) {
            return sequenceService;
        }
        synchronized (this) {
            String clazzName = mappings.getProperty(tableName.toUpperCase());
            if (clazzName == null || clazzName.isEmpty()) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("初始化表{}序号服务", tableName.toUpperCase())
                        .message("获取序号服务发生错误，因为属性'mappings'中没有配置序号服务映射")
                        .solution("在序号服务'mappings'中配置表名->序号服务类的映射"));
            }
            try {
                Class clazz = Class.forName(clazzName);
                sequenceService = init(tableName.toUpperCase(), clazz);
                TABLE_INSTANCES.put(tableName.toUpperCase(), sequenceService);
                return sequenceService;
            } catch (ClassNotFoundException e) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("初始化表{}序号服务", tableName.toUpperCase())
                        .message("初始化序号服务'{}'发生错误有", clazzName)
                        .solution("在系统中引入'{}'所在的组件jar包", clazzName));
            }
        }
    }
}
