package org.wing4j.common.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

public class ConfigWatcher implements Watcher {
    private String path;
    private ResilientActiveKeyValueStore store;

    public ConfigWatcher(String path, String hosts) throws IOException, InterruptedException {
        store = new ResilientActiveKeyValueStore(true);
        this.path = path;
        store.connect(hosts);
    }

    public void displayConfig() throws InterruptedException, KeeperException {
        String value = store.read(this.path, this, null);
        System.out.printf("Read %s as %s\n", this.path, value);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDataChanged) {
            try {
                displayConfig();
            } catch (InterruptedException e) {
                System.err.println("Interrupted. Exiting.");
                Thread.currentThread().interrupt();
            } catch (KeeperException e) {
                System.err.printf("KeeperException: %s. Exiting.\n", e);
            }
        }

    }
}