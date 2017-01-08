package org.wing4j.common.sequence.h2;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.common.sequence.SequenceService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wing4j on 2016/12/28.
 * 使用h2数据库中的自增序列实现自增连续序列
 */
@Slf4j
public class H2SequenceServiceImpl implements SequenceService{
    @Setter
    DataSource dataSource;
    @Setter
    boolean autoCreate = false;

    @Override
    public int nextval(String schema, String prefix, String sequenceName, String feature) {
        if(dataSource == null){
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService")
                    .message("{} base H2 database implement SequenceService require 'dataSource'",  this.getClass().getSimpleName())
                    .solution("please check configure file!"));
        }
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            String db = connection.getMetaData().getDatabaseProductName();
            if (!"H2".equals(db)) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is {}, {} can not run on other database", db, this.getClass().getSimpleName())
                        .solution("please check if use H2 database in pom.xml"));
            }
            String seqName = "seq_" + schema + "_" + prefix + "_" + sequenceName + "_" + feature;
            seqName = seqName.toLowerCase();
            String sql = "select " + seqName + ".nextval".toLowerCase();
            if (this.autoCreate) {
                init(connection, seqName);
            }
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is {}, {} can not run on other database", db, this.getClass().getSimpleName())
                        .solution("please check database configure!"));
            }
        } catch (SQLException e) {
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService generate nextval")
                    .message("nextval happens a error, cause current database is h2, {} can not run on other database", this.getClass().getSimpleName())
                    .solution("please check if use H2 database in pom.xml")
                    .cause(e));
        } finally {
            close(connection, st, rs);
        }
    }

    void close(Connection connection, Statement st, ResultSet rs) {
        if(rs != null){
            try{
                rs.close();
            }catch (SQLException e){
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is h2, {} can not run on other database", this.getClass().getSimpleName())
                        .solution("please check if use H2 database in pom.xml")
                        .cause(e));
            }
        }
        if(st != null){
            try{
                st.close();
            }catch (SQLException e){
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is h2, {} can not run on other database", this.getClass().getSimpleName())
                        .solution("please check if use H2 database in pom.xml")
                        .cause(e));
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate nextval")
                        .message("nextval happens a error, cause current database is h2, {} can not run on other database", this.getClass().getSimpleName())
                        .solution("please check if use H2 database in pom.xml")
                        .cause(e));
            }
        }
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        if(dataSource == null){
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService")
                    .message("{} base H2 database implement SequenceService require 'dataSource'",  this.getClass().getSimpleName())
                    .solution("please check configure file!"));
        }
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            String db = connection.getMetaData().getDatabaseProductName();
            if (!"H2".equals(db)) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate currval")
                        .message("nextval happens a error, cause current database is {}, {} can not run on other database", db, this.getClass().getSimpleName())
                        .solution("please check if use H2 database in pom.xml"));
            }
            String seqName = "seq_" + schema + "_" + prefix + "_" + sequenceName + "_" + feature;
            seqName = seqName.toLowerCase();
            String sql = "select " + seqName + ".currval".toLowerCase();
            if (this.autoCreate) {
                init(connection, seqName);
            }
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("use SequenceService generate currval")
                        .message("currval happens a error, cause current database is {}, {} can not run on other database", db, this.getClass().getSimpleName())
                        .solution("please check database configure!"));
            }
        } catch (SQLException e) {
            throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                    .activity("use SequenceService generate nextval")
                    .message("currval happens a error, cause current database is h2, {} can not run on other database", this.getClass().getSimpleName())
                    .solution("please check if use H2 database in pom.xml")
                    .cause(e));
        } finally {
            close(connection, st, rs);
        }
    }

    void init(Connection connection ,String seqName) throws SQLException {
        String sql = "create sequence if not exists " + seqName + " start with 1 ".toLowerCase();
        Statement st = connection.createStatement();
        st.execute(sql);
        close(null, st, null);
    }
}
