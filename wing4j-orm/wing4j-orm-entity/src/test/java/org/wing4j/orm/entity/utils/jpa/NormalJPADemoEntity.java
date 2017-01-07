package org.wing4j.orm.entity.utils.jpa;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "TB_DEMO", schema = "db")
public class NormalJPADemoEntity extends NoPkJPADemoEntity {
    @Id
    @Column(name = "SERIAL_NO", nullable = false, length = 36)
    String serialNo;
}
