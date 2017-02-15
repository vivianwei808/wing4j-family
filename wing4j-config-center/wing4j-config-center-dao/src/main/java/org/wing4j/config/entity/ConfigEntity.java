package org.wing4j.config.entity;

import java.util.Date;

/**
 * Created by wing4j on 2017/2/15.
 */
public class ConfigEntity {
    Integer id;
    Integer profileId;
    String key;
    String value;
    Integer version;
    String lastVersionValue;
    String description;
    Date createTime;
    Date updateTime;
    String createUser;
    String updateUser;
}
