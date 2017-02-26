package org.wing4j.orm.codegen;

import org.junit.Ignore;
import org.junit.Test;
import org.wing4j.orm.codegen.wing4j.NormalWing4jDemoEntity;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;
import org.wing4j.orm.entity.utils.ReverseEntityUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by woate on 2017/1/7.
 */
public class EntityCodeGenTest {
    @Test
    @Ignore
    public void testGenerate0() throws Exception {
        List<TableMetadata> tables = ReverseEntityUtils.reverseFormDatabase("wing4j", "jdbc:mysql://192.168.1.106:3306/wing4j", "root", "root");
        EntityCodeGen.generate(null, null, tables, "test.entity", "target/");
    }
    @Test
    public void testGenerate1() throws Exception {
//        List<TableMetadata> tables = ReverseEntityUtils.reverseFormDatabase("wing4j", "jdbc:mysql://192.168.1.106:3306/wing4j", "root", "root");
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class, false);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        EntityCodeGen.generate(null, null, os, tableMetadata, "test.entity");
        System.out.println(os.toString());
    }

    @Test
    public void testGenerate2() throws Exception {
//        List<TableMetadata> tables = ReverseEntityUtils.reverseFormDatabase("wing4j", "jdbc:mysql://192.168.1.106:3306/wing4j", "root", "root");
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class, false);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        EntityCodeGen.generate(null, null, os, tableMetadata, "test.entity");
        System.out.println(os.toString());
    }
}