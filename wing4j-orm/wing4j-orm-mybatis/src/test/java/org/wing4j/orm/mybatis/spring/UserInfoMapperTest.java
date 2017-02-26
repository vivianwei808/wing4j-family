package org.wing4j.orm.mybatis.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.wing4j.orm.WordMode;
import org.wing4j.orm.mybatis.mapper.builder.BaseTest;
import org.wing4j.orm.mybatis.mapper.builder.DemoEntity;
import org.wing4j.test.CreateTable;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

@ContextConfiguration(locations = {"classpath*:testContext-orm.xml" })
public class UserInfoMapperTest extends BaseTest {
    @Autowired
    DataSource dataSource;

    @CreateTable(entities = DemoEntity.class, sqlMode = WordMode.upperCase, keywordMode = WordMode.upperCase)
    @Test
    public void testInsert() {
        UserInfoCrudMapper userInfoCrudMapper = getBean(UserInfoCrudMapper.class);
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        {
            userInfoEntity.setCol1("col1");
            userInfoEntity.setCol2(new BigDecimal("111"));
            for (int i = 0; i < 1; i++) {
                userInfoEntity.setCol3(i);
                userInfoCrudMapper.insert(userInfoEntity);
                userInfoCrudMapper.insertSelective(userInfoEntity);
            }
            List<UserInfoEntity> list = userInfoCrudMapper.selectAll();
            System.out.println(list);
        }
        {
            userInfoEntity.setSerialNo(null);
            userInfoEntity.setCol1("col1_1");
            userInfoEntity.setCol2(new BigDecimal("222"));
            userInfoEntity.setCol3(23);
            userInfoCrudMapper.insert(userInfoEntity);
            Assert.assertNotNull(userInfoEntity.getSerialNo());
        }
        {
            List<UserInfoEntity> list = userInfoCrudMapper.selectAll();
            Assert.assertEquals(3, list.size());
        }
        {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setCol1("col1");
            userInfoEntity.setCol3(23);
            List<UserInfoEntity> list = userInfoCrudMapper.selectAnd(userInfoEntity);
            Assert.assertEquals(0, list.size());
        }
        {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setCol1("col1");
            userInfoEntity.setCol2(new BigDecimal("111"));
            List<UserInfoEntity> list = userInfoCrudMapper.selectAnd(userInfoEntity);
            Assert.assertEquals(2, list.size());
        }
        {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setCol2(new BigDecimal("222"));
            userInfoEntity.setCol1("col1");
            List<UserInfoEntity> list = userInfoCrudMapper.selectOr(userInfoEntity);
            Assert.assertEquals(3, list.size());
        }
        {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setCol2(new BigDecimal("222"));
            userInfoEntity.setCol1("col1");
            int cnt = userInfoCrudMapper.deleteOr(userInfoEntity);
            Assert.assertEquals(3, cnt);
            int count = userInfoCrudMapper.countAll();
            Assert.assertEquals(0, count);
        }

    }
}
