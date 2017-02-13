package org.wing4j.common.zookeeper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/2/7.
 */
public class PrintZookeeperTreeTest {

    @Test
    public void testList() throws Exception {
        PrintZookeeperTree printZookeeperTree = new PrintZookeeperTree();
        printZookeeperTree.connect("localhost");

        Thread.sleep(2000);

        System.out.println("\n\n==================");

        printZookeeperTree.list("/");

        System.out.println("\n\n==================");

        printZookeeperTree.close();

        System.out.println("\n\n==================");
    }
}