package org.wing4j.orm.entity.utils;

import org.junit.Assert;
import org.junit.Test;
import org.wing4j.orm.entity.metadata.TableMetadata;

/**
 * Created by wing4j on 2016/12/17.
 */
public class EntityExtracteUtilsTest {

    @Test(expected = RuntimeException.class)
    public void testExtractTable_jpa_no_pk() throws Exception {
        TableMetadata tableMetadata =  EntityExtracteUtils.extractTable(NoPkJPADemoEntity.class);
    }
    @Test
    public void testExtractTable_jpa_normal() throws Exception {
        TableMetadata tableMetadata =  EntityExtracteUtils.extractTable(NormalJPADemoEntity.class);
        Assert.assertEquals(4, tableMetadata.getColumnMetadatas().size());
        Assert.assertEquals("VARCHAR(36)", tableMetadata.getColumnMetadatas().get("SERIAL_NO").getDataType());
        Assert.assertEquals("VARCHAR(12)", tableMetadata.getColumnMetadatas().get("COL1").getDataType());
        Assert.assertEquals("DECIMAL(10,3)", tableMetadata.getColumnMetadatas().get("COL2").getDataType());
        Assert.assertEquals("INTEGER", tableMetadata.getColumnMetadatas().get("COL3").getDataType());
    }

    @Test
    public void testExtractTable_wing4j_normal() throws Exception {
        TableMetadata tableMetadata =  EntityExtracteUtils.extractTable(NormalWing4jDemoEntity.class);
        Assert.assertEquals(4, tableMetadata.getColumnMetadatas().size());
        Assert.assertEquals("CHAR(36)", tableMetadata.getColumnMetadatas().get("SERIAL_NO").getDataType());
        Assert.assertEquals("CHAR(12)", tableMetadata.getColumnMetadatas().get("COL1").getDataType());
        Assert.assertEquals("DECIMAL(10,3)", tableMetadata.getColumnMetadatas().get("COL2").getDataType());
        Assert.assertEquals("INTEGER", tableMetadata.getColumnMetadatas().get("COL3").getDataType());
    }
}