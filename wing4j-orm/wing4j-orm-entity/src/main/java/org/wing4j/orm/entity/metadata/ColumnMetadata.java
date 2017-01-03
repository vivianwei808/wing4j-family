package org.wing4j.orm.entity.metadata;

import lombok.*;

import java.lang.reflect.Field;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColumnMetadata {
    /**
     * 所属表元信息
     */
    TableMetadata tableMetadata;
    /**
     * 实体类对象
     */
    Class entityClass;
    /**
     * 字段对象
     */
    Field columnField;
    /**
     * 字段名称
     */
    String javaName = "";
    /**
     * Java数据类型
     */
    Class javaType;
    /**
     * 数据库字段名
     */
    String jdbcName = "";
    /**
     * 数据库数据类型 包含有长度信息或者精确度信息
     */
    String dataType;
    /**
     * 数据库字段类型 只是数据类型
     */
    String jdbcType;
    /**
     * 是否允许为空
     */
    Boolean nullable = true;
    /**
     * 字段注释
     */
    String comment = "";
    /**
     * 是否允许自动生成主键
     */
    Boolean autoIncrement = false;
    /**
     * 默认值
     */
    String defaultValue = "";

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ColumnMetadata{");
        sb.append("entityClass=").append(entityClass);
        sb.append(", columnField=").append(columnField);
        sb.append(", javaName='").append(javaName).append('\'');
        sb.append(", javaType=").append(javaType);
        sb.append(", jdbcName='").append(jdbcName).append('\'');
        sb.append(", dataType='").append(dataType).append('\'');
        sb.append(", jdbcType='").append(jdbcType).append('\'');
        sb.append(", nullable=").append(nullable);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", autoIncrement=").append(autoIncrement);
        sb.append(", defaultValue='").append(defaultValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}