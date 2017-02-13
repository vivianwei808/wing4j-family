package org.wing4j.orm.entity.utils;

import org.junit.Test;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.wing4j.NormalWing4jDemoEntity;

import java.io.ByteArrayOutputStream;

/**
 * Created by wing4j on 2016/12/17.
 */
public class SqlScriptUtilsTest {

    @Test
    public void testGenerateCreateTable() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class, false);
        SqlScriptUtils.generateCreateTable(os, tableMetadata, WordMode.lowerCase, WordMode.lowerCase, true);
        System.out.println(os.toString());
    }

    @Test
    public void testGenerateDropTable() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class, false);
        SqlScriptUtils.generateDropTable(os, tableMetadata, WordMode.lowerCase, WordMode.lowerCase, true);
        System.out.println(os.toString());
    }

    @Test
    public void testGenerateTruncateTable() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class, false);
        SqlScriptUtils.generateTruncateTable(os, tableMetadata, WordMode.lowerCase, WordMode.lowerCase);
        System.out.println(os.toString());
    }

    @Test
    public void testPrimaryKey() throws Exception {

    }

    @Test
    public void testGenreateSqlHead() throws Exception {
        String sql = SqlScriptUtils.genreateSqlHead(NormalWing4jDemoEntity.class, WordMode.lowerCase, WordMode.lowerCase, false);
        System.out.println(sql);
    }
}