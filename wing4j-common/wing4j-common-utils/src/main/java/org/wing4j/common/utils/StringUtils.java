package org.wing4j.common.utils;


public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
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
     * 第一个符号大写
     * @param param
     * @return
     */
   public static String firstCharToUpper(String param){
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        char[] chars = param.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
    /**
     * 第一个符号大写
     * @param param
     * @return
     */
    public static String firstCharToLower(String param){
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        char[] chars = param.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * 将下划线转换为驼峰命名
     * @param param 下划线的命名
     * @return 驼峰命名
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        param = param.toLowerCase();
        String[] words = param.split("_");
        for (int i = 0; i < words.length; i++) {
            if(i == 0){
                sb.append(words[i]);
            }else{
                sb.append(firstCharToUpper(words[i]));
            }
        }
        return sb.toString();
    }

    /**
     * 填充字符串长度
     * @param in 输入字符串
     * @param rightFillStyle 为真右填充
     * @param fillChar 填充字符
     * @param len 长度
     * @return
     */
    public static String fill(String in, boolean rightFillStyle, char fillChar, int len) {
        String str = in;
        while (str.length() < len) {
            str = rightFillStyle ? str + fillChar : fillChar + str;
        }
        return str;
    }

    /**
     * 安全toString
     * @param obj
     * @param ifNullRetVal 如果为空，则返回设置的值
     * @return
     */
    public static String safeToString(Object obj, String ifNullRetVal){
        if(obj == null){
            return ifNullRetVal;
        }
        String str = obj.toString();
        return str;
    }

    /**
     * 安全toString
     * @param obj
     * @return
     */
    public static String safeToString(Object obj){
        return safeToString(obj, null);
    }
}
