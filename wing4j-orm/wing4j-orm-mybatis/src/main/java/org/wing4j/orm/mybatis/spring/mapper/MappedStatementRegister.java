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
import org.wing4j.orm.select.*;
import org.wing4j.orm.update.UpdateByPrimaryKeyMapper;
import org.wing4j.orm.update.UpdateByPrimaryKeySelectiveMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 映射语句注册器
 */
public abstract class MappedStatementRegister {
    static Logger LOGGER = LoggerFactory.getLogger(MappedStatementRegister.class);

    /**
     * 扫描DAO接口实现的Mapper接口
     *
     * @param configuration
     * @param daoInterface
     */
    static void scan(Configuration configuration, Class daoInterface, WordMode sqlMode, WordMode keywordMode, boolean strictWing4j) {
        List<MappedStatementBuilder> builders = new ArrayList<>();
        //---------------------------新增-----------------------------------------
        if (InsertSelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertSelectiveMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (InsertAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new InsertMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        //---------------------------删除-----------------------------------------
        if (DeleteAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteAndMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (DeleteOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteOrMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (DeleteByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new DeleteByPrimaryKeyMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (TruncateMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new TruncateMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        //---------------------------修改-----------------------------------------
        if (UpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeyMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (UpdateByPrimaryKeySelectiveMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new UpdateByPrimaryKeySelectiveMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        //---------------------------查询-----------------------------------------
        if (SelectAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAndMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (SelectAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAndMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (SelectOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectOrMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (SelectByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectByPrimaryKeyMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (SelectAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectAllMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (SelectPageAndMappedStatementBuilder.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectPageAndMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (SelectPageOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new SelectOrMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        //---------------------------统计-----------------------------------------
        if (CountAllMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAllMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (CountAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountAndMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (CountOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new CountOrMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        //---------------------------加锁-----------------------------------------
        if (LockByForUpdateAndMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateAndMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (LockByForUpdateOrMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateOrMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (LockByForUpdateByPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByForUpdateByPrimaryKeyMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        if (LockByUpdateSetPrimaryKeyMapper.class.isAssignableFrom(daoInterface)) {
            builders.add(new LockByUpdateSetPrimaryKeyMappedStatementBuilder(configuration, daoInterface, sqlMode, keywordMode, strictWing4j));
        }
        for (MappedStatementBuilder builder : builders) {
            MappedStatement ms = builder.build();
            String id = ms.getId();
            if (configuration.hasStatement(id)) {
                LOGGER.error("Mybatis has existed MappedStatement id:{0},but now override...", id);
            } else {
                configuration.addMappedStatement(ms);
            }
        }
    }
}
