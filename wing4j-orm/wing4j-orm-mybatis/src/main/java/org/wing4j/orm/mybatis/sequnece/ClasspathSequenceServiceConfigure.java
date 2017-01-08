package org.wing4j.orm.mybatis.sequnece;

import lombok.Setter;
import org.wing4j.common.sequence.SequenceService;

import javax.activation.DataSource;
import java.lang.reflect.Method;

/**
 * Created by woate on 2017/1/8.
 */
public class ClasspathSequenceServiceConfigure extends AbstractSequenceServiceConfigure {
    @Setter
    DataSource dataSource;
    @Override
    public SequenceService init(String tableName, Class clazz) {
        try {
            SequenceService instance = (SequenceService) clazz.newInstance();
            //设置数据源方法
            Method setDataSourceMethod = null;
            try {
                setDataSourceMethod = clazz.getMethod("setDataSource", new Class[]{DataSource.class});
                setDataSourceMethod.invoke(instance, dataSource);
            } catch (NoSuchMethodException e) {

            }
        } catch (Exception e) {
        }
        return null;
    }
}
