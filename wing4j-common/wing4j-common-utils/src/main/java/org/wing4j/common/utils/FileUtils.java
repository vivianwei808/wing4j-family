package org.wing4j.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {
    /**
     * 关闭文件流
     * @param w 输出流
     */
    public static final void closeWriter(Writer w) {
        if (w != null) {
            try {
                w.close();
            } catch (Exception e) {
                log.warn(e.toString());
            }
        }
    }

    /**
     * 关闭文件流
     * @param r 输入流
     */
    public static final void closeReader(Reader r) {
        if (r != null) {
            try {
                r.close();
            } catch (Exception e) {
                log.warn(e.toString());
            }
        }
    }

    /**
     * 关闭文件流
     * @param os 输出流
     */
    public static final void closeOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (Exception e) {
                log.warn(e.toString());
            }
        }
    }

    /**
     * 关闭文件流
     * @param is 输入流
     */
    public static final void closeInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                log.warn(e.toString());
            }
        }
    }

    /**
     * 使用jar包：commons-codec-1.4.jar的md5比较方法
     * http://blog.csdn.net/very365_1208/article/details/8824033
     *
     * @param oldName 旧文件名
     * @param newName 新文件名
     *
     * @return 相等返回真
     */
    public static boolean isFileUpdate(String oldName, String newName) {
        return isFileEqual(new File(oldName), new File(newName));
    }

    /**
     * http://blog.csdn.net/very365_1208/article/details/8824033
     * http://www.avajava.com/tutorials/lessons/whats-a-quick-way
     * -to-tell-if-the-contents-of-two-files-are-identical-or-not.html
     *
     * @param oldFile 旧文件名
     * @param newFile 新文件名
     *
     * @return 相等返回真
     */
    public static boolean isFileEqual(File oldFile, File newFile) {
        try {
            return contentEquals(oldFile, newFile);
        } catch (IOException e) {
            log.warn(e.toString());
            return false;
        }

    }
}