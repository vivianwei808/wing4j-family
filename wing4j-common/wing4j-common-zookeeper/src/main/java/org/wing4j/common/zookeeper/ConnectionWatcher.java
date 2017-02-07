package org.wing4j.common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 连接管理
 *
 */
@Slf4j
public class ConnectionWatcher implements Watcher {
    // 10 秒会话时间 ，避免频繁的session expired
    private static final int SESSION_TIMEOUT = 10000;

    // 3秒
    private static final int CONNECT_TIMEOUT = 3000;
    protected ZooKeeper zk;

    private CountDownLatch connectedSignal = new CountDownLatch(1);

    private static String internalHost = "";

    // 是否调试状态
    private boolean debug = false;

    /**
     * @param debug
     */
    public ConnectionWatcher(boolean debug) {
        this.debug = debug;
    }

    /**
     * 连接ZK
     * @param hosts
     * @throws IOException
     * @throws InterruptedException
     */
    public void connect(String hosts) throws IOException, InterruptedException {
        internalHost = hosts;
        zk = new ZooKeeper(internalHost, SESSION_TIMEOUT, this);
        // 连接有超时哦
        connectedSignal.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        log.info("zookeeper: " + hosts + " , connected.");
    }

    /**
     * 当连接成功时调用的
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            log.info("zk SyncConnected");
            connectedSignal.countDown();
        } else if (event.getState().equals(Event.KeeperState.Disconnected)) {
            // 这时收到断开连接的消息，这里其实无能为力，因为这时已经和ZK断开连接了，只能等ZK再次开启了
            log.warn("zk Disconnected");
        } else if (event.getState().equals(Event.KeeperState.Expired)) {
            if (!debug) {
                // 这时收到这个信息，表示，ZK已经重新连接上了，但是会话丢失了，这时需要重新建立会话。
                log.error("zk Expired");
                // just reconnect forever
                reconnect();
            } else {
                log.info("zk Expired");
            }
        } else if (event.getState().equals(Event.KeeperState.AuthFailed)) {
            log.error("zk AuthFailed");
        }
    }

    /**
     * 含有重试机制的retry，加锁, 一直尝试连接，直至成功
     */
    public synchronized void reconnect() {
        log.info("start to reconnect....");
        int retries = 0;
        while (true) {
            try {
                if (!zk.getState().equals(ZooKeeper.States.CLOSED)) {
                    break;
                }
                log.warn("zookeeper lost connection, reconnect");
                close();
                connect(internalHost);
            } catch (Exception e) {
                log.error(retries + "\t" + e.toString());
                // sleep then retry
                try {
                    int sec = ResilientActiveKeyValueStore.RETRY_PERIOD_SECONDS;
                    log.warn("sleep " + sec);
                    TimeUnit.SECONDS.sleep(sec);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    /**
     * 关闭
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        zk.close();
    }

    public ZooKeeper getZooKeeper() {
        return zk;
    }

    public void setZooKeeper(ZooKeeper zk) {
        this.zk = zk;
    }
}