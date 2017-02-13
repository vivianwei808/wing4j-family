package org.wing4j.common.zookeeper;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ConfigUpdater {
    private String path;
    private ResilientActiveKeyValueStore store;
    private Random random = new Random();

    public ConfigUpdater(String path, String hosts) throws IOException, InterruptedException {
        store = new ResilientActiveKeyValueStore(true);
        this.path = path;
        store.connect(hosts);
    }

    public void run() throws InterruptedException, KeeperException {
        while (true) {
            String value = random.nextInt(100) + "";
            store.write(path, value);
            System.out.printf("Set %s to %s\n", path, value);
            TimeUnit.SECONDS.sleep(random.nextInt(10));
        }
    }
}
