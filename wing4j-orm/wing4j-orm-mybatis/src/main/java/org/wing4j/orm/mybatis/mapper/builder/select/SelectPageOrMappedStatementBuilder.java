package org.wing4j.orm.mybatis.mapper.builder.select;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.wing4j.orm.Constants;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;
import org.wing4j.orm.entity.utils.SqlScriptUtils;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;
import org.wing4j.orm.select.SelectMapper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractEntityClass;
import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractKeyClass;
import static org.wing4j.orm.entity.utils.KeywordsUtils.convert;

@Slf4j
public class SelectPageOrMappedStatementBuilder extends MappedStatementBuilder{

    public SelectPageOrMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, extractEntityClass(mapperClass, SelectMapper.class), extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(entityClass, strictWing4j);
        String select = convert("SELECT", keywordMode);
        String from = convert("FROM", keywordMode);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.genreateSqlHead(entityClass, keywordMode, sqlMode, false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(convert(tableMetadata.getTableName(), sqlMode)).append(" ");
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        List<SqlNode> wheres = new ArrayList<SqlNode>();
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            String whereSql = convert(" OR ", keywordMode) + convert(columnMetadata.getJdbcName(), sqlMode) + " = #{entity." + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            SqlNode node = new IfSqlNode(new TextSqlNode(whereSql), MessageFormat.format("entity.{0} != null", columnMetadata.getJavaName()));
            wheres.add(node);
            parameterMappings.add(new ParameterMapping.Builder(config, columnMetadata.getJavaName(), registry.getTypeHandler(keyClass)).build());
        }
        SqlNode whereSqlNode = new org.wing4j.orm.mybatis.mapper.builder.WhereSqlNode(config, new MixedSqlNode(wheres), keywordMode);
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString()), whereSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.SELECT_PAGE_OR, sqlSource, SqlCommandType.SELECT);
        msBuilder = msBuilder.flushCacheRequired(true).useCache(false);
        //创建参数映射
        for (String column: fields.keySet()){
            ColumnMetadata columnMetadata = fields.get(column);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, "entity."+ columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType()));
            parameterMappings.add(builder.build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "PageParameterMap", entityClass, parameterMappings);
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        for (String column: fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ResultMapping.Builder builder = new ResultMapping.Builder(config, columnMetadata.getJavaName(), convert(columnMetadata.getJdbcName(), sqlMode), columnMetadata.getJavaType());
            resultMappings.add(builder.build());
        }
        final ResultMap resultMap = new ResultMap.Builder(config, "PageBaseResultMap", entityClass, resultMappings).build();
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(resultMap);
        msBuilder.resultMaps(resultMaps);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
