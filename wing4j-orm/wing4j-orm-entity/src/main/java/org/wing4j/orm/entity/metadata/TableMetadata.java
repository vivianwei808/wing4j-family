package org.wing4j.orm.entity.metadata;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表元信息
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableMetadata {
    /**
     * 实体类对象
     */
    Class entityClass;
    /**
     * 类名
     */
    String className = "";
    /**
     * 表名
     */
    String tableName = "";
    /**
     * 备注
     */
    String comment = "";
    /**
     * 主键
     */
    final List<String> primaryKeys = new ArrayList<String>();
    /**
     * 字段缓存
     */
    final Map<String, ColumnMetadata> columnMetadatas = new HashMap<String, ColumnMetadata>();
    /**
     * 有序的字段名，和实体上定义的顺序一致
     */
    final List<String> orderColumns = new ArrayList<>();
    /**
     * 使用的数据引擎
     */
    String dataEngine;
    /**
     * 数据库模式
     */
    String schema = "";
    /**
     * 表前缀
     */
    String prefix = "";
    /**
     * 表后缀
     */
    String suffix = "";
}
