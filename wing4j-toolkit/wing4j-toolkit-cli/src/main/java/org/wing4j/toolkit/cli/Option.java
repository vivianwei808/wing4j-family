package org.wing4j.toolkit.cli;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wing4j on 2017/1/4.
 * 参数对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    /**
     * 长名字
     */
    String longName;
    /**
     * 短名字
     */
    String shortName;
    /**
     * 是否必须
     */
    boolean require;
    /**
     * 参数数目
     */
    int argNum;
    /**
     * 描述
     */
    String desc;
    /**
     * 例子参数
     */
    String example;

}
