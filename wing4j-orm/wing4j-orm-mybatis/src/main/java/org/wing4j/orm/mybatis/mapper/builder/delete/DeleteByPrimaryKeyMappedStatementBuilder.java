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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractEntityClass;
import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractKeyClass;
import static org.wing4j.orm.entity.utils.KeywordsUtils.convert;

/**
 * Created by wing4j on 2016/12/18.
 * 按照主键删除MS建造器
 */
public class DeleteByPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {

    public DeleteByPrimaryKeyMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, extractEntityClass(mapperClass, SelectMapper.class), extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(entityClass, strictWing4j);
        String primaryKeyName = tableMetadata.getPrimaryKeys().get(0);
        String delete = convert("DELETE FROM", keywordMode);
        String where = convert("WHERE", keywordMode);
        //headBuilder是前半段
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(delete).append(" ");
        headBuilder.append(convert(tableMetadata.getTableName(), sqlMode)).append(" ");

        //footBuilder是后半段
        StringBuilder footBuilder = new StringBuilder();
        footBuilder.append(where).append(" ");
        footBuilder.append(convert(primaryKeyName, sqlMode)).append(" = ?");
        DynamicSqlSource sqlSource = new DynamicSqlSource(config
                , mixedContents(new TextSqlNode(headBuilder.toString())
                , new TextSqlNode(footBuilder.toString())));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.DELETE_BY_PRIMARY_KEY, sqlSource, SqlCommandType.DELETE);
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        parameterMappings.add(new ParameterMapping.Builder(config, primaryKeyName, registry.getTypeHandler(keyClass)).build());
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", entityClass, parameterMappings);
        msBuilder.parameterMap(paramBuilder.build());
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
