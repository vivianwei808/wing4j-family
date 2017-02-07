package org.wing4j.common.httpclient.retry;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.httpclient.RetryStrategy;
import org.wing4j.common.httpclient.UnreliableHandler;

/**
 * Created by wing4j on 2017/2/7.
 * 轮循重试策略
 */
@Slf4j
public class RetryStrategyRoundBin implements RetryStrategy{
    @Override
    public <T> T retry(UnreliableHandler unreliableImpl, int retryTimes, int retryIntervalSeconds) throws Exception {
        int cur_time = 0;
        for (; cur_time < retryTimes; ++cur_time) {
            try {
                return unreliableImpl.call();
            } catch (Exception e) {
                log.warn("cannot reach, will retry " + cur_time + " .... " + e.toString());
                try {
                    Thread.sleep(retryIntervalSeconds * 1000);
                } catch (InterruptedException e1) {
                }
            }
        }
        log.warn("finally failed....");
        throw new Exception();
    }
}
