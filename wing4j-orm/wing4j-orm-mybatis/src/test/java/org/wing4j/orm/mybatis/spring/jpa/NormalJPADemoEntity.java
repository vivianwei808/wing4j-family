package org.wing4j.orm.mybatis.spring.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "TB_DEMO", schema = "db")
public class NormalJPADemoEntity extends NoPkJPADemoEntity {
    @Id
    @Column(name = "SERIAL_NO", nullable = false, length = 36)
    String serialNo;
}
