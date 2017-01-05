package org.wing4j.common.sequence.h2;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.wing4j.common.sequence.SequenceService;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by wing4j on 2016/12/28.
 * 使用h2数据库中的自增序列实现自增连续序列
 */
@Slf4j
public class H2SequenceServiceImpl implements SequenceService, InitializingBean {
    @Autowired(required = false)
    JdbcTemplate jdbcTemplate;
    @Setter
    boolean autoCreate = false;

    @Override
    public int nextval(String schema, String prefix, String sequenceName, String feature) {
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            String db = connection.getMetaData().getDatabaseProductName();
            if (!"H2".equals(db)) {
                throw new IllegalArgumentException("数据库不运行在H2上");
            }
        } catch (SQLException e) {
            log.error("", e);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        String seqName = "seq_" + schema + "_" + prefix + "_" + sequenceName + "_" + feature;
        seqName = seqName.toLowerCase();
        String sql = "select " + seqName + ".nextval".toLowerCase();
        if (this.autoCreate) {
            init(seqName);
        }
        int seq = jdbcTemplate.queryForObject(sql, Integer.class);
        return seq;
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            String db = connection.getMetaData().getDatabaseProductName();
            if (!"H2".equals(db)) {
                throw new IllegalArgumentException("数据库不运行在H2上");
            }
        } catch (SQLException e) {
            log.error("", e);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        String seqName = "seq_" + schema + "_" + prefix + "_" + sequenceName + "_" + feature;
        seqName = seqName.toLowerCase();
        String sql = "select " + seqName + ".currval";
        if (this.autoCreate) {
            init(seqName);
        }
        int seq = jdbcTemplate.queryForObject(sql, Integer.class);
        return seq;
    }

    void init(String seqName) {
        String sql = "create sequence if not exists " + seqName + " start with 1 ".toLowerCase();
        jdbcTemplate.execute(sql);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jdbcTemplate, "base h2 database implement SequenceService require 'JdbcTemplate'");
    }
}
