package org.wing4j.orm.mybatis.spring.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.count.CountAllMapper;
import org.wing4j.orm.count.CountAndMapper;
import org.wing4j.orm.count.CountOrMapper;
import org.wing4j.orm.delete.DeleteAndMapper;
import org.wing4j.orm.delete.DeleteByPrimaryKeyMapper;
import org.wing4j.orm.delete.DeleteOrMapper;
import org.wing4j.orm.delete.TruncateMapper;
import org.wing4j.orm.insert.InsertAllMapper;
import org.wing4j.orm.insert.InsertSelectiveMapper;
import org.wing4j.orm.lock.LockByForUpdateAndMapper;
import org.wing4j.orm.lock.LockByForUpdateByPrimaryKeyMapper;
import org.wing4j.orm.lock.LockByForUpdateOrMapper;
import org.wing4j.orm.lock.LockByUpdateSetPrimaryKeyMapper;
import org.wing4j.orm.mybatis.mapper.builder.MappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.count.CountAllMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.count.CountAndMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.count.CountOrMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.delete.DeleteAndMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.delete.DeleteByPrimaryKeyMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.delete.DeleteOrMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.delete.TruncateMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.insert.InsertMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.insert.InsertSelectiveMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.lock.LockByForUpdateAndMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.lock.LockByForUpdateByPrimaryKeyMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.lock.LockByForUpdateOrMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.lock.LockByUpdateSetPrimaryKeyMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.select.*;
import org.wing4j.orm.mybatis.mapper.builder.update.UpdateByPrimaryKeyMappedStatementBuilder;
import org.wing4j.orm.mybatis.mapper.builder.update.UpdateByPrimaryKeySelectiveMappedStatementBuilder;
import org.wing4j.orm.mybatis.sequnece.SequenceServiceConfigure;
import org.wing4j.orm.select.*;
import org.wing4j.orm.update.UpdateByPrimaryKeyMapper;
import org.wing4j.orm.update.UpdateByPrimaryKeySelectiveMapper;
import org.wing4j.test.TableNameMode;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 映射语句注册器
 */
public abstract class MappedStatementRegister {
    static Logger LOGGER = LoggerFactory.getLogger(MappedStatementRegister.class);
    static final Set<MappedStatementBuilder> MAPPER_BUILDER_CACHE = new HashSet<>();
    static final Set<String> HAVE_LOADED_MAPPER_IDS = new HashSet<>();

    /**
     * 重新扫描自动生成的Mapper
     * @param configuration 配置对象
     * @param sqlMode SQL模式
     * @param keywordMode 关键字模式
     */
    public static void rescan(Configuration configuration,
                              WordMode sqlMode,
                              WordMode keywordMode,
                              TableNameMode schemaMode,
                              String schema,
                              TableNameMode prefixMode,
                              String prefix,
                              TableNameMode suffixMode,
                              String suffix) {
        Map<String, MappedStatement> mappedStatements = null;
        try {
            Field mappedStatementsField = Configuration.class.getDeclaredField("mappedStatements");
            mappedStatements = (Map) mappedStatementsField.get(configuration);
        } catch (Exception e) {

        }
        for (String id : HAVE_LOADED_MAPPER_IDS){
            mappedStatements.remove(id);
        }
        for (MappedStatementBuilder builder : MAPPER_BUILDER_CACHE) {
            builder.setKeywordMode(keywordMode);
            builder.setSqlMode(sqlMode);
            builder.setSchemaMode(schemaMode);
            builder.setSchema(schema);
            builder.setPrefixMode(prefixMode);
            builder.setPrefix(prefix);
            builder.setSuffixModed(suffixMode);
            builder.setSuffix(suffix);
            MappedStatement ms = builder.build();
            String id = ms.getId();
            if (configuration.hasStatement(id)) {
                LOGGER.error("Mybatis has existed MappedStatement id:{0},but now override...", id);
            } else {
                configuration.addMappedStatement(ms);
            }
        }
    }

    /**
     * 通过Junit识别运行环境
     * @return
     */
    static boolean isTestEnv() {
        try {
            Class.forName("org.junit.Test");
            return true;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("junit.framework.Test");
                return true;
            } catch (ClassNotFoundException e1) {
                return false;
            }
        }
    }

    /**
     * 扫描DAO接口实现的Mapper接口
     * @param configuration 配置对象
     * @param daoInterface DAO接口
     * @param sqlMode SQL模式
     * @param keywordMode 关键字模式
     * @param strictWing4j 是否严格Wing4j注解
     * @param sequenceConfigure 序号配置对象
     */
    public static void scan(Configuration configuration,
                      Class daoInterface,
                      WordMode sqlMode,
                      WordMode keywordMode,
                      TableNameMode schemaMode,
                      String schema,
                      TableNameMode prefixMode,
                      String prefix,
                      TableNameMode suffixMode,
                      String suffix,
                      boolean strictWing4j,
                      SequenceServiceConfigure sequenceConfigure) {
        List<MappedStatementBuilder> builders = new ArrayList<>();
        //---------------------------新增-----------------------------------------
        if (InsertSelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertSelectiveMappedStatementBuilder(configuration, daoInterface, sequenceConfigure));
        }
        if (InsertAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertMappedStatementBuilder(configuration, daoInterface, sequenceConfigure));
        }
        //---------------------------删除-----------------------------------------
        if (DeleteAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (DeleteOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteOrMappedStatementBuilder(configuration, daoInterface));
        }
        if (DeleteByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (TruncateMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new TruncateMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------修改-----------------------------------------
        if (UpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (UpdateByPrimaryKeySelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeySelectiveMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------查询-----------------------------------------
        if (SelectAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectOrMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAllMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectPageAndMappedStatementBuilder.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectPageAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (SelectPageOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectOrMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------统计-----------------------------------------
        if (CountAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAllMappedStatementBuilder(configuration, daoInterface));
        }
        if (CountAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (CountOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountOrMappedStatementBuilder(configuration, daoInterface));
        }
        //---------------------------加锁-----------------------------------------
        if (LockByForUpdateAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateAndMappedStatementBuilder(configuration, daoInterface));
        }
        if (LockByForUpdateOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateOrMappedStatementBuilder(configuration, daoInterface));
        }
        if (LockByForUpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateByPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        if (LockByUpdateSetPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByUpdateSetPrimaryKeyMappedStatementBuilder(configuration, daoInterface));
        }
        for (MappedStatementBuilder builder : builders) {
            builder.setKeywordMode(keywordMode);
            builder.setSqlMode(sqlMode);
            builder.setSchemaMode(schemaMode);
            builder.setSchema(schema);
            builder.setPrefixMode(prefixMode);
            builder.setPrefix(prefix);
            builder.setSuffixModed(suffixMode);
            builder.setSuffix(suffix);
            builder.setStrictWing4j(strictWing4j);
            MappedStatement ms = builder.build();
            String id = ms.getId();
            if (configuration.hasStatement(id)) {
                LOGGER.error("Mybatis has existed MappedStatement id:{0},but now override...", id);
            } else {
                configuration.addMappedStatement(ms);
                HAVE_LOADED_MAPPER_IDS.add(ms.getId());
                MAPPER_BUILDER_CACHE.add(builder);
            }
        }
    }
}
