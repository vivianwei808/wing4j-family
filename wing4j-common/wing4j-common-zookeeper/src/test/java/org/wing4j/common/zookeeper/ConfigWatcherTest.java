package org.wing4j.common.zookeeper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/2/7.
 */
public class ConfigWatcherTest {

    @Test
    public void testDisplayConfig() throws Exception {
        ConfigWatcher configWatcher = new ConfigWatcher("localhost");
        configWatcher.displayConfig();
        // stay alive until process is killed or thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }
}