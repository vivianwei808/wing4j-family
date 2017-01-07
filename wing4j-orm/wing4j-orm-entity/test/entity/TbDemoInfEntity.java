package test.entity;

import lombok.Data;
import org.wing4j.orm.*;
import org.wing4j.orm.mysql.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_demo_inf", schema = "wing4j")
@Comment("???")
@DataEngine(DataEngineType.InnoDB)
public class TbDemoInfEntity {
	
	@StringColumn(name = "SERAIL_NO" ,length = 255 ,type = StringType.VARCHAR ,nullable = false )
	@Comment("????")
	String serailNo;
	
	@StringColumn(name = "NAME" ,length = 255 ,type = StringType.VARCHAR ,nullable = true )
	@Comment("??")
	String name;
	
	@NumberColumn(name = "AGE" ,type = NumberType.INTEGER ,nullable = false ,defaultValue = "0" )
	@Comment("??")
	Integer age;
	
	@DateColumn(name = "CREATE_DATE" ,type = DateType.TIME ,nullable = true )
	@Comment("????")
	Date createDate;
	
	@DateColumn(name = "LAST_UPDATE_DATE" ,type = DateType.TIMESTAMP ,nullable = true ,defaultValue = "1971-01-01 00:00:00" )
	@Comment("??????")
	Date lastUpdateDate;

}
