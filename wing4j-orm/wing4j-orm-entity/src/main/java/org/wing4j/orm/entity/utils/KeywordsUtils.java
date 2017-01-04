package org.wing4j.orm.entity.utils;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.ColumnMetadata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 关键字工具，用于对关键字进行检查
 */
@Slf4j
public abstract class KeywordsUtils {
    final static Properties KEYWORDS = new Properties();

    static {
        Enumeration<URL> keywordFiles = null;
        try {
            keywordFiles = Thread.currentThread().getContextClassLoader().getResources("wing4.orm.keywords.properties");
        } catch (IOException e) {
            log.error("加载关键字配置文件失败", e);
        }
        while (keywordFiles.hasMoreElements()){
            URL keywordFile = keywordFiles.nextElement();
            File file = new File(keywordFile.getPath());
            if(file.exists()){
                try {
                    KEYWORDS.load(keywordFile.openStream());
                } catch (IOException e) {
                    log.error("加载关键字配置文件失败", e);
                }
            }
        }

    }
    /**
     * 进行关键词校验
     *
     * @param columnMetadata 字段元信息
     */
    public static void vlidateKeyword(ColumnMetadata columnMetadata) {
        if ("true".equals(KEYWORDS.getProperty(columnMetadata.getJdbcName().toUpperCase()))) {
            log.error( "实体{} 中字段 {} 所标注的JPA注解包含不能使用的关键字 [{}] "
                    , columnMetadata.getEntityClass()
                    , columnMetadata.getColumnField().getName()
                    , columnMetadata.getJdbcName());
            throw new IllegalArgumentException(columnMetadata.getEntityClass().getName() + "中" + columnMetadata.getColumnField().getName() + " 所标注的JPA注解包含不能使用的关键字[" + columnMetadata.getJdbcName() + "]");
        }
    }

    public static String convert(String sql, WordMode mode){
        if(mode == WordMode.lowerCase){
            return sql.toLowerCase();
        }else if(mode == WordMode.upperCase){
            return sql.toUpperCase();
        }else{
            return sql;
        }
    }
}
