package org.wing4j.common.utils;

/**
 * 日期样式
 */
public enum DateStyle {
    /**
     * 精确到毫秒的日期格式 yyyy-MM-dd HH:mm:ss,SSS
     */
    MILLI_FORMAT1("yyyy-MM-dd HH:mm:ss,SSS", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}"),
    /**
     * 精确到毫秒的日期格式 HH:mm:ss,SSS
     */
    MILLI_FORMAT2("HH:mm:ss,SSS", "\\d{2}:\\d{2}:\\d{2},\\d{3}"),
    /**
     * 精确到秒的日期格式 yyyy-MM-dd HH:mm:ss
     */
    SECOND_FORMAT1("yyyy-MM-dd HH:mm:ss", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
    /**
     * 精确到秒的日期格式 yyyy/MM/dd HH:mm:ss
     */
    SECOND_FORMAT2("yyyy/MM/dd HH:mm:ss", "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"),
    /**
     * 精确到秒的日期格式 HH:mm:ss
     */
    SECOND_FORMAT3("HH:mm:ss", "\\d{2}:\\d{2}:\\d{2}"),
    /**
     * 精确到秒的日期格式 yyyy/MM/dd HH/mm/ss
     */
    SECOND_FORMAT4("yyyy/MM/dd HH/mm/ss", "\\d{4}/\\d{2}/\\d{2} \\d{2}/\\d{2}/\\d{2}"),
    /**
     * 精确到分的日期格式 yyyy-MM-dd HH:mm
     */
    MINUTE_FORMAT1("yyyy-MM-dd HH:mm", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"),
    /**
     * 精确到分的日期格式 yyyy/MM/dd HH:mm
     */
    MINUTE_FORMAT2("yyyy/MM/dd HH:mm", "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}"),
    /**
     * 精确到天的日期格式 yyyy-MM-dd
     */
    DAY_FORMAT1("yyyy-MM-dd", "\\d{4}-\\d{2}-\\d{2}"),
    /**
     * 精确到天的日期格式 yyyy/MM/dd
     */
    DAY_FORMAT2("yyyy/MM/dd", "\\d{4}/\\d{2}/\\d{2}"),
    /**
     * 能够用于文件命名的日期 yyyy-MM-dd_HH-mm-ss
     */
    FILE_FORMAT1("yyyy-MM-dd_HH-mm-ss", "\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}"),
    /**
     * 能够用于文件命名的日期 yyyyMMddHHmmssSSS
     */
    FILE_FORMAT2("yyyyMMddHHmmssSSS", "\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}\\d{3}"),
    /**
     * 能够用于文件命名的日期 yyyyMMddHHmmss
     */
    FILE_FORMAT3("yyyyMMddHHmmss", "\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}"),
    /**
     * 能够用于文件命名的日期 yyyyMMddHHmm
     */
    FILE_FORMAT4("yyyyMMddHHmm", "\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}"),
    /**
     * 能够用于文件命名的日期 yyyyMMddHH
     */
    FILE_FORMAT5("yyyyMMddHH", "\\d{4}\\d{2}\\d{2}\\d{2}"),
    /**
     * 能够用于文件命名的日期 yyyyMMdd
     */
    FILE_FORMAT6("yyyyMMdd", "\\d{4}\\d{2}\\d{2}"),
    /**
     * 能够用于文件命名的日期 yyyyMM
     */
    FILE_FORMAT7("yyyyMM", "\\d{4}\\d{2}"),
    /**
     * 能够用于文件命名的日期 HHmmssSSS
     */
    FILE_FORMAT8("HHmmssSSS", "\\d{2}\\d{2}\\d{2}\\d{3}"),
    /**
     * 能够用于文件命名的日期 HHmmss
     */
    FILE_FORMAT9("HHmmss", "\\d{2}\\d{2}\\d{2}"),
    /**
     * 中文日期格式 yyyy年MM月dd日 HH时mm分ss秒
     */
    CHINESE_FORMAT1("yyyy年MM月dd日 HH时mm分ss秒", "\\d{4}年\\d{2}月\\d{2}日 \\d{2}时\\d{2}分\\d{2}秒"),
    /**
     * 中文日期格式 yyyy年MM月dd日
     */
    CHINESE_FORMAT2("yyyy年MM月dd日", "\\d{4}年\\d{2}月\\d{2}日"),
    /**
     * 中文时间格式 HH时mm分ss秒
     */
    CHINESE_FORMAT3("HH时mm分ss秒", "\\d{2}时\\d{2}分\\d{2}秒");

    String format;
    String mask;

    DateStyle(String format, String mask) {
        this.format = format;
        this.mask = mask;
    }

    public String getFormat() {
        return format;
    }

    public String getMask() {
        return mask;
    }

    public static  DateStyle of(String format){
        for(DateStyle dateStyle : DateStyle.values()){
            if(dateStyle.format.equals(format)){
                return dateStyle;
            }
        }
        throw  new IllegalArgumentException("无效的日期样式");
    }
}
