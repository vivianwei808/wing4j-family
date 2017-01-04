package org.wing4j.common.utils;

/**
 * 字符串工具
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 驼峰风格命名转换成下划线风格的命名
     *
     * @param str 驼峰命名风格字符串
     * @return 下划线风格的命名
     */
    public static String camelToUnderline(String str) {
        if (isBlank(str)) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append("_");
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }
        return sb.toString();
    }

    /**
     * 下划线风格的命名转换成驼峰风格命名
     *
     * @param str 下划线的命名
     * @return 驼峰命名
     */
    public static String underlineToCamel(String str) {
        if (isBlank(str)) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        str = str.toLowerCase();
        String[] words = str.split("_");
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                sb.append(words[i]);
            } else {
                sb.append(firstCharToUpper(words[i]));
            }
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String firstCharToUpper(String str) {
        if (isBlank(str)) {
            return "";
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String firstCharToLower(String str) {
        if (isBlank(str)) {
            return "";
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }


    /**
     * 填充字符串长度
     *
     * @param in            输入字符串
     * @param leftFillStyle 是否进行左填充
     * @param fill          填充字符
     * @param len           长度
     * @return 期望长度的字段
     */
    public static String fill(String in, boolean leftFillStyle, char fill, int len) {
        String str = in;
        while (str.length() < len) {
            str = leftFillStyle ? fill + str : str + fill;
        }
        return str;
    }

    /**
     * 以安全的方式获取字符串
     *
     * @param in 输入对象
     * @param ifNullRetVal 如果为空，则返回设置的值
     * @return 字符串
     */
    public static String safeToString(Object in, String ifNullRetVal) {
        if (in == null) {
            return ifNullRetVal;
        }
        return in.toString();
    }

    /**
     * 以安全的方式获取字符串
     *
     * @param in 输入对象
     * @return 获取字符串形式
     */
    public static String safeToString(Object in) {
        return safeToString(in, null);
    }
}
