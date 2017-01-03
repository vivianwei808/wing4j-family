package org.wing4j.orm.mybatis.plugins;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.wing4j.orm.DatabaseType;
import org.wing4j.orm.Pagination;

@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
public class PaginationStage1Interceptor implements Interceptor {
    String databaseType;

    public Object intercept(Invocation invocation) throws Throwable {
        if(!invocation.getTarget().getClass().isAssignableFrom(RoutingStatementHandler.class)){
            return invocation.proceed();
        }
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
        BoundSql boundSql = delegate.getBoundSql();
        Object obj = boundSql.getParameterObject();
        if (obj instanceof Pagination) {
            Pagination page = (Pagination) obj;
            MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
            Connection connection = (Connection) invocation.getArgs()[0];
            String sql = boundSql.getSql();
            if (page.isStatsTotal()) {
                this.doStatsTotalRecord(page, mappedStatement, connection);
            }
            String pageSql = this.getPageSql(page, sql);
            ReflectUtil.setFieldValue(boundSql, "sql", pageSql);
        }
        return invocation.proceed();
    }


    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        this.databaseType = properties.getProperty("databaseType");
    }

    private String getPageSql(Pagination<?> page, String sql) {
        StringBuffer sqlBuffer = new StringBuffer(sql);
        if (DatabaseType.MySQL.name().equalsIgnoreCase(databaseType)) {
            return getMysqlPageSql(page, sqlBuffer);
        } else if (DatabaseType.Oracle.name().equalsIgnoreCase(databaseType)) {
            return getOraclePageSql(page, sqlBuffer);
        }
        return sqlBuffer.toString();
    }

    private String getMysqlPageSql(Pagination<?> page, StringBuffer sqlBuffer) {
        //计算第一条记录的位置，Mysql中记录的位置是从0开始的。
        int offset = (page.getCurPageNo() - 1) * page.getPageSize();
        sqlBuffer.append(" limit ").append(offset).append(",").append(page.getPageSize());
        return sqlBuffer.toString();
    }

    private String getOraclePageSql(Pagination<?> page, StringBuffer sqlBuffer) {
        //计算第一条记录的位置，Oracle分页是通过rownum进行的，而rownum是从1开始的
        int offset = (page.getCurPageNo() - 1) * page.getPageSize() + 1;
        sqlBuffer.insert(0, "SELECT u.*, rownum r FROM (").append(") u WHERE rownum < ").append(offset + page.getPageSize());
        sqlBuffer.insert(0, "SELECT * FROM (").append(") WHERE r >= ").append(offset);
        //上面的Sql语句拼接之后大概是这个样子：
        //select * from (select u.*, rownum r from (select * from t_user) u where rownum < 31) where r >= 16
        return sqlBuffer.toString();
    }

    private void doStatsTotalRecord(Pagination<?> page, MappedStatement mappedStatement, Connection connection) {
        BoundSql boundSql = mappedStatement.getBoundSql(page);
        String sql = boundSql.getSql();
        String countSql = this.getCountSql(sql);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
        Map<String, Object> additionalParameters = (Map<String, Object>) ReflectUtil.getFieldValue(boundSql, "additionalParameters");
        Map<String, Object> count_additionalParameters = (Map<String, Object>) ReflectUtil.getFieldValue(countBoundSql, "additionalParameters");
        count_additionalParameters.putAll(additionalParameters);
        MetaObject metaObject = (MetaObject) ReflectUtil.getFieldValue(boundSql, "metaParameters");
        ReflectUtil.setFieldValue(countBoundSql, "metaParameters", metaObject);
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(countSql);
            parameterHandler.setParameters(pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalRecord = rs.getInt(1);
                page.ifOverPageNumResetCurPageNoAndTotal(totalRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCountSql(String sql) {
        return "select count(1) from (" + sql + ") page_table";
    }

    private static class ReflectUtil {
        public static Object getFieldValue(Object obj, String fieldName) {
            Object result = null;
            Field field = ReflectUtil.getField(obj, fieldName);
            if (field != null) {
                field.setAccessible(true);
                try {
                    result = field.get(obj);
                } catch (Exception e) {
                }
            }
            return result;
        }

        private static Field getField(Object obj, String fieldName) {
            Field field = null;
            for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    //这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
                }
            }
            return field;
        }

        public static void setFieldValue(Object obj, String fieldName,
                                         Object fieldValue) {
            Field field = ReflectUtil.getField(obj, fieldName);
            if (field != null) {
                try {
                    field.setAccessible(true);
                    field.set(obj, fieldValue);
                    field.setAccessible(false);
                } catch (Exception e) {
                }
            }
        }
    }

    public PaginationStage1Interceptor(DatabaseType databaseType) {
        this.databaseType = databaseType.name();
    }
}