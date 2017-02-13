package org.wing4j.orm.entity.utils;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.utils.ReflectionUtils;
import org.wing4j.orm.Comment;
import org.wing4j.orm.DateColumn;
import org.wing4j.orm.NumberColumn;
import org.wing4j.orm.StringColumn;
import org.wing4j.orm.entity.exception.OrmEntityRuntimeException;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体信息提取器
 */
@Slf4j
public abstract class EntityExtracteUtils {

    public static final Map<Class, TableMetadata> TABLES_CACHE = new HashMap<Class, TableMetadata>();

    /**
     * 提取实体上的元信息
     *
     * @param entityClass  实体类
     * @param strictWing4j 严格使用Wing4j注解
     * @return 元信息
     */
    public static TableMetadata extractTable(Class entityClass, boolean strictWing4j) {
        //如果缓存包含则直接返回
        if (TABLES_CACHE.containsKey(entityClass)) {
            return TABLES_CACHE.get(entityClass);
        }
        javax.persistence.Table tableAnnJPA = (javax.persistence.Table) entityClass.getAnnotation(javax.persistence.Table.class);
        org.wing4j.orm.Table tableAnnWing4j = (org.wing4j.orm.Table) entityClass.getAnnotation(org.wing4j.orm.Table.class);
        if (tableAnnJPA == null && tableAnnWing4j == null) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", entityClass)
                    .message("没有使用JPA注解{}，也没使用Wing4j注解{}", javax.persistence.Table.class, org.wing4j.orm.Table.class)
                    .solution("建议使用{}注解", org.wing4j.orm.Table.class));
        }
        if (tableAnnJPA != null && tableAnnWing4j != null) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", entityClass)
                    .message("同时使用JPA注解{}和使用Wing4j注解{}", javax.persistence.Table.class, org.wing4j.orm.Table.class)
                    .solution("建议使用{}注解，也可以使用JPA注解{}", org.wing4j.orm.Table.class, javax.persistence.Table.class));
        }
        TableMetadata tableMetadata = TableMetadata.builder().entityClass(entityClass).className(entityClass.getSimpleName()).build();
        //严格模式下，只能使用Wing4j注解
        if(strictWing4j){
            if (tableAnnWing4j == null) {
                throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                        .activity("提取实体类{}的元信息", entityClass)
                        .message("由于已开启强制使用Wing4j注解，但是实际使用Wing4j注解{}", javax.persistence.Table.class)
                        .solution("必须强制使用Wing4j注解", org.wing4j.orm.Table.class));
            }
            checkCouldNotUseJpaAnnotation(tableMetadata);
        }

        //解析JPA注解
        if (tableAnnWing4j != null) {
            Wing4jEntityExtracteUtils.extractTableWing4j(tableMetadata);
            //解析wing4j注解
        } else if (tableAnnJPA != null) {
            JpaEntityExtracteUtils.extractTableJPA(tableMetadata);
            //解析wing4j注解
        }
        //提取字段
        extractFields(tableMetadata, tableAnnWing4j != null);
        if (tableMetadata.getPrimaryKeys().isEmpty()) {
            throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                    .activity("提取实体类{}的元信息", entityClass)
                    .message("不允许无物理主键的实体")
                    .solution("建议在主键字段标注{}注解，也可以使用JPA注解{}", org.wing4j.orm.PrimaryKey.class, javax.persistence.Id.class));
        }
        return tableMetadata;
    }

    /**
     * 检查不能用JPA注解
     * @param tableMetadata 表元信息
     */
    public static void checkCouldNotUseJpaAnnotation(TableMetadata tableMetadata){
        List<Field> fields = ReflectionUtils.getFieldsExcludeTransient(tableMetadata.getEntityClass());
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations){
                Class annClazz = annotation.annotationType();
                boolean useJpa = annClazz.getName().startsWith("javax.persistence.");
                if(useJpa){
                    throw new OrmEntityRuntimeException(ErrorContextFactory.instance()
                            .activity("提取实体类{}的元信息", tableMetadata.getEntityClass())
                            .message("已开启严格Wing4j模式，禁止使用JPA注解，当前使用了{}注解", annClazz.getName())
                            .solution("将所有字段标注JPA注解转换成等价的Wing4j注解"));
                }
            }
        }
    }
    /**
     * 提取实体类的所有字段
     *
     * @param tableMetadata 表元信息
     * @param useWing4jAnn 用wing4j注解
     */
    public static void extractFields(TableMetadata tableMetadata, boolean useWing4jAnn) {
        List<Field> fields = ReflectionUtils.getFieldsExcludeTransient(tableMetadata.getEntityClass());
        for (Field field : fields) {
            //任意使用了一个字段注解的
            ColumnMetadata columnMetadata = ColumnMetadata.builder()
                    .tableMetadata(tableMetadata)
                    .entityClass(tableMetadata.getEntityClass())
                    .columnField(field)
                    .javaName(field.getName())
                    .javaType(field.getType())
                    .build();
            //提取该字段元信息
            if(useWing4jAnn){
                if (!Wing4jEntityExtracteUtils.extractField(columnMetadata)) {
                    continue;
                }
            }else{
                if (!JpaEntityExtracteUtils.extractField(columnMetadata)) {
                    continue;
                }
            }
            KeywordsUtils.vlidateKeyword(columnMetadata);
            //保存有序的字段
            tableMetadata.getOrderColumns().add(columnMetadata.getJdbcName());
            tableMetadata.getColumnMetadatas().put(columnMetadata.getJdbcName(), columnMetadata);
        }
    }
}
