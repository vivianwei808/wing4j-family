package org.wing4j.orm.mybatis.spring;

import lombok.Data;
import lombok.ToString;
import org.wing4j.orm.*;

import java.math.BigDecimal;

@Data
@Table(name = "TB_DEMO", schema = "db")
@Comment("测试表")
@ToString
public class UserInfoEntity {
    @PrimaryKey(strategy = PrimaryKeyStrategy.SEQUENCE, feature = PrimaryKeyFeatureConstant.yyyy_MM_dd_HH)
    @NumberColumn(name = "SERIAL_NO", nullable = false, type = NumberType.INTEGER, defaultValue = "1")
    @Comment("流水号")
    Integer serialNo;

    @StringColumn(name = "COL1", length = 12, type = StringType.CHAR)
    @Comment("字段1")
    String col1;

    @NumberColumn(name = "COL2", precision = 10, scale = 3, type = NumberType.DECIMAL)
    @Comment("字段2")
    BigDecimal col2;

    @NumberColumn(name = "COL3", scale = 10, nullable = false)
    @Comment("字段3")
    Integer col3;
}