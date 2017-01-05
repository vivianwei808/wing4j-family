package test.entity;

import lombok.Data;
import org.wing4j.orm.*;
import org.wing4j.orm.mysql.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_demo_inf", schema = "wing4j")
@Comment("测试表")
@DataEngine(DataEngineType.InnoDB)
public class TbDemoInf {
	
	@NumberColumn(name = "SERAIL_NO" ,type = NumberType.INTEGER ,nullable = false )
	@Comment("物理主键")
	Integer serailNo;
	
	@StringColumn(name = "NAME" ,length = 255 ,type = StringType.VARCHAR ,nullable = true )
	@Comment("姓名")
	String name;
	
	@NumberColumn(name = "AGE" ,type = NumberType.INTEGER ,nullable = false ,defaultValue = "0" )
	@Comment("年龄")
	Integer age;
	
	@DateColumn(name = "CREATE_DATE" ,type = DateType.TIME ,nullable = true )
	@Comment("创建日期")
	Date createDate;
	
	@DateColumn(name = "LAST_UPDATE_DATE" ,type = DateType.TIMESTAMP ,nullable = true ,defaultValue = "1971-01-01 00:00:00" )
	@Comment("最近更新日期")
	Date lastUpdateDate;

}
