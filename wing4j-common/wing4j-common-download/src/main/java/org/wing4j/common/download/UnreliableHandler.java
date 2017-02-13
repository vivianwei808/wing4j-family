package org.wing4j.common.download;

/**
 * 一个可重试可执行方法
 */
public interface UnreliableHandler {
    /**
     * 调用方法
     * @param <T> 返回值泛型
     * @return 返回值
     * @throws Exception 异常
     */
    <T> T call() throws Exception;
}
