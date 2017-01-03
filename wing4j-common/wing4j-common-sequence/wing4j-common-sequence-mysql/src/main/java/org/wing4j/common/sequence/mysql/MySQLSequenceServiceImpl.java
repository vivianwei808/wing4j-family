package org.wing4j.common.sequence.mysql;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;
import org.wing4j.common.sequence.SequenceService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

/**
 * Created by wing4j on 2016/12/29.
 */
@Slf4j
public class MySQLSequenceServiceImpl implements SequenceService, InitializingBean {
    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;
    /**
     * allow auto execute create-table sql
     */
    @Setter
    boolean autoCreate = false;
    /**
     * create-table sql script
     * primary key is order, seq_name, seq_feature, seq_value is fixed!
     */
    static String SQL_CREATE_TABLE = "create table if not exists {0}{1}_sequence_inf(seq_value int not null auto_increment, seq_name varchar(50) not null, seq_feature varchar(50) not null, primary key(seq_name, seq_feature, seq_value)) ENGINE=MyISAM auto_increment=1";
    /**
     * nextval sql script
     */
    static String SQL_NEXTVAL = "insert into {0}{1}_sequence_inf(seq_name, seq_feature) values(?,?)";
    /**
     * curval sql script
     */
    static String SQL_CURVAL = "select max(seq_value) from {0}{1}_sequence_inf where seq_name=? and seq_feature=?";

    @Override
    public int nextval(String schema, String prefix, final String sequenceName, final String feature) {
        if (schema == null || schema.isEmpty()) {
            schema = "";
        } else {
            schema = schema + ".";
        }
        if (prefix == null || prefix.isEmpty()) {
            prefix = "wing4j";
        }
        final String sql = MessageFormat.format(SQL_NEXTVAL, schema, prefix);
        log.debug(sql);
        if (this.autoCreate) {
            jdbcTemplate.execute(MessageFormat.format(SQL_CREATE_TABLE, schema, prefix));
        }
        try {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement preState = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preState.setString(1, sequenceName);
                    preState.setString(2, feature);
                    return preState;
                }
            }, generatedKeyHolder);
            return generatedKeyHolder.getKey().intValue();
        } catch (DataAccessException e) {
            //进行异常处理,打印建表语句
            log.error("use MySQL MyISAM engine nextval happens error!", e);
            log.error("please in MySQL database execute this sql script：{}", MessageFormat.format(SQL_CREATE_TABLE, schema, prefix));
            throw e;
        }
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        if (schema == null || schema.isEmpty()) {
            schema = "";
        } else {
            schema = schema + ".";
        }
        if (prefix == null || prefix.isEmpty()) {
            prefix = "wing4j";
        }
        if (this.autoCreate) {
            jdbcTemplate.execute(MessageFormat.format(SQL_CREATE_TABLE, schema, prefix));
        }
        try {
            int seq = jdbcTemplate.queryForObject(MessageFormat.format(SQL_CURVAL, schema, prefix), new Object[]{sequenceName, feature}, Integer.class);
            return seq;
        } catch (DataAccessException e) {
            //进行异常处理,打印建表语句
            log.error("use MySQL MyISAM engine curval happens error!", e);
            log.error("please in MySQL database execute this sql script：{}", MessageFormat.format(SQL_CREATE_TABLE, schema, prefix));
            throw e;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jdbcTemplate, "base MySQL implement on MyISAM engine ,SequenceService require 'JdbcTemplate'");
    }

}
