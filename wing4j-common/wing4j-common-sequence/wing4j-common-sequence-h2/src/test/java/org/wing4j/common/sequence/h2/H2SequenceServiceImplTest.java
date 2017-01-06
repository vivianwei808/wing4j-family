package org.wing4j.common.sequence.h2;

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
public class H2SequenceServiceImplTest {
    @Autowired
    SequenceService sequenceService;
    @Test
    public void testNextval(){
        {
            int seq = sequenceService.nextval("wing4j1", "fa", "testCurval", "fixed");
            Assert.assertEquals(1, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j1", "fa", "testCurval", "fixed");
            Assert.assertEquals(2, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j1", "fa", "testCurval", "fixed1");
            Assert.assertEquals(1, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j", "fa", "testCurval", "fixed");
            Assert.assertEquals(3, seq);
        }
    }

    @Test
    public void testCurval() throws Exception {
        {
            int seq = sequenceService.nextval("wing4j", "fa", "testCurval", "fixed");
            Assert.assertEquals(1, seq);
        }
        {
            int seq = sequenceService.curval("wing4j", "fa", "testCurval", "fixed");
            Assert.assertEquals(1, seq);
        }
        {
            int seq = sequenceService.nextval("wing4j", "fa", "testCurval", "fixed");
            Assert.assertEquals(2, seq);
        }
        {
            int seq = sequenceService.curval("wing4j", "fa", "testCurval", "fixed");
            Assert.assertEquals(2, seq);
        }
        {
            int seq = sequenceService.curval("wing4j", "fa", "testCurval", "fixed");
            Assert.assertEquals(2, seq);
        }
    }
}