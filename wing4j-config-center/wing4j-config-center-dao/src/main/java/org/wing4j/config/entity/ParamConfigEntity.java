package org.wing4j.config.entity;

import org.wing4j.orm.*;

import java.util.Date;

/**
 * Created by wing4j on 2017/2/15.
 */
@Table(name = "PARAM_CONFIG", prefix = "TB")
@Comment("参数配置信息")
public class ParamConfigEntity {
    @PrimaryKey(strategy = PrimaryKeyStrategy.IDENTITY)
    @NumberColumn(name = "CONFIG_NO", nullable = false, type = NumberType.INTEGER)
    @Comment("参数号")
    Integer paramNo;
    Integer profileId;
    String key;
    String value;
    Integer version;
    String lastVersionValue;
    String description;
    Date createDate;
    Date lastUpdateDate;
    String createUid;
    String lastUpdateUid;
}
