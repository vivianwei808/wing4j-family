package org.wing4j.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.UUID;

/**
 * Created by wing4j on 2017/2/7.
 */
@Slf4j
public class FileSystemUtils {
    /**
     * 将文件名转换成随机文件名后缀
     * @param fileName 文件名
     * @return 随机文件名后缀
     */
    public static String randomFileName(String fileName){
        String random = UUID.randomUUID().toString();
        // 去掉“-”符号
        String suffix = random.substring(0, 8) + random.substring(9, 13) + random.substring(14, 18) + random.substring(19, 23) + random.substring(24);
        return fileName + "." + suffix;
    }
    /**
     * 建多层目录
     * @param filePath 多层目录
     * @return 建立成功返回真
     */
    public static boolean mkdir(final String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return true;
    }

    /**
     * 文件或目录是否存在
     * @param filePathString 文件或目录地址
     * @return 存在返回真
     */
    public static boolean exists(final String filePathString) {
        File f = new File(filePathString);
        return f.exists();
    }

    /**
     * 拼接目录
     * @param paths 子目录
     * @return 目录字符串
     */
    public static String pathJoin(final String... paths) {
        final String path;
        if (paths == null || paths.length == 0) {
            path = File.separator;
        } else {
            final StringBuffer sb = new StringBuffer();
            for (final String pathElement : paths) {
                if (pathElement.length() > 0) {
                    sb.append(pathElement);
                    sb.append(File.separator);
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            path = sb.toString();
        }
        return (path);
    }

    /**
     * 获取File相对于Folder的相对路径
     * @param file 文件路径
     * @param folder 相对文件夹
     * @return 相对路径
     */
    public static String relativePath(File file, File folder) {
        String filePath = file.getAbsolutePath();
        String folderPath = folder.getAbsolutePath();
        if (filePath.startsWith(folderPath)) {
            return filePath.substring(folderPath.length() + 1);
        } else {
            return null;
        }
    }

    /**
     * 转移文件
     * @param src
     * @param dest
     * @throws Exception
     */
    public static void copyFile(File src, File dest) throws IOException {
        // 删除文件
        if (dest.exists()) {
            dest.delete();
        }
        // 转移临时下载文件至下载文件夹
        FileUtils.copyFile(src, dest);
    }

    /**
     * 具有重试机制的 ATOM 转移文件 ，并且会校验文件是否一致 才替换
     * @param src 源文件
     * @param dest 目标文件
     * @param isDeleteSource 是否删除源文件
     * @throws IOException 异常
     */
    public static void copyFileWithAtom(File src, File dest, boolean isDeleteSource) throws IOException {
        // 文件锁所在文件
        File lockFile = new File(dest + ".lock");
        FileOutputStream outStream = null;
        FileLock lock = null;
        try {
            int tryTime = 0;
            while (tryTime < 3) {
                try {
                    outStream = new FileOutputStream(lockFile);
                    FileChannel channel = outStream.getChannel();
                    lock = channel.tryLock();
                    if (lock != null) {
                        if (dest.exists()) {
                            // 判断内容是否一样
                            if (FileUtils.isFileEqual(src, dest)) {
                                // 内容如果一样，就只需要删除源文件就行了
                                if (isDeleteSource) {
                                    src.delete();
                                }
                                break;
                            }
                        }
                        log.debug("start to replace " + src.getAbsolutePath() + " to " + dest.getAbsolutePath());
                        // 转移
                        copyFile(src, dest);
                        // 删除源文件
                        if (isDeleteSource) {
                            src.delete();
                        }
                        break;
                    }
                } catch (FileNotFoundException e) {
                    // 打不开文件，则后面进行重试
                    log.warn(e.toString());
                } finally {
                    // 释放锁，通道；删除锁文件
                    if (null != lock) {
                        try {
                            lock.release();
                        } catch (IOException e) {
                            log.warn(e.toString());
                        }

                        if (lockFile != null) {
                            lockFile.delete();
                        }
                    }
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            log.warn(e.toString());
                        }
                    }
                }
                // 进行重试
                log.warn("try lock failed. sleep and try " + tryTime);
                tryTime++;

                try {
                    Thread.sleep(1000 * tryTime);
                } catch (Exception e) {
                }
            }
        } catch (IOException e) {
            log.warn(e.toString());
        }

    }
}
