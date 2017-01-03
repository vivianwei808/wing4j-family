package org.wing4j.orm.entity.utils;

import lombok.Data;
import org.wing4j.orm.Comment;
import org.wing4j.orm.Table;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Table(name = "TB_DEMO", schema = "db")
@Comment("测试表")
public class NormalJPADemoEntity extends NoPkJPADemoEntity{
    @Id
    @Column(name = "SERIAL_NO", nullable = false, length = 36)
    @Comment("流水号")
    String serialNo;
}
