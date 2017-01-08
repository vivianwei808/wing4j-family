package org.wing4j.common.sequence.mysql;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.common.sequence.SequenceService;
import org.wing4j.common.utils.MessageFormatter;
import org.wing4j.common.utils.StringUtils;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by wing4j on 2016/12/29.
 */
@Slf4j
public class MySQLSequenceServiceImpl implements SequenceService {
    @Setter
    DataSource dataSource;
    /**
     * allow auto execute create-table sql
     */
    @Setter
    boolean autoCreate = false;
    /**
     * create-table sql script
     * primary key is order, seq_name, seq_feature, seq_value is fixed!
     */
    static String SQL_CREATE_TABLE = "create table if not exists {}{}_sequence_inf(" +
            "   seq_value int not null auto_increment, " +
            "   seq_name varchar(50) not null," +
            "   seq_feature varchar(50) not null," +
            "   primary key(seq_name, seq_feature, seq_value)" +
            ") ENGINE=MyISAM auto_increment=1";
    /**
     * nextval sql script
     */
    static String SQL_NEXTVAL = "insert into {}{}_sequence_inf(seq_name, seq_feature) values(?,?)";
    /**
     * curval sql script
     */
    static String SQL_CURVAL = "select max(seq_value) from {}{}_sequence_inf where seq_name=? and seq_feature=?";

    @Override
    public int nextval(String schema, String prefix, final String sequenceName, final String feature) {
        if(dataSource == null){
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService")
                    .message("{} base MySQL database implement SequenceService require 'dataSource'",  this.getClass().getSimpleName())
                    .solution("please check configure file!"));
        }
        Connection connection = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        if (schema == null || schema.isEmpty()) {
            schema = "";
        } else {
            schema = schema + ".";
        }
        if (prefix == null || prefix.isEmpty()) {
            prefix = "wing4j";
        }
        final String nextvalsql = MessageFormatter.format(SQL_NEXTVAL, schema, prefix);
        log.debug(nextvalsql);
        try {
            connection = dataSource.getConnection();
            String db = connection.getMetaData().getDatabaseProductName();
            if (!"MySQL".equals(db)) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is {}, {} only can run on MySQL MyISAM database", db, this.getClass().getSimpleName())
                        .solution("please check if use MySQL database in pom.xml"));
            }
            if (autoCreate) {
                st = connection.createStatement();
                st.execute(StringUtils.format(SQL_CREATE_TABLE, schema, prefix));
                close(null, st, null);
            }
            pst = connection.prepareStatement(nextvalsql,Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, sequenceName);
            pst.setString(2, feature);
            pst.executeUpdate();
            //获取自动生成的主键
            rs = pst.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is {}, {} only can run on MySQL MyISAM database", db, this.getClass().getSimpleName())
                        .solution("please check database configure!"));
            }
        } catch (SQLException e) {
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService generate nextval")
                    .message("nextval happens a error,{} only can run on MySQL MyISAM database", this.getClass().getSimpleName())
                    .solution("please check if use H2 database in pom.xml")
                    .cause(e));
        }finally {
            close(connection, pst, rs);
        }
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        if(dataSource == null){
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService")
                    .message("{} base MySQL database implement SequenceService require 'dataSource'",  this.getClass().getSimpleName())
                    .solution("please check configure file!"));
        }
        Connection connection = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        if (schema == null || schema.isEmpty()) {
            schema = "";
        } else {
            schema = schema + ".";
        }
        if (prefix == null || prefix.isEmpty()) {
            prefix = "wing4j";
        }
        final String curvalsql = MessageFormatter.format(SQL_CURVAL, schema, prefix);
        log.debug(curvalsql);
        try {
            connection = dataSource.getConnection();
            String db = connection.getMetaData().getDatabaseProductName();
            if (!"MySQL".equals(db)) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService fetch curval")
                        .message("curval happens a error, cause current database is {}, {} only can run on MySQL MyISAM database", db, this.getClass().getSimpleName())
                        .solution("please check if use MySQL database in pom.xml"));
            }
            if (autoCreate) {
                st = connection.createStatement();
                st.execute(StringUtils.format(SQL_CREATE_TABLE, schema, prefix));
                close(null, st, null);
            }
            pst = connection.prepareStatement(curvalsql);
            pst.setString(1, sequenceName);
            pst.setString(2, feature);
            pst.execute();
            //获取自动生成的主键
            rs = pst.getResultSet();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService fetch curval")
                        .message("curval happens a error, cause current database is {}, {} only can run on MySQL MyISAM database", db, this.getClass().getSimpleName())
                        .solution("please check database configure!"));
            }
        } catch (SQLException e) {
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService fetch curval")
                    .message("curval happens a error,{} only can run on MySQL MyISAM database", this.getClass().getSimpleName())
                    .solution("please check if use MySQL database in pom.xml")
                    .cause(e));
        }finally {
            close(connection, pst, rs);
        }
    }

    void close(Connection connection, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is MySQL, {} can not run on other database", this.getClass().getSimpleName())
                        .solution("please check if use MySQL database in pom.xml")
                        .cause(e));
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is MySQL, {} can not run on other database", this.getClass().getSimpleName())
                        .solution("please check if use MySQL database in pom.xml")
                        .cause(e));
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is MySQL, {} can not run on other database", this.getClass().getSimpleName())
                        .solution("please check if use MySQL database in pom.xml")
                        .cause(e));
            }
        }
    }
}
