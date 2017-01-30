package org.wing4j.orm.mybatis.markdown.wing4j;

import lombok.Data;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 */
@Data
public class MappedStatementRuntimeContext {
    /**
     * 编号
     */
    String id;

    boolean flushCacheRequired;
    boolean useCache;
    int fetchSize;
    int timeout;
    Class resultEntity;
    Class paramEntity;
    /**
     * SQL语句缓冲
     */
    StringBuilder sqlBuffer;
    /**
     * 语句对象类型
     */
    SqlCommandType sqlCommandType;
    /**
     * SQL源
     */
    final List<SqlNode> sqlNodes = new ArrayList<>();
    /**
     * 参数映射列表
     */
    final List<ParameterMapping> parameterMappings = new ArrayList<>();
    /**
     * 结果映射列表
     */
    final List<ResultMapping> resultMappings = new ArrayList<>();
}
