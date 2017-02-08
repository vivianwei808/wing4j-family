package org.wing4j.orm.mybatis.mapper.builder;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.wing4j.orm.WordMode;
import org.wing4j.test.TableNameMode;

import java.util.Arrays;

/**
 * Created by wing4j on 2016/12/18.
 */
@ToString
@Data
public abstract class MappedStatementBuilder {
    public MappedStatementBuilder(Configuration config, String namespace, Class mapperClass, Class entityClass, Class keyClass) {
        this.config = config;
        this.namespace = namespace;
        this.mapperClass = mapperClass;
        this.entityClass = entityClass;
        this.keyClass = keyClass;
    }
    /**
     * 数据库模式
     */
    String schema;

    /**
     * 前缀
     */
    String prefix;

    /**
     * 后缀
     */
    String suffix;
    /**
     * 表模式使用
     */
    TableNameMode schemaMode;

    /**
     * 前缀模式
     */
    TableNameMode prefixMode;

    /**
     * 后缀模式
     */
    TableNameMode suffixModed;
    /**
     * 配置对象
     */
    protected Configuration config;
    /**
     * 命名空间
     */
    protected String namespace;

    /**
     * Mapper类对象
     */
    protected  Class mapperClass;
    /**
     * 实体类对象
     */
    protected  Class entityClass;
    /**
     * 主键类对象
     */
    protected Class keyClass;
    /**
     * SQL语句大小写模式
     */
    protected WordMode sqlMode;
    /**
     * 关键字大小写模式
     */
    protected WordMode keywordMode;
    /**
     * 严格Wing4j注解
     */
    protected boolean strictWing4j;
    /**
     * 将多个节点组装成组合节点
     * @param sqlNodes 节点数组
     * @return 组合节点
     */
    protected MixedSqlNode mixedContents(SqlNode... sqlNodes) {
        return new MixedSqlNode(Arrays.asList(sqlNodes));
    }
    /**
     * 构建MapperStatement
     * @return MapperStatement
     */
    public abstract MappedStatement build();
}
