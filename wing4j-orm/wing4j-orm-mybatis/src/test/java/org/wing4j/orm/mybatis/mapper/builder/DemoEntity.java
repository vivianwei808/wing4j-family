package org.wing4j.orm.mybatis.mapper.builder;

import lombok.Data;
import org.wing4j.orm.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "TB_DEMO")
@Comment("测试表")
public class DemoEntity{
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @StringColumn(name = "SERIAL_NO", nullable = false, length = 36, type = StringType.CHAR, defaultValue = "1")
    @Comment("流水号")
    String serialNo;

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