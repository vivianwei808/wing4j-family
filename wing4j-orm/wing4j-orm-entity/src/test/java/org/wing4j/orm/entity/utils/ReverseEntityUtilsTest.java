package org.wing4j.orm.entity.utils;

import org.junit.Ignore;
import org.junit.Test;
import org.wing4j.orm.entity.metadata.TableMetadata;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/1/2.
 */
public class ReverseEntityUtilsTest {

    @Test
    @Ignore
    public void testGenerateFormDatabase() throws Exception {
        List<TableMetadata> tables = ReverseEntityUtils.reverseFormDatabase("wing4j", "jdbc:mysql://192.168.1.106:3306/wing4j", "root", "root");
        System.out.println(tables);
    }
}