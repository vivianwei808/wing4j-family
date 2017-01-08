package org.wing4j.orm.mybatis.mapper.builder.insert;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.wing4j.common.utils.ReflectionUtils;
import org.wing4j.common.utils.StringUtils;
import org.wing4j.orm.Constants;
import org.wing4j.orm.PrimaryKeyStrategy;
import org.wing4j.orm.select.SelectMapper;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractEntityClass;
import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractKeyClass;
import static org.wing4j.orm.entity.utils.KeywordsUtils.convert;

/**
 * Created by wing4j on 2016/12/18.
 * 全字段插入MS建造器
 */
@Slf4j
public class InsertMappedStatementBuilder extends MappedStatementBuilder {
    public InsertMappedStatementBuilder(Configuration config, Class mapperClass, WordMode sqlMode, WordMode keywordMode, boolean strictWing4j) {
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
            heads.add(new TextSqlNode(convert(columnMetadata.getJdbcName(), sqlMode) + ","));
            String valueExp = "#{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " },";
            values.add(new TextSqlNode(valueExp));
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
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.INSERT, sqlSource, SqlCommandType.INSERT);
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
            final String pkColumonName = primaryKeys.get(0);
            final ColumnMetadata primaryKeyMetadata = fields.get(pkColumonName);
            //如果主键是自增整数主键，则使用主键自动生成
            if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.IDENTITY) {
                log.debug("use int increment");
                msBuilder.keyColumn(primaryKeyMetadata.getJdbcName());
                msBuilder.keyProperty(primaryKeyMetadata.getJavaName());
                msBuilder.keyGenerator(new Jdbc3KeyGenerator());
            }else if(primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.UUID){
                log.debug("use uuid");
                msBuilder.keyColumn(primaryKeyMetadata.getJdbcName());
                msBuilder.keyProperty(primaryKeyMetadata.getJavaName());
                final Class entityClass1 = this.entityClass;
                msBuilder.keyGenerator(new KeyGenerator() {
                    @Override
                    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
                        try {
                            Method pkm = entityClass1.getMethod("set" + StringUtils.firstCharToUpper(pkColumonName), primaryKeyMetadata.getJavaType());
                            pkm.invoke(parameter, UUID.randomUUID().toString());
                        } catch (Exception e) {
                            //这个异常一般不会发生
                        }
                    }
                    @Override
                    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

                    }
                });
            }
        }
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
