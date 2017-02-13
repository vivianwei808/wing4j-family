package org.wing4j.config.client.loader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by wing4j on 2017/2/12.
 * 文件配置中心加载器
 */
public class FileConfigCenterLoader {
    /**
     * 打开文件输入流
     * @param path 文件所在路径
     * @param fileName 文件名
     * @return 输入流
     */
    public InputStream openInputStream(String path, String fileName){
        //TODO 1.从配置中心下载文件 groupId, artifactId, version, profile, key(path,fileName),下载方式samba,ftp,http三种方式
        //下载都支持重试机制
        //TODO 2.将文件保存在临时目录
        //~/.file/profile/key.version
        //TODO 3.读取文件写入ByteArrayInputStream,这样做的目的在于防止远程服务器更新时，本地读入发生阻塞
        ByteArrayInputStream is = null;
        return is;
    }

    /**
     * 配置中心主机地址
     */
    String host;
    /**
     * 配置中心主机端口号
     */
    int port;
    /**
     * 组织编号
     */
    String groupId;
    /**
     * 组件编号
     */
    String artifactId;
    /**
     * 版本
     */
    String version;
    /**
     * 环境
     */
    String profile;
    /**
     * 安全密钥
     */
    String secretKey;
}
