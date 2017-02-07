package org.wing4j.common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ZK读写
 */
@Slf4j
public class ResilientActiveKeyValueStore extends ConnectionWatcher {
    private static final Charset CHARSET = Charset.forName("UTF-8");
    // 最大重试次数
    public static final int MAX_RETRIES = 3;
    // 每次重试超时时间
    public static final int RETRY_PERIOD_SECONDS = 2;

    public ResilientActiveKeyValueStore(boolean debug) {
        super(debug);
    }

    /**
     * 写PATH数据, 是持久化的
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void write(String path, String value) throws InterruptedException, KeeperException {
        int retries = 0;
        while (true) {
            try {
                Stat stat = zk.exists(path, false);
                if (stat == null) {
                    zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } else {
                    zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
                }
                break;
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                log.warn("write connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                log.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 创建一个临时结点，如果原本存在，则不新建, 如果存在，则更新值
     * @param path
     * @param value
     * @param createMode
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String createEphemeralNode(String path, String value, CreateMode createMode)
        throws InterruptedException, KeeperException {
        int retries = 0;
        while (true) {
            try {
                Stat stat = zk.exists(path, false);
                if (stat == null) {
                    return zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                } else {
                    if (value != null) {
                        zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
                    }
                }
                return path;
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                log.warn("createEphemeralNode connect lost... will retry " + retries + "\t" + e.toString());
                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                log.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 判断是否存在
     *
     * @param path
     *
     * @return
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    public boolean exists(String path) throws InterruptedException, KeeperException {
        int retries = 0;
        while (true) {
            try {
                Stat stat = zk.exists(path, false);
                if (stat == null) {
                    return false;
                } else {
                    return true;
                }
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                log.warn("exists connect lost... will retry " + retries + "\t" + e.toString());
                if (retries++ == MAX_RETRIES) {
                    log.error("connect final failed");
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                log.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 读数据
     * @param path
     * @param watcher
     * @param stat
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String read(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {

        byte[] data = zk.getData(path, watcher, stat);
        return new String(data, CHARSET);
    }

    /**
     * 获取子孩子数据
     * @return
     */
    public List<String> getRootChildren() {
        List<String> children = new ArrayList<String>();
        try {
            children = zk.getChildren("/", false);
        } catch (KeeperException e) {
            log.error(e.toString());
        } catch (InterruptedException e) {
            log.error(e.toString());
        }
        return children;
    }

    /**
     * 删除结点
     * @param path
     */
    public void deleteNode(String path) {
        try {
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException e) {
            log.error("cannot delete path: " + path, e);
        } catch (InterruptedException e) {
            log.warn(e.toString());
        } catch (KeeperException e) {
            log.error("cannot delete path: " + path, e);
        }
    }

}