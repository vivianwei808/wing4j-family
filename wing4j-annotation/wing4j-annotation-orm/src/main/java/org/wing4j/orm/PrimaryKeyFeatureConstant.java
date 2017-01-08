package org.wing4j.orm;

/**
 * Created by wing4j on 2017/1/8.
 * 主键生成特征字符串常量
 */
public interface PrimaryKeyFeatureConstant {
    String CURRENT_DATE = "${new Date()}";
    String yyyy_MM_dd = "${yyyyMMdd}";
    String yyyy_MM_dd_HH = "${yyyyMMddHH}";
    String yyyy_MM_dd_HH_mm = "${yyyyMMddHHmm}";
    String yyyy_MM_dd_HH_mm_ss = "${yyyyMMddHHmmss}";
    String yyyy_MM_dd_HH_mm_ss_SSS = "${yyyyMMddHHmmssSSS}";
}
