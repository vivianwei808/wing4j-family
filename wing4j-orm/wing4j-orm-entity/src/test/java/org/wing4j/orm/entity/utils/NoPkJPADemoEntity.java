package org.wing4j.orm.entity.utils;

import lombok.Data;
import org.wing4j.orm.*;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@Table(name = "TB_DEMO", schema = "db")
@Comment("测试表")
public class NoPkJPADemoEntity {
    @Column(name = "COL1", length = 12)
    @Comment("字段1")
    String col1;

    @Column(name = "COL2", precision = 10, scale = 3)
    @Comment("字段2")
    BigDecimal col2;

    @Column(name = "COL3", scale = 10, nullable = false)
    @Comment("字段3")
    Integer col3;
}
