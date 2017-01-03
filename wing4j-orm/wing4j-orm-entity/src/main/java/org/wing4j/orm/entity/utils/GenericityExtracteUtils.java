package org.wing4j.orm.entity.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型提取工具类
 */
public abstract class GenericityExtracteUtils {
    /**
     * 提取接口声明的实体类对象
     * @param mapperClass 映射类
     * @param targetMapper 目标映射接口
     * @return
     */
    public static Class extractEntityClass(Class mapperClass, Class targetMapper) {
        //获取当前Mapper实现的接口
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            //检查泛型
            if (type instanceof ParameterizedType) {
                //获取泛型
                Class rawType = (Class)((ParameterizedType) type).getRawType();
                //mapper类
                if(targetMapper.isAssignableFrom(rawType)){
                    ParameterizedType target = (ParameterizedType) type;
                    Type[] parameters = target.getActualTypeArguments();
                    Class<?> modelClass = (Class<?>) parameters[0];
                    return modelClass;
                }
            }
        }
        throw new IllegalArgumentException("没有定义实体泛型");
    }

    /**
     * 提取主键类对象
     * @param mapperClass 映射类
     * @param targetMapper 目标映射接口
     * @return
     */
    public static Class extractKeyClass(Class mapperClass, Class targetMapper){
        //获取当前Mapper实现的接口
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            //检查泛型
            if (type instanceof ParameterizedType) {
                //获取泛型
                Class rawType = (Class)((ParameterizedType) type).getRawType();
                //mapper类
                if(targetMapper.isAssignableFrom(rawType)){
                    ParameterizedType target = (ParameterizedType) type;
                    Type[] parameters = target.getActualTypeArguments();
                    Class<?> keyClass = (Class<?>) parameters[1];
                    return keyClass;
                }
            }
        }
        throw new IllegalArgumentException("没有定义主键泛型");
    }
}
