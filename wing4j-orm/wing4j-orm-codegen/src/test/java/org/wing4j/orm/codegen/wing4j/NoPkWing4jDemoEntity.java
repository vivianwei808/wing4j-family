package org.wing4j.orm.codegen.wing4j;

import lombok.Data;
import org.wing4j.orm.*;

import java.math.BigDecimal;

@Data
@Table(name = "TB_DEMO", schema = "db")
@Comment("测试表")
public class NoPkWing4jDemoEntity {

    @StringColumn(name = "COL1", length = 12, type = StringType.VARCHAR)
    @Comment("字段1")
    String col1;

    @NumberColumn(name = "COL2", precision = 10, scale = 3, type = NumberType.DECIMAL)
    @Comment("字段2")
    BigDecimal col2;

    @NumberColumn(name = "COL3", scale = 10, nullable = false)
    @Comment("字段3")
    Integer col3;

    @NumberColumn(name = "COL4", scale = 10, nullable = false)
    @Comment("字段4")
    int col4;

    @DateColumn(name = "COL5", nullable = false, type = DateType.DATETIME)
    @Comment("字段5")
    java.util.Date col5;

    @DateColumn(name = "COL6", nullable = false, type = DateType.DATE)
    @Comment("字段6")
    java.sql.Date col6;

    @DateColumn(name = "COL7", nullable = false, type = DateType.TIME)
    @Comment("字段7")
    java.sql.Time col7;

    @DateColumn(name = "COL8", nullable = false, type = DateType.TIMESTAMP)
    @Comment("字段8")
    java.sql.Timestamp col8;

    @NumberColumn(name = "COL9", nullable = false, type = NumberType.INTEGER)
    @Comment("字段9")
    boolean col9;

    @NumberColumn(name = "COL10", nullable = false, type = NumberType.INTEGER)
    @Comment("字段10")
    Boolean col10;

}
