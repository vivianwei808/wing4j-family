package org.wing4j.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/2/7.
 */
public class FileSystemUtilsTest {

    @Test
    public void testMkdir() throws Exception {
        String path = "target/demo/test" + Math.random();
        FileSystemUtils.mkdir(path);
    }

    @Test
    public void testExists() throws Exception {
        String path = "target/demo/test" + Math.random();
        Assert.assertEquals(false, FileSystemUtils.exists(path));
        FileSystemUtils.mkdir(path);
        Assert.assertEquals(true, FileSystemUtils.exists(path));
    }

    @Test
    public void testPathJoin() throws Exception {
        String path = FileSystemUtils.pathJoin("target","demo", "test");
        Assert.assertEquals("target\\demo\\test", path);
    }

    @Test
    public void testRelativePath() throws Exception {
        String path = FileSystemUtils.relativePath(new File("target/demo/test/file"), new File("target/demo/test"));
        System.out.println(path);
    }

    @Test
    public void testCopyFile() throws Exception {
        String path = "target/demo/test" + Math.random();
        FileSystemUtils.mkdir(path);
        File srcFile = new File(path, "demo" + Math.random() + ".xml");
        FileOutputStream fos = new FileOutputStream(srcFile);
        fos.write("this is a test".getBytes());
        fos.flush();
        fos.close();
        FileSystemUtils.copyFile(srcFile, new File("target/demo/demo.xml"));
    }

    @Test
    public void testCopyFileWithAtom() throws Exception {
        String path = "target/demo/test" + Math.random();
        FileSystemUtils.mkdir(path);
        File srcFile = new File(path, "demo" + Math.random() + ".xml");
        FileOutputStream fos = new FileOutputStream(srcFile);
        fos.write("this is a test".getBytes());
        fos.flush();
        fos.close();
        FileSystemUtils.copyFileWithAtom(srcFile, new File("target/demo/demo.xml"), true);
    }
}