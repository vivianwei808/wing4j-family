package org.wing4j.orm.entity.utils;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.utils.ReflectionUtils;
import org.wing4j.orm.*;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.mysql.DataEngineType;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体信息提取器
 */
@Slf4j
public abstract class EntityExtracteUtils {

    public static final Map<Class, TableMetadata> TABLES_CACHE = new HashMap<Class, TableMetadata>();

    /**
     * 提取实体上的元信息
     *
     * @param entityClass 实体类
     * @return 元信息
     */
    public static TableMetadata extractTable(Class entityClass) {
        //如果缓存包含则直接返回
        if (TABLES_CACHE.containsKey(entityClass)) {
            return TABLES_CACHE.get(entityClass);
        }
        javax.persistence.Table tableAnnJPA = (javax.persistence.Table) entityClass.getAnnotation(javax.persistence.Table.class);
        org.wing4j.orm.Table tableAnnWing4j = (org.wing4j.orm.Table) entityClass.getAnnotation(org.wing4j.orm.Table.class);
        if (tableAnnJPA == null && tableAnnWing4j == null) {
            throw new IllegalArgumentException("实体 " + entityClass.getName() + " 未按照约定标注javax.persistence.Table或者org.wing4j.orm.Table注解");
        }
        if (tableAnnJPA != null && tableAnnWing4j != null) {
            throw new IllegalArgumentException("实体 " + entityClass.getName() + " 同时标注javax.persistence.Table和org.wing4j.orm.Table注解，请至采用一种");
        }
        TableMetadata tableMetadata = TableMetadata.builder().entityClass(entityClass).className(entityClass.getSimpleName()).build();
        //解析JPA注解
        extractTableJPA(tableMetadata);
        //解析wing4j注解
        extractTableWing4j(tableMetadata);
        //提取字段
        extractFields(tableMetadata);
        if (tableMetadata.getPrimaryKeys().isEmpty()) {
            throw new RuntimeException("不允许无物理主键的实体");
        }
        return tableMetadata;
    }

    /**
     * 提取Wing4j提供的注解
     *
     * @param tableMetadata 表元信息
     */
    static void extractTableWing4j(TableMetadata tableMetadata) {
        org.wing4j.orm.Table tableAnn = (org.wing4j.orm.Table) tableMetadata.getEntityClass().getAnnotation(org.wing4j.orm.Table.class);
        org.wing4j.orm.Comment commentAnn = (org.wing4j.orm.Comment) tableMetadata.getEntityClass().getAnnotation(org.wing4j.orm.Comment.class);
        org.wing4j.orm.mysql.DataEngine dataEngineAnn = (org.wing4j.orm.mysql.DataEngine) tableMetadata.getEntityClass().getAnnotation(org.wing4j.orm.mysql.DataEngine.class);
        //提取表名
        if (tableMetadata.getTableName() == null && tableAnn != null && tableAnn.name() != null) {
            tableMetadata.setTableName(tableAnn.name());
            tableMetadata.setSchema(tableAnn.schema());
        }
        //提取表注释
        if (commentAnn != null && commentAnn.value() != null) {
            tableMetadata.setComment(commentAnn.value());
        }
        //提取数据引擎
        if (dataEngineAnn != null && dataEngineAnn.value() != null) {
            if (dataEngineAnn.value() != DataEngineType.Auto) {
                tableMetadata.setDataEngine(dataEngineAnn.value().name());
            }
        }
    }

    /**
     * 提取JPA提供的注解
     *
     * @param tableMetadata 表元信息
     */
    static void extractTableJPA(TableMetadata tableMetadata) {
        javax.persistence.Table tableAnn = (javax.persistence.Table) tableMetadata.getEntityClass().getAnnotation(javax.persistence.Table.class);
        //提取表名
        if (tableAnn != null && tableAnn.name() != null && !tableAnn.name().trim().isEmpty()) {
            tableMetadata.setTableName(tableAnn.name().trim());
            tableMetadata.setSchema(tableAnn.schema());
        }
    }

    /**
     * 提取实体类的所有字段
     *
     * @param tableMetadata 表元信息
     */
    public static void extractFields(TableMetadata tableMetadata) {
        List<Field> fields = ReflectionUtils.getFieldsExcludeTransient(tableMetadata.getEntityClass());
        for (Field field : fields) {
            //任意使用了一个字段注解的
            ColumnMetadata columnMetadata = ColumnMetadata.builder()
                    .tableMetadata(tableMetadata)
                    .entityClass(tableMetadata.getEntityClass())
                    .columnField(field)
                    .javaName(field.getName())
                    .javaType(field.getType())
                    .build();
            //提取该字段元信息
            if(!extractField(columnMetadata)){
                continue;
            }
            KeywordsUtils.vlidateKeyword(columnMetadata);
            //保存有序的字段
            tableMetadata.getOrderColumns().add(columnMetadata.getJdbcName());
            tableMetadata.getColumnMetadatas().put(columnMetadata.getJdbcName(), columnMetadata);
        }
    }

    /**
     * 提取字符串类型元信息
     *
     * @param columnMetadata 元信息
     */
    static void extractFieldString(ColumnMetadata columnMetadata) {
        Column column = columnMetadata.getColumnField().getAnnotation(Column.class);
        StringColumn stringColumn = columnMetadata.getColumnField().getAnnotation(StringColumn.class);
        String dataType = null;
        String jdbcType = null;
        if (stringColumn != null) {
            if (stringColumn.type() == StringType.CHAR) {
                dataType = "CHAR(" + stringColumn.length() + ")";
                jdbcType = "VARCHAR";
            } else if (stringColumn.type() == StringType.VARCHAR) {
                dataType = "VARCHAR(" + stringColumn.length() + ")";
                jdbcType = "VARCHAR";
            } else if (stringColumn.type() == StringType.TEXT) {
                dataType = "TEXT";
                jdbcType = "VARCHAR";
            } else if (stringColumn.type() == StringType.AUTO) {
                if (stringColumn.length() <= 0) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为VARCHAR类型，但是没有指定length");
                } else if (stringColumn.length() > 255) {
                    log.warn("实体[{}]字段[{}]的文本长度超过了256，自动使用TEXT数据类型", columnMetadata.getEntityClass().getName(), columnMetadata.getJdbcName());
                    dataType = "TEXT";
                    jdbcType = "VARCHAR";
                } else {
                    dataType = "VARCHAR(" + stringColumn.length() + ")";
                    jdbcType = "VARCHAR";
                }
            }
            columnMetadata.setDefaultValue(stringColumn.defaultValue());
        } else if (column != null) {
            if (column.length() <= 0) {
                throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为VARCHAR类型，但是没有指定length");
            } else if (column.length() > 255) {
                log.warn("实体[{}]字段[{}]的文本长度超过了256，自动使用TEXT数据类型", columnMetadata.getEntityClass().getName(), columnMetadata.getJdbcName());
                dataType = "TEXT";
                jdbcType = "VARCHAR";
            } else {
                dataType = "VARCHAR(" + column.length() + ")";
                jdbcType = "VARCHAR";
            }
        }
        if (jdbcType != null) {
            columnMetadata.setJdbcType(jdbcType);
        }
        if (dataType != null) {
            columnMetadata.setDataType(dataType);
        }

    }

    /**
     * 提取字段上的数字元信息
     *
     * @param columnMetadata 元信息
     */
    static void extractFieldNumber(ColumnMetadata columnMetadata) {
        Column column = columnMetadata.getColumnField().getAnnotation(Column.class);
        NumberColumn numberColumn = columnMetadata.getColumnField().getAnnotation(NumberColumn.class);
        Class fieldClass = columnMetadata.getJavaType();
        String dataType = null;
        String jdbcType = null;
        if (column != null) {
            if (fieldClass == BigDecimal.class) {
                if (column.scale() > 0 && column.precision() > 0 && column.precision() > column.scale()) {
                    dataType = "DECIMAL(" + column.precision() + "," + column.scale() + ")";
                } else if (column.precision() > 0 && column.scale() == 0) {
                    dataType = "DECIMAL(" + column.precision() + ")";
                } else if (column.precision() > column.scale() && column.scale() == 0) {
                    dataType = "DECIMAL(18)";
                } else {
                    dataType = "DECIMAL(18,2)";
                }
                jdbcType = "DECIMAL";
            } else if (fieldClass == Integer.class) {
                dataType = "INTEGER";
                jdbcType = "NUMERIC";
                if (column.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为整型包装类型，不允许为空");
                }
            } else if (fieldClass == Integer.TYPE) {
                dataType = "INTEGER";
                jdbcType = "NUMERIC";
                if (column.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为整型包装类型，不允许为空");
                }
            } else if (fieldClass == Boolean.TYPE) {
                dataType = "TINYINT";
                jdbcType = "NUMERIC";
                if (column.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为布尔值包装类型，不允许为空");
                }
            } else if (fieldClass == Boolean.class) {
                dataType = "TINYINT";
                jdbcType = "NUMERIC";
                if (column.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为布尔值包装类型，不允许为空");
                }
            }else{
            }
        } else if (numberColumn != null) {
            if (fieldClass == BigDecimal.class) {
                if (numberColumn.scale() > 0 && numberColumn.precision() > 0 && numberColumn.precision() > numberColumn.scale()) {
                    dataType = "DECIMAL(" + numberColumn.precision() + "," + numberColumn.scale() + ")";
                } else if (numberColumn.precision() > 0 && numberColumn.scale() == 0) {
                    dataType = "DECIMAL(" + numberColumn.precision() + ")";
                } else if (numberColumn.precision() > numberColumn.scale() && numberColumn.scale() == 0) {
                    dataType = "DECIMAL(18)";
                } else {
                    dataType = "DECIMAL(18,2)";
                }
                jdbcType = "DECIMAL";
            } else if (fieldClass == Integer.class) {
                dataType = "INTEGER";
                jdbcType = "NUMERIC";
                if (numberColumn.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为整型包装类型，不允许为空");
                }
            } else if (fieldClass == Integer.TYPE) {
                dataType = "INTEGER";
                jdbcType = "NUMERIC";
                if (numberColumn.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为整型包装类型，不允许为空");
                }
            } else if (fieldClass == Boolean.TYPE) {
                dataType = "TINYINT";
                jdbcType = "NUMERIC";
                if (numberColumn.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为布尔值包装类型，不允许为空");
                }
            } else if (fieldClass == Boolean.class) {
                dataType = "TINYINT";
                jdbcType = "NUMERIC";
                if (numberColumn.nullable()) {
                    throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]为布尔值包装类型，不允许为空");
                }
            }else{
                throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]数据类型和注解类型不一致");
            }
            columnMetadata.setDefaultValue(numberColumn.defaultValue());
        }
        if (jdbcType != null) {
            columnMetadata.setJdbcType(jdbcType);
        }
        if (dataType != null) {
            columnMetadata.setDataType(dataType);
        }

    }

    /**
     * 提取字段上的日期元信息
     *
     * @param columnMetadata 元信息
     */
    static void extractFieldDate(ColumnMetadata columnMetadata) {
        Column column = columnMetadata.getColumnField().getAnnotation(Column.class);
        DateColumn dateColumn = columnMetadata.getColumnField().getAnnotation(DateColumn.class);
        Class fieldClass = columnMetadata.getJavaType();
        String dataType = null;
        String jdbcType = null;
        if (column != null) {
            if (fieldClass == java.util.Date.class) {
                Temporal temporal = (Temporal) fieldClass.getAnnotation(Temporal.class);
                //如果没有注解则直接使用DATETIME
                if (temporal == null) {
                    dataType = "DATETIME";
                    jdbcType = "DATE";
                } else {
                    //如果有注解指定数据类型，则使用指定的数据类型
                    if (TemporalType.TIMESTAMP == temporal.value()) {
                        dataType = "TIMESTAMP";
                        jdbcType = "TIMESTAMP";
                    } else if (TemporalType.TIME == temporal.value()) {
                        dataType = "TIME";
                        jdbcType = "TIME";
                    } else {
                        dataType = "DATETIME";
                        jdbcType = "DATE";
                    }
                }
            } else if (fieldClass == java.sql.Timestamp.class) {
                dataType = "TIMESTAMP";
                jdbcType = "TIMESTAMP";
            }else{
            }
        } else if (dateColumn != null) {
            if (dateColumn.type() == DateType.DATE) {
                dataType = "DATETIME";
                jdbcType = "DATE";
            } else if (dateColumn.type() == DateType.TIMESTAMP) {
                dataType = "TIMESTAMP";
                jdbcType = "TIMESTAMP";
            } else if (dateColumn.type() == DateType.TIME) {
                dataType = "TIME";
                jdbcType = "TIME";
            }else{
                throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJdbcName() + "]数据类型和注解类型不一致");
            }
            columnMetadata.setDefaultValue(dateColumn.defaultValue());
        }
        if (jdbcType != null) {
            columnMetadata.setJdbcType(jdbcType);
        }
        if (dataType != null) {
            columnMetadata.setDataType(dataType);
        }
    }

    /**
     * 提取字段上的主键信息
     *
     * @param columnMetadata 元信息
     */
    static void extractFieldPrimaryKey(ColumnMetadata columnMetadata) {
        Field field = columnMetadata.getColumnField();
        GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        Id id = columnMetadata.getColumnField().getAnnotation(Id.class);
        PrimaryKey primaryKey = columnMetadata.getColumnField().getAnnotation(PrimaryKey.class);
        if (id != null && primaryKey != null) {
            throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJavaName() + "]同时使用javax.persistence.Id和org.wing4j.orm.PrimaryKey");
        }
        boolean autoIncrement = false;
        if (generatedValue != null) {
            autoIncrement = generatedValue.strategy() == GenerationType.AUTO || generatedValue.strategy() == GenerationType.IDENTITY;
        }
        if (primaryKey != null) {
            autoIncrement = primaryKey.strategy() == PrimaryKeyStrategy.AUTO || primaryKey.strategy() == PrimaryKeyStrategy.IDENTITY;
        }
        if (autoIncrement) {
            if (columnMetadata.getJavaType() != Integer.class && columnMetadata.getJavaType() != Integer.TYPE) {
                throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJavaName() + "]使用了自增主键，类型必须为整数");
            }
            if (columnMetadata.getNullable()) {
                throw new IllegalArgumentException("实体[" + columnMetadata.getEntityClass().getName() + "]字段[" + columnMetadata.getJavaName() + "]使用了自增主键，该字段不允许为空");
            }
        }
        if (id != null || primaryKey != null) {
            columnMetadata.getTableMetadata().getPrimaryKeys().add(columnMetadata.getJdbcName());
        }
        columnMetadata.setAutoIncrement(autoIncrement);
    }

    /**
     * 提字段信息
     * @param columnMetadata 字段元信息
     * @return 是否处理
     */
    static boolean extractField(ColumnMetadata columnMetadata) {
        Field field = columnMetadata.getColumnField();
        Class entityClass = columnMetadata.getEntityClass();
        Column column = field.getAnnotation(Column.class);
        NumberColumn numberColumn = field.getAnnotation(NumberColumn.class);
        StringColumn stringColumn = field.getAnnotation(StringColumn.class);
        DateColumn dateColumn = field.getAnnotation(DateColumn.class);
        Comment comment = field.getAnnotation(Comment.class);
        if("schema".equals(columnMetadata.getJavaName())){
            return false;
        }
        //提取字段注释
        if (comment != null) {
            columnMetadata.setComment(comment.value());
        }
        //统计同一类注解使用几个
        int count = 0;
        if (column != null) {
            count++;
        }
        if (numberColumn != null) {
            count++;
        }
        if (stringColumn != null) {
            count++;
        }
        if (dateColumn != null) {
            count++;
        }
        if (count == 0) {
            throw new IllegalArgumentException("实体 "
                    + entityClass.getName()
                    + "."
                    + field.getName()
                    + " 未按照约定标注javax.persistence.Column或org.wing4j.orm.StringColumn或org.wing4j.orm.NumberColumn或org.wing4j.orm.DateColumn注解");
        }

        if (count != 1) {
            throw new IllegalArgumentException("实体 "
                    + entityClass.getName()
                    + "."
                    + field.getName()
                    + " 未按照约定标注javax.persistence.Column或org.wing4j.orm.StringColumn或org.wing4j.orm.NumberColumn或org.wing4j.orm.DateColumn注解");
        }
        if (column != null) {
            if (columnMetadata.getJdbcName() == null) {
                columnMetadata.setJdbcName(column.name());
            }
            if (columnMetadata.getNullable() == null) {
                columnMetadata.setNullable(column.nullable());
            }
        }
        if (stringColumn != null) {
            if (columnMetadata.getJdbcName() == null) {
                columnMetadata.setJdbcName(stringColumn.name());
            }
            if (columnMetadata.getNullable() == null) {
                columnMetadata.setNullable(stringColumn.nullable());
            }
        }
        if (numberColumn != null) {
            if (columnMetadata.getJdbcName() == null) {
                columnMetadata.setJdbcName(numberColumn.name());
            }
            if (columnMetadata.getNullable() == null) {
                columnMetadata.setNullable(numberColumn.nullable());
            }
        }
        if (dateColumn != null) {
            if (columnMetadata.getJdbcName() == null) {
                columnMetadata.setJdbcName(dateColumn.name());
            }
            if (columnMetadata.getNullable() == null) {
                columnMetadata.setNullable(dateColumn.nullable());
            }
        }
        extractFieldString(columnMetadata);
        extractFieldNumber(columnMetadata);
        extractFieldDate(columnMetadata);
        extractFieldPrimaryKey(columnMetadata);
        return true;
    }

}
