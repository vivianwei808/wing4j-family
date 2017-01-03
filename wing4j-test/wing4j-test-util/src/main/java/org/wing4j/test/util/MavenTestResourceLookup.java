package org.wing4j.test.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by wing4j on 2016/12/30.
 */
public class MavenTestResourceLookup {
    public static final String TARGET_PREFIX = "target/test-classes/";

    /**
     * 从线程栈获取当前路径
     *
     * @return 当前路径
     */
    static String getCurrentPackage() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().equals("java.lang.Thread")) {
                continue;
            }
            if (stackTraceElement.getClassName().equals("org.wing4j.test.util.MavenTestResourceLookup")) {
                continue;
            }
            //获取线程栈中的栈帧，然后加载类对象，获取包
            String className = stackTraceElement.getClassName();
            int lastDotIdx = className.lastIndexOf(".");
            return className.substring(0, lastDotIdx);
//                return Class.forName(stackTraceElement.getClassName()).getPackage().getName().replaceAll("\\.", "/");
            // NONE
        }
        return "";
    }

    /**
     * 打开target下的测试资源文件
     *
     * @param fileName 文件路径
     * @return URI
     * @throws IOException IO异常
     */
    public static URI open(String fileName) throws IOException {
        String path = getCurrentPackage();
        return open(path, fileName);
    }

    /**
     * 打开文件资源
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @return URL
     */
    public static URI open(String path, String fileName) throws IOException {
        if (fileName.startsWith(File.separator)) {
            path = path.substring(1);
        }
        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.startsWith(File.separator)) {
            fileName = fileName.substring(1);
        }
        path = path.replaceAll("\\.", "/");
        File file = new File(TARGET_PREFIX + path + File.separator +  fileName);
        if (!file.exists()) {
            throw new IOException(fileName + " is not exist!");
        }
        if (file.isDirectory()) {
            throw new IOException(fileName + " is directory!");
        }
        return file.toURI();
    }
}
