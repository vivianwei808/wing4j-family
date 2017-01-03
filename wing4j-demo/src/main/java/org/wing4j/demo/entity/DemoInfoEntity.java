package org.wing4j.demo.entity;

import lombok.Data;
import lombok.ToString;
import org.wing4j.orm.*;

import java.util.Date;

/**
 * Created by wing4j on 2017/1/1.
 */
@Table(name = "TB_DEMO_INF")
@Comment("测试表")
@Data
@ToString
public class DemoInfoEntity {
    @PrimaryKey
    @StringColumn(name = "SERAIL_NO", type = StringType.VARCHAR)
    @Comment("物理主键")
    String serialNo;

    @StringColumn(name = "NAME", type = StringType.VARCHAR)
    @Comment("姓名")
    String name;

    @NumberColumn(name = "AGE", type = NumberType.INTEGER, nullable = false)
    @Comment("年龄")
    int age;

    @DateColumn(name = "CREATE_DATE", type = DateType.DATE)
    @Comment("创建日期")
    Date createDate;

    @DateColumn(name = "LAST_UPDATE_DATE", type = DateType.TIMESTAMP)
    @Comment("最近更新日期")
    Date lastUpdateDate;
}
