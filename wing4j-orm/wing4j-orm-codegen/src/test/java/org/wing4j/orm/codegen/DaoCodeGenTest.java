package org.wing4j.orm.codegen;

import org.junit.Test;
import org.wing4j.orm.codegen.wing4j.NormalWing4jDemoEntity;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/7.
 */
public class DaoCodeGenTest {

    @Test
    public void testGenerate() throws Exception {
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class, false);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        DaoCodeGen.generate(DaoCodeGen.DEFAULT_HEAD, null, os, tableMetadata, "test.dao", "org.wing4j.orm.codegen.wing4j", "NormalWing4jDemoDAO");
        System.out.println(os.toString());
    }

    @Test
    public void testGenerate1() throws Exception {

    }
}