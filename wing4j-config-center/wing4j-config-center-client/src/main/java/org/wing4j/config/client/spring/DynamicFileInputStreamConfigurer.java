package org.wing4j.config.client.spring;

import org.wing4j.config.client.loader.FileConfigCenterLoader;
import lombok.Setter;

import java.io.InputStream;

/**
 * Created by wing4j on 2017/2/12.
 * 动态文件输入流配置对象
 */
@Setter
public class DynamicFileInputStreamConfigurer {
    /**
     * File配置中心加载器
     */
    FileConfigCenterLoader fileConfigCenterLoader;
    /**
     * 打开指定文件名的文件
     * @param file 文件名，可以包含路径
     * @return 输入流
     */
    public InputStream openInputStream(String file){
        String path = null;
        String fileName = null;
        return openInputStream(path, fileName);
    }

    /**
     * 打开指定文件名的文件
     * @param path 路径
     * @param fileName 文件名，不包含路径
     * @return 输入流
     */
    public InputStream openInputStream(String path, String fileName){
        return fileConfigCenterLoader.openInputStream(path, fileName);
    }
}
