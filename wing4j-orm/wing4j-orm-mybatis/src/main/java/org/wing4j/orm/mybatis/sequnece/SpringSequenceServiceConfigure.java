package org.wing4j.orm.mybatis.sequnece;

import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.common.sequence.SequenceService;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * Created by wing4j on 2017/1/8.
 * 基于
 */
public class SpringSequenceServiceConfigure extends AbstractSequenceServiceConfigure implements InitializingBean {
    @Setter
    DataSource dataSource;

    @Override
    public SequenceService init(String tableName, Class clazz) {
        try {
            SequenceService instance = (SequenceService) clazz.newInstance();
            //设置数据源方法
            Method setDataSourceMethod = null;
            Method setAutoCreate = null;
            boolean autoCreate = Boolean.valueOf(parameters.getProperty("autoCreate", "false"));
            try {
                setDataSourceMethod = clazz.getMethod("setDataSource", new Class[]{DataSource.class});
                setAutoCreate = clazz.getMethod("setAutoCreate", new Class[]{Boolean.TYPE});
                setDataSourceMethod.invoke(instance, dataSource);
                setAutoCreate.invoke(instance, autoCreate);
            } catch (NoSuchMethodException e) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("初始化序号服务{}", clazz.getName())
                        .message("不存在设置属性方法")
                        .solution("检查序号服务的实现")
                        .cause(e));
            }
//            //设置ZooKeeper
//            Method setZkUrlMethod = null;
//            Method setZkUsernameMethod = null;
//            Method setZkPasswordMethod = null;
//
//            //设置Redis

            return instance;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(this.mappings == null){
            Assert.notNull(this.mappings, "Property 'mappings' is required");
        }else{
            for (String key : mappings.stringPropertyNames()){
                String key0 = key.toUpperCase();
                String val = mappings.getProperty(key);
                mappings.remove(key);
                mappings.setProperty(key0, val);
            }
        }
        if (this.parameters == null) {
            Assert.notNull(this.parameters, "Property 'parameters' is required");
        }
    }
}
