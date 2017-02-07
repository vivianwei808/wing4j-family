package org.wing4j.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/2/7.
 */
public class MachineInfoUtilsTest {

    @Test
    public void testGetHostName() throws Exception {
        System.out.println(MachineInfoUtils.getHostName());
    }

    @Test
    public void testGetHostIp() throws Exception {
        System.out.println(MachineInfoUtils.getHostIp());
    }
}