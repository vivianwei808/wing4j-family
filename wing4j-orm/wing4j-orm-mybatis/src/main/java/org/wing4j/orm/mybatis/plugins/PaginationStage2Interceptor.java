package org.wing4j.orm.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.wing4j.orm.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationStage2Interceptor implements Interceptor {

    private String databaseType;

    public Object intercept(Invocation invocation) throws Throwable {
        Object obj = invocation.getArgs()[1];
        if (obj instanceof Pagination) {
            Pagination pagination = (Pagination) obj;
            List result = (List) invocation.proceed();
            pagination.setRecords(result);
            List<Pagination> list = new ArrayList<>();
            list.add(pagination);
            return list;
        } else {
            Object result = invocation.proceed();
            return result;
        }

    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}