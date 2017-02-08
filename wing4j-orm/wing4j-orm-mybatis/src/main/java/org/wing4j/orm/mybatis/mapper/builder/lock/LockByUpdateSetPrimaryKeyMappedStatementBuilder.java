package org.wing4j.orm.mybatis.mapper.builder.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.wing4j.orm.Constants;
import org.wing4j.orm.select.SelectMapper;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;

import java.util.Map;

import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractEntityClass;
import static org.wing4j.orm.entity.utils.GenericityExtracteUtils.extractKeyClass;
import static org.wing4j.orm.entity.utils.KeywordsUtils.convert;
/**
 * Created by wing4j on 2016/12/18.
 */
@Slf4j
public class LockByUpdateSetPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {

    public LockByUpdateSetPrimaryKeyMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, extractEntityClass(mapperClass, SelectMapper.class), extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(entityClass, strictWing4j);
        String primaryKeyName = tableMetadata.getPrimaryKeys().get(0);
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        ColumnMetadata primaryKeyColumn = fields.get(primaryKeyName);
        String update = convert("UPDATE", keywordMode);
        //headBuilder是前半段
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(update).append(" ");
        headBuilder.append(convert(tableMetadata.getTableName(), sqlMode)).append(" ");
        headBuilder.append(convert("SET", keywordMode)).append(" ");
        headBuilder.append(convert(primaryKeyColumn.getJdbcName(), sqlMode)).append(" ");
        headBuilder.append("=");
        headBuilder.append(convert(primaryKeyColumn.getJdbcName(), sqlMode)).append(" ");

        //footBuilder是后半段
        String where = convert("WHERE", keywordMode);
        String primaryKeySql = "#{" + primaryKeyColumn.getJavaName() + ":" + primaryKeyColumn.getJdbcType() + " }";
        StringBuilder footBuilder = new StringBuilder();
        footBuilder.append(where).append(" ");
        footBuilder.append(convert(primaryKeyName, sqlMode)).append(" = ").append(primaryKeySql);
        DynamicSqlSource sqlSource = new DynamicSqlSource(config
                , mixedContents(new TextSqlNode(headBuilder.toString())
                , new TextSqlNode(footBuilder.toString())));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.LOCK_BY_UPDATE_SET_PRIMARY_KEY, sqlSource, SqlCommandType.UPDATE);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
