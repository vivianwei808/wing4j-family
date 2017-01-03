package org.wing4j.orm;

/**
 * Created by wing4j on 2016/12/18.
 * 这个类定义了ORM使用到的方法名称
 */
public interface Constants {
    /**
     * 统计整个表的条数
     */
    String COUNT_ALL = "countAll";
    /**
     * 按照条件统计条数
     */
    String COUNT_OR = "countOr";
    /**
     * 按照条件统计条数
     */
    String COUNT_AND = "countAnd";
    /**
     * 按照主键进行删除
     */
    String DELETE_BY_PRIMARY_KEY = "deleteByPrimaryKey";
    /**
     * 按照And条件进行删除
     */
    String DELETE_AND = "deleteAnd";
    /**
     * 按照Or条件进行删除
     */
    String DELETE_OR = "deleteOr";
    /**
     * 无事务清空整个表
     */
    String TRUNCATE = "truncate";
    /**
     * 进行整个实体字段插入
     */
    String INSERT = "insert";
    /**
     * 进行实体上的非null字段的插入
     */
    String INSERT_SELECTIVE = "insertSelective";
    /**
     * 按照物理主键使用悲观锁进行锁定
     */
    String LOCK_BY_FOR_UPDATE_BY_PRIMARY_KEY = "lockByForUpdateByPrimaryKey";
    /**
     * 按照条件，使用悲观锁进行锁定
     */
    String LOCK_BY_FOR_UPDATE_AND = "lockByForUpdateAnd";
    String LOCK_BY_FOR_UPDATE_ORD = "lockByForUpdateOr";
    /**
     * 按照物理主键使用update方式锁定
     */
    String LOCK_BY_UPDATE_SET_PRIMARY_KEY = "lockByUpdateSetPrimaryKey";
    /**
     * 按照物理主键进行查询
     */
    String SELECT_BY_PRIMARY_KEY = "selectByPrimaryKey";
    /**
     * 按照AND条件查询
     */
    String SELECT_AND = "selectAnd";
    /**
     * 按照OR条件查询
     */
    String SELECT_OR = "selectOr";
    /**
     * 无条件查询整个表，可能导致系统奔溃
     */
    String SELECT_ALL = "selectAll";
    /**
     * 按照条件进行分页查询
     */
    String SELECT_PAGE_AND = "selectPageAnd";
    String SELECT_PAGE_OR = "selectPageOr";
    /**
     * 按照物理主键进行更新
     */
    String UPDATE_BY_PRIMARY_KEY = "updateByPrimaryKey";
    /**
     * 按照物理主键进行更新非null字段
     */
    String UPDATE_BY_PRIMARY_KEY_SELECTIVE = "updateByPrimaryKeySelective";
}
