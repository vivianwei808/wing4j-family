package org.wing4j.common.httpclient;

/**
 * 重试的策略
 */
public interface RetryStrategy {
    /**
     * 重试
     * @param unreliableImpl 下载方法
     * @param retryTimes 重试次数
     * @param retryIntervalSeconds 每次重试间隔时间
     * @param <T> 返回值泛型
     * @return 返回值
     * @throws Exception 异常
     */
    <T> T retry(UnreliableHandler unreliableImpl, int retryTimes, int retryIntervalSeconds) throws Exception;
}