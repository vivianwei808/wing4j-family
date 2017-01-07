package org.wing4j.orm.entity.utils.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.wing4j.orm.entity.metadata.TableMetadata;
import org.wing4j.orm.entity.utils.EntityExtracteUtils;

/**
 * Created by wing4j on 2016/12/17.
 */
public class JpaEntityExtracteUtilsTest {

    @Test(expected = RuntimeException.class)
    public void testExtractTable_jpa_no_pk() throws Exception {
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NoPkJPADemoEntity.class, false);
    }

    @Test
    public void testExtractTable_jpa_normal() throws Exception {
        TableMetadata tableMetadata = EntityExtracteUtils.extractTable(NormalJPADemoEntity.class, false);
        Assert.assertEquals(11, tableMetadata.getColumnMetadatas().size());
        Assert.assertEquals("VARCHAR(36)", tableMetadata.getColumnMetadatas().get("SERIAL_NO").getDataType());
        Assert.assertEquals("VARCHAR(12)", tableMetadata.getColumnMetadatas().get("COL1").getDataType());
        Assert.assertEquals("DECIMAL(10,3)", tableMetadata.getColumnMetadatas().get("COL2").getDataType());
        Assert.assertEquals("INTEGER", tableMetadata.getColumnMetadatas().get("COL3").getDataType());
        Assert.assertEquals("INTEGER", tableMetadata.getColumnMetadatas().get("COL4").getDataType());
        Assert.assertEquals("DATETIME", tableMetadata.getColumnMetadatas().get("COL5").getDataType());
        Assert.assertEquals("DATE", tableMetadata.getColumnMetadatas().get("COL6").getDataType());
        Assert.assertEquals("TIME", tableMetadata.getColumnMetadatas().get("COL7").getDataType());
        Assert.assertEquals("TIMESTAMP", tableMetadata.getColumnMetadatas().get("COL8").getDataType());
        Assert.assertEquals("TINYINT", tableMetadata.getColumnMetadatas().get("COL9").getDataType());
        Assert.assertEquals("TINYINT", tableMetadata.getColumnMetadatas().get("COL10").getDataType());
    }

}