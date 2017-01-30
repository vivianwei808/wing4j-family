这是一个markdown文件的演示例子
```wing4j configure
--方言设置
--@dialect wing4j
--命名空间
--@namespace org.wing4j.orm
```
#[查询用户信息](selectDemo)#

这是一个markdown文件的演示例子
```wing4j param
--@flushCacheRequired=true //强制刷新缓存
--使用缓存关闭
--@useCache=false
--获取记录大小
--@fetchSize=1
--超时时间
--@timeout=1000
--语句注释
--@comment=备注
```
```sql
select * 
from tb_demo_wing4j_inf t
where t.col1=#{col2:VRACHAR}
/*#     if col2 == null                  */
and col2=#{col2:VRACHAR}
/*#     fi                               */
/*#     if col3 is not null              */
and col3=#{col2:NUMBER}
/*#     fi                               */
```

[新增数据](insert)
================================

```sql
insert into table1(col1, col2, col3)
values('col1', 'col2', 3)
```

[根据用户ID更新数据](updateById)
================================

```sql
update table t
set t.col1 = #{col2:VRACHAR}
where t.col1='col1'
/*#     if col2 is not null              */
and col2=$col2:VRACHAR$
/*#     fi                               */
/*#     if col3 is not null              */
and col3=$col3:NUMBER$
/*#     fi                               */
```


[根据用户ID删除数据](deleteById)
================================

```sql
delete from table t
where t.col1='col1'
/*#     if col2 is not null              */
and col2=$col2:VRACHAR$
/*#     fi                               */
/*#     if col3 is not null              */
and col3=$col3:NUMBER$
/*#     fi                               */
```