package org.wing4j.orm.entity.utils;

import org.junit.Test;
import org.wing4j.orm.entity.metadata.TableMetadata;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/2.
 */
public class EntityUtilsTest {
    @Test
    public void testGenerate0() throws Exception {
        List<TableMetadata> tables = ReverseEntityUtils.reverseFormDatabase("wing4j", "jdbc:mysql://192.168.1.106:3306/wing4j", "root", "root");
        EntityUtils.generate(tables, "test.entity");
    }
    @Test
    public void testGenerate1() throws Exception {
//        List<TableMetadata> tables = ReverseEntityUtils.reverseFormDatabase("wing4j", "jdbc:mysql://192.168.1.106:3306/wing4j", "root", "root");
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        EntityUtils.generate(os, tableMetadata, "test.entity");
        final byte[] bytes = os.toByteArray();
        System.out.println(os.toString());
    }

    @Test
    public void testGenerate2() throws Exception {
//        List<TableMetadata> tables = ReverseEntityUtils.reverseFormDatabase("wing4j", "jdbc:mysql://192.168.1.106:3306/wing4j", "root", "root");
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalJPADemoEntity.class);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        EntityUtils.generate(os, tableMetadata, "test.entity");
        final byte[] bytes = os.toByteArray();
        System.out.println(os.toString());
    }
}