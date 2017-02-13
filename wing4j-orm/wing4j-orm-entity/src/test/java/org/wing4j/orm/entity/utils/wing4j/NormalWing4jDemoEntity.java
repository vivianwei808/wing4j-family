package org.wing4j.orm.entity.utils.wing4j;

import lombok.Data;
import org.wing4j.orm.*;

@Data
@Table(prefix = "FA", name = "TB_DEMO", suffix = "INF", schema = "SCHEMA")
@Comment("测试表")
public class NormalWing4jDemoEntity extends NoPkWing4jDemoEntity{
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @StringColumn(name = "SERIAL_NO", nullable = false, length = 36, type = StringType.CHAR, defaultValue = "1")
    @Comment("流水号")
    String serialNo;

}
