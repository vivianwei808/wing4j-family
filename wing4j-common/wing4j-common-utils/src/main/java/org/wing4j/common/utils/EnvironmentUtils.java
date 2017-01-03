package org.wing4j.common.utils;

import lombok.extern.slf4j.Slf4j;
/**
 * 环境工具
 */
@Slf4j
public abstract class EnvironmentUtils {
    /**
     * 环境枚举
     */
    public static enum Environment{
        /**
         * IDE环境的JUNIT
         */
        IDE_JUNIT,
        /**
         * MAVEN环境的JUNIT
         */
        MAVEN_JUNIT
    }
    /**
     * 判断运行时环境
     * @return 环境枚举
     */
    public static Environment determineRuntime(){
        Class mavenBooter = null;
        try{
            mavenBooter = Class.forName("org.apache.maven.surefire.booter.ForkedBooter");
            if(mavenBooter != null){
                log.info("当前环境为Maven单元测试环境");
                return Environment.MAVEN_JUNIT;
            }
        }catch (Exception e){
        }
        log.info("当前环境为IDE单元测试环境");
        return Environment.IDE_JUNIT;
    }
}
