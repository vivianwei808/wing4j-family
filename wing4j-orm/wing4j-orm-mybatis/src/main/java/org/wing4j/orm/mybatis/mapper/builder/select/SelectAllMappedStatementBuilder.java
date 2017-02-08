package org.wing4j.orm.mybatis.mapper.builder.select;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.wing4j.orm.Constants;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;
import org.wing4j.orm.entity.utils.SqlScriptUtils;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;
import org.wing4j.orm.select.SelectAllMapper;
import org.wing4j.orm.select.SelectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractEntityClass;
import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractKeyClass;
import static org.wing4j.orm.entity.utils.KeywordsUtils.convert;

/**
 * Created by wing4j on 2016/12/18.
 * 按照主键查询MS建造器
 */
public class SelectAllMappedStatementBuilder extends MappedStatementBuilder {

    public SelectAllMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, extractEntityClass(mapperClass, SelectMapper.class), extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(entityClass, strictWing4j);
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        String select = convert("SELECT", keywordMode);
        String from = convert("FROM", keywordMode);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.genreateSqlHead(entityClass, keywordMode, sqlMode, false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(convert(tableMetadata.getTableName(), sqlMode)).append(" ");
        StaticSqlSource sqlSource = new StaticSqlSource(config, sqlBuilder.toString());
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.SELECT_ALL, sqlSource, SqlCommandType.SELECT);
        //创建结果映射
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ResultMapping.Builder builder = new ResultMapping.Builder(config, columnMetadata.getJavaName(), columnMetadata.getJdbcName(), columnMetadata.getJavaType());
            resultMappings.add(builder.build());
        }
        final ResultMap resultMap = new ResultMap.Builder(config, "BaseResultMap", entityClass, resultMappings).build();
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(resultMap);
        msBuilder.resultMaps(resultMaps);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
