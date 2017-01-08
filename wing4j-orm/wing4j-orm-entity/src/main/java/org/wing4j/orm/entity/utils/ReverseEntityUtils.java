package org.wing4j.orm.entity.utils;

import org.wing4j.common.utils.StringUtils;
import org.wing4j.orm.PrimaryKeyStrategy;
import org.wing4j.orm.entity.metadata.ColumnMetadata;
import org.wing4j.orm.entity.metadata.TableMetadata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/1.
 */
public class ReverseEntityUtils {
    public static Connection connectMySQL(String url, String user, String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未配置MySQL驱动");
        }
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从数据库生成实体
     *
     * @param url  数据库链接地址
     * @param user 用户名
     * @param pass 密码
     */
    public static List<TableMetadata> reverseFormDatabase(String shcema, String url, String user, String pass) throws SQLException {
        Connection connection = connectMySQL(url, user, pass);
        Statement statement = connection.createStatement();
        List<TableMetadata> tableMetadatas = new ArrayList<>();
        String sql = "select table_name as table_name, engine as table_engine, auto_increment as auto_increment, table_comment as table_comment  from information_schema.tables where table_schema = '" + shcema + "'";
        boolean succ = statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()){
            String name = resultSet.getString("table_name");
            String engine = resultSet.getString("table_engine");
            String autoIncrement = resultSet.getString("auto_increment");
            String comment = resultSet.getString("table_comment");
            TableMetadata tableMetadata = TableMetadata.builder().tableName(name).className(StringUtils.firstCharToUpper(StringUtils.underlineToCamel(name)) + "Entity").dataEngine(engine).comment(comment).schema(shcema).build();
            reverse(tableMetadata, shcema, connection);
            tableMetadatas.add(tableMetadata);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return tableMetadatas;
    }

    public static void reverse(TableMetadata tableMetadata, String schema, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select cols.column_name as column_name, cols.column_default as column_default, cols.is_nullable as is_nullable, cols.data_type as data_type, cols.column_type as column_type, extra as extra, cols.column_key as column_key, cols.column_comment as column_comment from information_schema.columns cols where cols.table_schema = '" + schema + "' and table_name = '"+ tableMetadata.getTableName() +"' order by cols.ordinal_position";
        //执行数据SQL获取表字段信息
        boolean succ = statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()){
            String column_name = resultSet.getString("column_name");
            String column_default = resultSet.getString("column_default");
            String is_nullable = resultSet.getString("is_nullable");
            String jdbc_type = resultSet.getString("data_type");
            String extra = resultSet.getString("extra");
            String dataType = resultSet.getString("column_type");
            String column_key = resultSet.getString("column_key");
            String column_comment = resultSet.getString("column_comment");

            if(jdbc_type.equals("int")){
                jdbc_type = "NUMERIC";
                dataType = "INTEGER";
            }else if(jdbc_type.equals("tinyint")){
                jdbc_type = "NUMERIC";
                dataType = "TINYINT";
            }else if(jdbc_type.equals("decimal")){
                jdbc_type = "DECIMAL";
            }else if(jdbc_type.equals("datetime")){
                jdbc_type = "TIMESTAMP";
            }
            ColumnMetadata.ColumnMetadataBuilder builder = ColumnMetadata.builder()
                    .tableMetadata(tableMetadata)
                    .jdbcName(column_name.toUpperCase())
                    .defaultValue(column_default)
                    .nullable("YES".equals(is_nullable))
                    .jdbcType(jdbc_type.toUpperCase())
                    .dataType(dataType.toUpperCase())
                    .comment(column_comment);
            //如果是主键则添加到主键列表中
            if(column_key != null && "PRI".equalsIgnoreCase(column_key)){
                tableMetadata.getPrimaryKeys().add(column_name);
                builder.primaryKeyStrategy(PrimaryKeyStrategy.IDENTITY);
            }
            tableMetadata.getOrderColumns().add(column_name);
            tableMetadata.getColumnMetadatas().put(column_name, builder.build());
        }

    }
}
