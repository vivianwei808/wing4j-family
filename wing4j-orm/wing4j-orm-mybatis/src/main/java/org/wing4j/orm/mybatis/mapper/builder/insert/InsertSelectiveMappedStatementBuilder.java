package org.wing4j.orm.mybatis.mapper.builder.insert;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.wing4j.orm.Constants;
import org.wing4j.orm.select.SelectMapper;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractEntityClass;
import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractKeyClass;
import static org.wing4j.orm.entity.utils.KeywordsUtils.convert;

/**
 * Created by wing4j on 2016/12/18.
 * 字段非null插入MS建造器
 */
@Slf4j
public class InsertSelectiveMappedStatementBuilder extends MappedStatementBuilder {
    public InsertSelectiveMappedStatementBuilder(Configuration config, Class mapperClass, WordMode sqlMode, WordMode keywordMode, boolean strictWing4j) {
        super(config, mapperClass.getName(), mapperClass, extractEntityClass(mapperClass, SelectMapper.class), extractKeyClass(mapperClass, SelectMapper.class), sqlMode, keywordMode, strictWing4j);
    }

    @Override
    public MappedStatement build() {
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(entityClass, strictWing4j);
        TextSqlNode insertIntoSqlNode = new TextSqlNode(convert("INSERT INTO ", keywordMode) + convert(tableMetadata.getTableName(), keywordMode) + "(");
        List<SqlNode> heads = new ArrayList<>();
        List<SqlNode> values = new ArrayList<>();
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        for (String column : tableMetadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = fields.get(column);
            heads.add(new IfSqlNode(new TextSqlNode(convert(columnMetadata.getJdbcName(), sqlMode) + ","), columnMetadata.getJavaName() + " != null"));
            String valueExp = "#{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " },";
            values.add(new IfSqlNode(new TextSqlNode(valueExp), columnMetadata.getJavaName() + " != null"));
        }
        MixedSqlNode headsSqlNode = new MixedSqlNode(heads);
        MixedSqlNode valuesSqlNode = new MixedSqlNode(values);
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(insertIntoSqlNode
                , new TrimSqlNode(config, headsSqlNode, "", "", "", ",")//用于去除多余的逗号
                , new TextSqlNode(") ")
                , new TextSqlNode(convert("VALUES", keywordMode))
                , new TextSqlNode(" (")
                , new TrimSqlNode(config, valuesSqlNode, "", "", "", ",")//用于去除多余的逗号
                , new TextSqlNode(")")));

        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.INSERT_SELECTIVE, sqlSource, SqlCommandType.INSERT);
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList<>();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType()));
            parameterMappings.add(builder.build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "BaseParameterMap", entityClass, parameterMappings);
        List<String> primaryKeys = tableMetadata.getPrimaryKeys();
        //存在主键，肯定的
        if (!primaryKeys.isEmpty()) {
            String pkColumonName = primaryKeys.get(0);
            ColumnMetadata primaryKeyMetadata = fields.get(pkColumonName);
            //如果主键是自增整数主键，则使用主键自动生成
            if (primaryKeyMetadata.getAutoIncrement()) {
                log.debug("use Jdbc3KeyGenerator");
                msBuilder.keyColumn(primaryKeyMetadata.getJdbcName());
                msBuilder.keyProperty(primaryKeyMetadata.getJavaName());
                msBuilder.keyGenerator(new Jdbc3KeyGenerator());
            }
        }
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
