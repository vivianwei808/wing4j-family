package org.wing4j.common.markdown.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/30.
 */
public class IoUtilsTest {

    @Test
    public void testRead2list() throws Exception {
        List<String> lines = IoUtils.read2list(new FileInputStream(new File("target/test-classes/example1.md")), "GBK");
        Assert.assertTrue(!lines.isEmpty());
    }
}