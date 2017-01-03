package org.wing4j.common.sequence.mysql;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wing4j.common.sequence.SequenceService;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2016/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testContext-dev.xml")
public class MySQLSequenceServiceImplTest {
    @Autowired
    SequenceService sequenceService;
    @Test
    public void testNextval(){
        {
            int seq = sequenceService.nextval("wing4j", "fa", "ORDER_NO", "fixed");
            Assert.assertEquals(1, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j", "fa", "ORDER_NO", "fixed");
            Assert.assertEquals(2, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j", "fa", "ORDER_NO", "fixed1");
            Assert.assertEquals(1, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j", "fa", "ORDER_NO1", "fixed1");
            Assert.assertEquals(1, seq);
        }
    }

    @Test
    public void testCurval() throws Exception {
        {
            int seq = sequenceService.curval("wing4j", "fa", "ORDER_NO", "fixed");
            Assert.assertEquals(1, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j", "fa", "ORDER_NO", "fixed");
            Assert.assertEquals(2, seq);
        }
        {
            int seq = sequenceService.curval("wing4j", "fa", "ORDER_NO", "fixed");
            Assert.assertEquals(2, seq);
        }
        {
            int seq = sequenceService.curval("wing4j", "fa", "ORDER_NO", "fixed");
            Assert.assertEquals(2, seq);
        }
    }
}