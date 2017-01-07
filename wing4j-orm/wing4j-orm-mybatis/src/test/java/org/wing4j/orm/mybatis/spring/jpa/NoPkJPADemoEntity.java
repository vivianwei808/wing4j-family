package org.wing4j.orm.mybatis.spring.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "TB_DEMO", schema = "db")
public class NoPkJPADemoEntity {
    @Column(name = "COL1", length = 12)
    String col1;

    @Column(name = "COL2", precision = 10, scale = 3)
    BigDecimal col2;

    @Column(name = "COL3", nullable = false)
    Integer col3;

    @Column(name = "COL4", nullable = false)
    int col4;

    @Column(name = "COL5", nullable = false)
    java.util.Date col5;

    @Column(name = "COL6", nullable = false)
    java.sql.Date col6;

    @Column(name = "COL7", nullable = false)
    java.sql.Time col7;

    @Column(name = "COL8", nullable = false)
    java.sql.Timestamp col8;

    @Column(name = "COL9", nullable = false)
    boolean col9;

    @Column(name = "COL10", nullable = false)
    Boolean col10;
}
