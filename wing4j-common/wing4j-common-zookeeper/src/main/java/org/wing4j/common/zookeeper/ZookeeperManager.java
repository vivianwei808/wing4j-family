package org.wing4j.common.zookeeper;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import org.wing4j.common.utils.MachineInfoUtils;

/**
 * ZK统一管理器
 */
@Slf4j
public class ZookeeperManager {
    ResilientActiveKeyValueStore store;
    String curHost;
    String curDefaultPrefixString;
    /**
     * 建立连接
     */
    private ZookeeperManager() {

    }
    public void init(String hosts, String defaultPrefixString, boolean debug) throws Exception {
        try {
            init0(hosts, defaultPrefixString, debug);
            log.debug("ZookeeperManager init.");
        } catch (Exception e) {
            throw new Exception("zookeeper init failed. ", e);
        }
    }

    /**
     * 初始化
     * @param hosts
     * @param defaultPrefixString
     * @param debug
     * @throws IOException
     * @throws InterruptedException
     */
    void init0(String hosts, String defaultPrefixString, boolean debug)
            throws IOException, InterruptedException {
        curHost = hosts;
        curDefaultPrefixString = defaultPrefixString;
        store = new ResilientActiveKeyValueStore(debug);
        store.connect(hosts);
        log.info("zoo prefix: " + defaultPrefixString);
        // 新建父目录
        makeDir(defaultPrefixString, MachineInfoUtils.getHostIp());
    }
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static ZookeeperManager instance = new ZookeeperManager();
    }

    public static ZookeeperManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 重新连接
     */
    public void reconnect() {
        store.reconnect();
    }

    /**
     * Zoo的新建目录
     * @param dir
     * @param data
     */
    public void makeDir(String dir, String data) {
        try {
            boolean exist = store.exists(dir);
            //如果不存在节点，则进行持久化写入
            if (!exist) {
                log.info("create: " + dir);
                this.writePersistentUrl(dir, data);
            } else {
            }
        } catch (KeeperException e) {
            log.error("cannot create path: " + dir, e);
        } catch (Exception e) {
            log.error("cannot create path: " + dir, e);
        }
    }

    /**
     * 应用程序必须调用它来释放zookeeper资源
     * @throws InterruptedException
     */
    public void release() throws InterruptedException {
        store.close();
    }

    /**
     * 获取子孩子 列表
     * @return
     */
    public List<String> getRootChildren() {
        return store.getRootChildren();
    }

    /**
     * 写持久化结点, 没有则新建, 存在则进行更新
     * @param url
     * @param value
     * @throws Exception
     */
    public void writePersistentUrl(String url, String value) throws Exception {
        store.write(url, value);
    }

    /**
     * 读结点数据
     * @param url
     * @param watcher
     * @return
     * @throws Exception
     */
    public String readUrl(String url, Watcher watcher) throws Exception {
        return store.read(url, watcher, null);
    }

    /**
     * 返回zk
     * @return
     */
    public ZooKeeper getZooKeeper() {
        return store.getZooKeeper();
    }

    /**
     * 路径是否存在
     * @param path
     * @return
     * @throws Exception
     */
    public boolean exists(String path) throws Exception {
        return store.exists(path);
    }

    /**
     * 生成一个临时结点
     * @param path 节点路径
     * @param value
     * @param createMode
     * @return
     * @throws Exception
     */
    public String createEphemeralNode(String path, String value, CreateMode createMode) throws Exception {
        return store.createEphemeralNode(path, value, createMode);
    }

    /**
     * 带状态信息的读取数据
     * @param path 节点路径
     * @param watcher
     * @param stat
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String read(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {
        return store.read(path, watcher, stat);
    }

    /**
     * 删除结点
     * @param path 节点路径
     */
    public void deleteNode(String path) {
        store.deleteNode(path);
    }
}