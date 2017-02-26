package org.wing4j.network.download.retry;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.network.download.RetryStrategy;
import org.wing4j.network.download.UnreliableHandler;

/**
 * Created by wing4j on 2017/2/7.
 * 轮循重试策略
 */
@Slf4j
public class RetryStrategyRoundRobin implements RetryStrategy {
    @Override
    public <T> T retry(UnreliableHandler handler, int retryTimes, int retryIntervalSeconds) throws Exception {
        int cur_time = 0;
        for (; cur_time < retryTimes; ++cur_time) {
            try {
                return handler.call();
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
