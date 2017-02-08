package org.wing4j.orm.entity.utils;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.orm.*;
import org.wing4j.orm.entity.exception.OrmEntityRuntimeException;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.mysql.DataEngineType;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Created by wing4j on 2017/1/7.
 */
@Slf4j
public class Wing4jEntityExtracteUtils {

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
            tableMetadata.setPrefix(tableAnn.prefix());
            tableMetadata.setSuffix(tableAnn.suffix());
        }
        //提取表注释
        if (commentAnn != null && commentAnn.value() != null) {
            tableMetadata.setComment(commentAnn.value());
        }
        //提取数据引擎
        if (dataEngineAnn != null && dataEngineAnn.value() != null) {
            if (dataEngineAnn.value() != DataEngineType.AUTO) {
                tableMetadata.setDataEngine(dataEngineAnn.value().name());
            }
        }
    }


    /**
     * 提取字符串类型元信息
     *
     * @param columnMetadata 元信息
     */
    static void extractFieldString(ColumnMetadata columnMetadata) {
        StringColumn stringColumn = columnMetadata.getColumnField().getAnnotation(StringColumn.class);
        if (stringColumn == null) {
            return;
        }
        Class fieldClass = columnMetadata.getJavaType();
        if (fieldClass != String.class) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                    .message("字段{}数据类型和注解类型支持的映射数据不一致", columnMetadata.getJavaName())
                    .solution("将字段{}的类型从{}修改为{}", columnMetadata.getJavaName(), fieldClass, String.class));
        }
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
                    throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息中{}字段", columnMetadata.getEntityClass(), columnMetadata.getJavaName())
                            .message("字段{}为VARCHAR类型，但是没有指定length", columnMetadata.getJdbcName())
                            .solution("在标注{}注解上的属性{}设置字符串长度", org.wing4j.orm.StringColumn.class, "length"));
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
        NumberColumn numberColumn = columnMetadata.getColumnField().getAnnotation(NumberColumn.class);
        if (numberColumn == null) {
            return;
        }
        Class fieldClass = columnMetadata.getJavaType();
        String dataType = null;
        String jdbcType = null;
        if (fieldClass != BigDecimal.class
                && fieldClass != Integer.class
                && fieldClass != Integer.TYPE
                && fieldClass != Boolean.class
                && fieldClass != Boolean.TYPE
                ) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                    .message("字段{}数据类型和注解类型支持的映射数据不一致", columnMetadata.getJavaName())
                    .solution("将字段{}的类型从{}修改为[{},{},{},{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, BigDecimal.class, Integer.class, Integer.TYPE, Boolean.class, Boolean.TYPE));
        }
        if (numberColumn != null) {
            if (numberColumn.type() != null) {
                if(numberColumn.type() == NumberType.AUTO){
                    if (fieldClass == Integer.TYPE) {
                        dataType = "INTEGER";
                        jdbcType = "NUMERIC";
                    } else if (fieldClass == Integer.class) {
                        dataType = "INTEGER";
                        jdbcType = "NUMERIC";
                        if (numberColumn.nullable()) {
                            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                    .message("字段{}为包装类型，该字段必须设置为非null", columnMetadata.getJavaName())
                                    .solution("nullable属性需要设置为{}", false));
                        }
                    } else if (fieldClass == Boolean.TYPE) {
                        dataType = "TINYINT";
                        jdbcType = "NUMERIC";
                    } else if (fieldClass == Boolean.class) {
                        dataType = "TINYINT";
                        jdbcType = "NUMERIC";
                        if (numberColumn.nullable()) {
                            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                    .message("字段{}为包装类型，该字段必须设置为非null", columnMetadata.getJavaName())
                                    .solution("nullable属性需要设置为{}", false));
                        }
                    }else if(fieldClass == BigDecimal.class){
                        if (numberColumn.scale() > 0 && numberColumn.precision() > 0 && numberColumn.precision() > numberColumn.scale()) {
                            dataType = "DECIMAL(" + numberColumn.precision() + "," + numberColumn.scale() + ")";
                        } else if (numberColumn.precision() > 0 && numberColumn.scale() == 0) {
                            dataType = "DECIMAL(" + numberColumn.precision() + ")";
                        } else if (numberColumn.precision() > numberColumn.scale() && numberColumn.scale() == 0) {
                            dataType = "DECIMAL(18)";
                        } else {
                            dataType = "DECIMAL(18,2)";
                        }
                    }
                }else if (numberColumn.type() == NumberType.INTEGER) {
                    if (fieldClass == Integer.TYPE) {
                        dataType = "INTEGER";
                        jdbcType = "NUMERIC";
                    } else if (fieldClass == Integer.class) {
                        dataType = "INTEGER";
                        jdbcType = "NUMERIC";
                        if (numberColumn.nullable()) {
                            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                    .message("字段{}为包装类型，该字段必须设置为非null", columnMetadata.getJavaName())
                                    .solution("nullable属性需要设置为{}", false));
                        }
                    } else if (fieldClass == Boolean.TYPE) {
                        dataType = "TINYINT";
                        jdbcType = "NUMERIC";
                    } else if (fieldClass == Boolean.class) {
                        dataType = "TINYINT";
                        jdbcType = "NUMERIC";
                        if (numberColumn.nullable()) {
                            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                    .message("字段{}为包装类型，该字段必须设置为非null", columnMetadata.getJavaName())
                                    .solution("nullable属性需要设置为{}", false));
                        }
                    } else {
                        throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                                .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                .message("字段{}为为非整数类型，标注注解为整数型", columnMetadata.getJavaName())
                                .solution("将字段类型修改为[{},{},{},{}]任意一种或者修改注解", Integer.class, Integer.TYPE, Boolean.class, Boolean.TYPE));
                    }
                } else if (numberColumn.type() == NumberType.DECIMAL) {
                    if (fieldClass != BigDecimal.class) {
                        throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                                .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                .message("字段{}为为非小数类型，标注注解为小数型", columnMetadata.getJavaName())
                                .solution("将字段类型修改为{}", BigDecimal.class));
                    }
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
                } else {
                    throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}数据类型没有配置type", columnMetadata.getJavaName())
                            .solution("type属性需要设置值"));
                }
            } else {
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
                } else if (fieldClass == Integer.TYPE) {
                    dataType = "INTEGER";
                    jdbcType = "NUMERIC";
                } else if (fieldClass == Integer.class) {
                    dataType = "INTEGER";
                    jdbcType = "NUMERIC";
                    if (numberColumn.nullable()) {
                        throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                                .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                .message("字段{}为包装类型，该字段必须设置为非null", columnMetadata.getJavaName())
                                .solution("nullable属性需要设置为{}", false));
                    }
                } else if (fieldClass == Boolean.TYPE) {
                    dataType = "TINYINT";
                    jdbcType = "NUMERIC";
                } else if (fieldClass == Boolean.class) {
                    dataType = "TINYINT";
                    jdbcType = "NUMERIC";
                    if (numberColumn.nullable()) {
                        throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                                .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                                .message("字段{}为包装类型，该字段必须设置为非null", columnMetadata.getJavaName())
                                .solution("nullable属性需要设置为{}", false));
                    }
                } else {
                    throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}数据类型不支持{}属性", columnMetadata.getJavaName(), columnMetadata.getJavaType())
                            .solution("将字段{}的类型从{}修改为[{},{},{},{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, BigDecimal.class, Integer.class, Integer.TYPE, Boolean.class, Boolean.TYPE));
                }
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
        DateColumn dateColumn = columnMetadata.getColumnField().getAnnotation(DateColumn.class);
        if (dateColumn == null) {
            return;
        }
        Class fieldClass = columnMetadata.getJavaType();
        if (fieldClass != java.util.Date.class && fieldClass != java.sql.Date.class && fieldClass != java.sql.Time.class && fieldClass != java.sql.Timestamp.class) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                    .message("字段{}数据类型和注解类型支持的映射数据不一致", columnMetadata.getJavaName())
                    .solution("将字段{}的类型从{}修改为[{},{},{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, java.util.Date.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class));
        }
        String dataType = null;
        String jdbcType = null;
        if (dateColumn != null) {
            if (dateColumn.type() == DateType.DATE) {
                if (fieldClass == java.sql.Time.class) {
                    throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}为时间戳数据类型，要求Java数据结构能够提供年月日的精度", columnMetadata.getJavaName())
                            .solution("将字段{}的类型从{}修改为[{},{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, java.util.Date.class, java.sql.Date.class, java.sql.Timestamp.class));

                }
                dataType = "DATE";
                jdbcType = "DATE";
            } else if (dateColumn.type() == DateType.TIME) {
                if (fieldClass == java.sql.Date.class) {
                    throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}为时间戳数据类型，要求Java数据结构能够提供时分秒的精度", columnMetadata.getJavaName())
                            .solution("将字段{}的类型从{}修改为[{},{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, java.util.Date.class, java.sql.Time.class, java.sql.Timestamp.class));

                }
                dataType = "TIME";
                jdbcType = "TIME";
            } else if (dateColumn.type() == DateType.TIMESTAMP) {
                if (fieldClass == java.sql.Date.class || fieldClass == java.sql.Time.class) {
                    throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}为时间戳数据类型，要求Java数据结构能够提供年月日时分秒的精度", columnMetadata.getJavaName())
                            .solution("将字段{}的类型从{}修改为[{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, java.util.Date.class, java.sql.Timestamp.class));

                }
                dataType = "TIMESTAMP";
                jdbcType = "TIMESTAMP";
            } else if (dateColumn.type() == DateType.DATETIME) {
                if (fieldClass == java.sql.Date.class || fieldClass == java.sql.Time.class) {
                    throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}为时间戳数据类型，要求Java数据结构能够提供年月日时分秒的精度", columnMetadata.getJavaName())
                            .solution("将字段{}的类型从{}修改为[{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, java.util.Date.class, java.sql.Timestamp.class));

                }
                dataType = "DATETIME";
                jdbcType = "TIMESTAMP";
            } else if (dateColumn.type() == DateType.AUTO) {
                if (fieldClass == java.sql.Date.class) {
                    dataType = "DATETIME";
                    jdbcType = "TIMESTAMP";
                } else if (fieldClass == java.util.Date.class) {
                    dataType = "DATE";
                    jdbcType = "DATE";
                } else if (fieldClass == java.sql.Time.class) {
                    dataType = "TIME";
                    jdbcType = "TIME";
                } else if (fieldClass == java.sql.Timestamp.class) {
                    dataType = "TIMESTAMP";
                    jdbcType = "TIMESTAMP";
                } else {
                    throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                            .message("字段{}数据类型和注解类型支持的映射数据不一致", columnMetadata.getJavaName())
                            .solution("将字段{}的类型从{}修改为[{},{},{},{}]任意一种", columnMetadata.getJavaName(), fieldClass, java.util.Date.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class));
                }
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
        PrimaryKey primaryKey = columnMetadata.getColumnField().getAnnotation(PrimaryKey.class);
        PrimaryKeyStrategy primaryKeyStrategy = PrimaryKeyStrategy.AUTO;
        if (primaryKey == null) {
            columnMetadata.setPrimaryKeyStrategy(primaryKeyStrategy);
            return;
        }
        if (primaryKey.strategy() == PrimaryKeyStrategy.AUTO) {
            if (columnMetadata.getJavaType() == Integer.class || columnMetadata.getJavaType() == Integer.TYPE) {
                primaryKeyStrategy = PrimaryKeyStrategy.IDENTITY;
            }
        } else if (primaryKey.strategy() == PrimaryKeyStrategy.IDENTITY) {
            if (columnMetadata.getJavaType() != Integer.class && columnMetadata.getJavaType() != Integer.TYPE) {
                throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                        .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                        .message("字段{}使用了自增主键，类型必须为整数", field.getName())
                        .solution("将字段{}的类型从{}修改为{}或者{}", field.getName(), columnMetadata.getJavaType(), Integer.class, "int"));
            }
            primaryKeyStrategy = PrimaryKeyStrategy.IDENTITY;
        } else if (primaryKey.strategy() == PrimaryKeyStrategy.SEQUENCE) {
            if (columnMetadata.getJavaType() != Integer.class && columnMetadata.getJavaType() != Integer.TYPE) {
                throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                        .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                        .message("字段{}使用了自增主键，类型必须为整数", field.getName())
                        .solution("将字段{}的类型从{}修改为{}或者{}", field.getName(), columnMetadata.getJavaType(), Integer.class, "int"));
            }
            columnMetadata.setPrimaryKeyFeature(primaryKey.feature());
            primaryKeyStrategy = PrimaryKeyStrategy.SEQUENCE;
        } else if (primaryKey.strategy() == PrimaryKeyStrategy.UUID) {
            primaryKeyStrategy = PrimaryKeyStrategy.UUID;
        }
        if (columnMetadata.getNullable() != null && columnMetadata.getNullable()) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", columnMetadata.getEntityClass())
                    .message("字段{}是主键，该字段不允许为空", field.getName())
                    .solution("在字段{}将{}属性修改为{}", field.getName(), "nullabe", false));
        }
        columnMetadata.getTableMetadata().getPrimaryKeys().add(columnMetadata.getJdbcName());
        columnMetadata.setPrimaryKeyStrategy(primaryKeyStrategy);

    }


    /**
     * 提字段信息
     *
     * @param columnMetadata 字段元信息
     * @return 是否处理
     */
    static boolean extractField(ColumnMetadata columnMetadata) {
        Field field = columnMetadata.getColumnField();
        Class entityClass = columnMetadata.getEntityClass();
        NumberColumn numberColumn = field.getAnnotation(NumberColumn.class);
        StringColumn stringColumn = field.getAnnotation(StringColumn.class);
        DateColumn dateColumn = field.getAnnotation(DateColumn.class);
        Comment comment = field.getAnnotation(Comment.class);
        if ("schema".equals(columnMetadata.getJavaName())) {
            return false;
        }
        //提取字段注释
        if (comment != null) {
            columnMetadata.setComment(comment.value());
        }
        //统计同一类注解使用几个
        int count = 0;
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
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", entityClass)
                    .message("字段{}未按照约定标注{}或{}或{}注解", field.getName(), org.wing4j.orm.StringColumn.class, NumberColumn.class, DateColumn.class)
                    .solution("在字段{}上标注{}或{}或{}注解", field.getName(), org.wing4j.orm.StringColumn.class, org.wing4j.orm.NumberColumn.class, org.wing4j.orm.DateColumn.class));

        }

        if (count != 1) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", entityClass)
                    .message("字段{}同时标注{}或{}或{}注解的组合", field.getName(), org.wing4j.orm.StringColumn.class, NumberColumn.class, DateColumn.class)
                    .solution("在字段{}上只能标注{}或{}或{}注解中的一种", field.getName(), org.wing4j.orm.StringColumn.class, org.wing4j.orm.NumberColumn.class, org.wing4j.orm.DateColumn.class));

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
