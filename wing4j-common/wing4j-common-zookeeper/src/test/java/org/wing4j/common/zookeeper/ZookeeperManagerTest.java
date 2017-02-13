package org.wing4j.common.zookeeper;

import org.junit.Test;
import org.wing4j.common.utils.MachineInfoUtils;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/2/7.
 */
public class ZookeeperManagerTest {

    @Test
    public void testReconnect() throws Exception {

    }

    @Test
    public void testMakeDir() throws Exception {
        ZookeeperManager zookeeperManager = ZookeeperManager.getInstance();
        zookeeperManager.init("localhost", "/demo", true);
        zookeeperManager.makeDir(MachineInfoUtils.getHostIp(), MachineInfoUtils.getHostName());
    }

    @Test
    public void testRelease() throws Exception {

    }

    @Test
    public void testGetRootChildren() throws Exception {

    }

    @Test
    public void testWritePersistentUrl() throws Exception {

    }

    @Test
    public void testReadUrl() throws Exception {

    }

    @Test
    public void testGetZooKeeper() throws Exception {

    }

    @Test
    public void testExists() throws Exception {

    }

    @Test
    public void testRead() throws Exception {

    }

    @Test
    public void testDeleteNode() throws Exception {

    }
}