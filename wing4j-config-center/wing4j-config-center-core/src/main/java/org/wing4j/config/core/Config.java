package org.wing4j.config.core;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * Created by wing4j on 2017/2/15.
 */
@Data
@ToString
@Builder
@EqualsAndHashCode
public class Config {
    /**
     * 在数据库的原始ID
     */
    long id;
    /**
     * 参数项键
     */
    String key;
    /**
     * 参数项值
     */
    String value;
    /**
     * 修改的版本数
     */
    long version = 0;
    /**
     * 描述
     */
    String description;
    /**
     * 创建日期
     */
    Date createDate;
    /**
     * 更新日期
     */
    Date lastUpdateDate;
}
