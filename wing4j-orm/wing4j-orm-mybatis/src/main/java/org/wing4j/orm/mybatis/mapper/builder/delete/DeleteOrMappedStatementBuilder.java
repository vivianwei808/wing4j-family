package org.wing4j.orm.mybatis.mapper.builder.delete;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.wing4j.orm.Constants;
import org.wing4j.orm.select.SelectMapper;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractEntityClass;
import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractKeyClass;
import static org.wing4j.orm.entity.utils.KeywordsUtils.convert;

/**
 * Created by wing4j on 2016/12/18.
 * 按照Or条件进行删除MS建造器
 */
public class DeleteOrMappedStatementBuilder extends MappedStatementBuilder {

    public DeleteOrMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, extractEntityClass(mapperClass, SelectMapper.class), extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(entityClass, strictWing4j);
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        String delete = convert("DELETE FROM", keywordMode);
        //headBuilder是前半段
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(delete).append(" ");
        headBuilder.append(convert(tableMetadata.getTableName(), sqlMode)).append(" ");
        //创建结果映射
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        List<SqlNode> wheres = new ArrayList<SqlNode>();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            String whereSql = convert(" OR ", keywordMode) + convert(columnMetadata.getJdbcName(), sqlMode) + " = #{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            SqlNode node = new IfSqlNode(new TextSqlNode(whereSql), MessageFormat.format("{0} != null", columnMetadata.getJavaName()));
            wheres.add(node);
            parameterMappings.add(new ParameterMapping.Builder(config, columnMetadata.getJavaName(), registry.getTypeHandler(keyClass)).build());
        }
        SqlNode whereSqlNode = new org.wing4j.orm.mybatis.mapper.builder.WhereSqlNode(config, new MixedSqlNode(wheres), keywordMode);
        //创建参数映射
        DynamicSqlSource sqlSource = new DynamicSqlSource(config
                , mixedContents(new TextSqlNode(headBuilder.toString())
                , whereSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.DELETE_OR, sqlSource, SqlCommandType.DELETE);
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", entityClass, parameterMappings);
        //创建参数映射
        msBuilder.parameterMap(paramBuilder.build());
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
