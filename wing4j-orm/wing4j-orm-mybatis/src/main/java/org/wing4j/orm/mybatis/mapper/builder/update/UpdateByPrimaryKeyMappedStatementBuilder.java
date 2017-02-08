package org.wing4j.orm.mybatis.mapper.builder.update;

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
 * 按照主键更新MS建造器
 */
public class UpdateByPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {

    public UpdateByPrimaryKeyMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, extractEntityClass(mapperClass, SelectMapper.class), extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(entityClass, strictWing4j);
        String primaryKeyName = tableMetadata.getPrimaryKeys().get(0);
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        ColumnMetadata primaryKeyColumn = fields.get(primaryKeyName);
        String update = convert("UPDATE", keywordMode);
        String where = convert("WHERE", keywordMode);
        //headBuilder是前半段
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(update).append(" ");
        headBuilder.append(convert(tableMetadata.getTableName(), sqlMode)).append(" ");

        //footBuilder是后半段
        String primaryKeySql = "#{" + primaryKeyColumn.getJavaName() + ":" + primaryKeyColumn.getJdbcType() + " }";
        StringBuilder footBuilder = new StringBuilder();
        footBuilder.append(where).append(" ");
        footBuilder.append(convert(primaryKeyName, sqlMode)).append(" = ").append(primaryKeySql);
        //生成Set部分
        List<SqlNode> sets = new ArrayList<>();
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList<>();
        for (String column : tableMetadata.getOrderColumns()) {
            //如果是主键，没必要更新
            if (column.equals(primaryKeyName)) {
                continue;
            }
            ColumnMetadata columnMetadata = fields.get(column);
            //生成Set
            String valueSql = "#{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(convert(column, sqlMode))
                    .append(" = ")
                    .append(valueSql)
                    .append(" , ");
            sets.add(new TextSqlNode(sqlBuilder.toString()));
            //创建参数映射
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType()));
            parameterMappings.add(builder.build());
        }
        DynamicSqlSource sqlSource = new DynamicSqlSource(config
                , mixedContents(new TextSqlNode(headBuilder.toString())
                , new TrimSqlNode(config, new MixedSqlNode(sets), convert("SET", keywordMode), "", "", ",")
                , new TextSqlNode(footBuilder.toString())));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.UPDATE_BY_PRIMARY_KEY, sqlSource, SqlCommandType.UPDATE);
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "BaseParameterMap", entityClass, parameterMappings);
        msBuilder.parameterMap(paramBuilder.build());
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
