# wing4j orm markdown

wing4j orm markdown项目用于支持markdown方式SQL语句管理方式

```markdown
​```configure
--方言设置
--@dialect wing4j
--命名空间
--@namespace org.wing4j.orm
​```
[查询用户信息](selectDemo)
================================

​```params
--强制刷新缓存
--@flushCacheRequired=true
--使用缓存关闭
--@useCache=false
--获取记录大小
--@fetchSize=1
--超时时间
--@timeout=1000
--语句注释
--@comment=备注
​```
​```sql
select * 
from tb_demo_wing4j_inf t
where t.col1=#{col2:VRACHAR}
/*#     if col2 == null                  */
and col2=#{col2:VRACHAR}
/*#     fi                               */
/*#     if col3 is not null              */
and col3=#{col2:NUMBER}
/*#     fi                               */
​```

[新增数据](insert)
================================

​```sql
insert into table1(col1, col2, col3)
values('col1', 'col2', 3)
​```

[根据用户ID更新数据](updateById)
================================

​```sql
update table t
set t.col1 = #{col2:VRACHAR}
where t.col1='col1'
/*#     if col2 is not null              */
and col2=$col2:VRACHAR$
/*#     fi                               */
/*#     if col3 is not null              */
and col3=$col3:NUMBER$
/*#     fi                               */
​```


[根据用户ID删除数据](deleteById)
================================

​```sql
delete from table t
where t.col1='col1'
/*#     if col2 is not null              */
and col2=$col2:VRACHAR$
/*#     fi                               */
/*#     if col3 is not null              */
and col3=$col3:NUMBER$
/*#     fi                               */
​```
```



## 当前md文件配置

```
​```configure
--方言设置
--@dialect wing4j
--命名空间
--@namespace org.wing4j.orm
​```
```

​	```configure用于引导配置开始

​	```用于引导配置结束

​	--用于引导注释

​	--@用于引导参数项

### dialect

这个参数用于设置当前md文件使用的别名，目前只有wing4j一个设置项。

### namespace

这个参数用于设置当前的命名空间，最好对应对应的dao的包名。



## 语句节点

```
[查询用户信息](selectDemo)
================================

​```params
--强制刷新缓存
--@flushCacheRequired=true
--使用缓存关闭
--@useCache=false
--获取记录大小
--@fetchSize=1
--超时时间
--@timeout=1000
--语句注释
--@comment=备注
​```
​```sql
select * 
from tb_demo_wing4j_inf t
where t.col1=#{col2:VRACHAR}
/*#     if col2 == null                  */
and col2=#{col2:VRACHAR}
/*#     fi                               */
/*#     if col3 is not null              */
and col3=#{col2:NUMBER}
/*#     fi                               */
​```
```

		[用于引导语句块开始，下语句的开始或者md文件最后行表明当前语句的结束。
		[]中的内容表明当前语句的描述。
		()中的内容表明当前语句的ID,与namespace构成唯一的标识。
		==用于引导语句内容的开始
		```params用于引导语句参数开始,代码块中存放当前语句对应的配置参数
		flushCacheRequired设置是否强制刷新缓存
		useCache设置是否使用缓存
		fetchSize设置每次获取的记录数
		timeout设置对应SQL语句执行超时时间
		```sql用于引导SQL语句开始
		/*#用于引导SQL语句命令
		SQL语句命令目前有if，if用于设置if块包围的SQL语句根据 if中的条件是否包含在实际执行的SQL语句中。

