package org.wing4j.config.entity;

import org.wing4j.orm.*;

import java.util.Date;

/**
 * Created by wing4j on 2017/2/16.
 */
@Table(name = "FILE_CONFIG", prefix = "TB")
@Comment("文件配置信息")
public class FileConfigEntity {
    @PrimaryKey(strategy = PrimaryKeyStrategy.IDENTITY)
    @NumberColumn(name = "FILE_NO", nullable = false, type = NumberType.INTEGER)
    @Comment("文件号")
    Integer fileNo;

    @StringColumn(name = "FILE_NAME", nullable = false, type = StringType.VARCHAR)
    @Comment("文件名")
    String fileName;

    @StringColumn(name = "FILE_PATH", nullable = false, type = StringType.VARCHAR)
    @Comment("文件路径")
    String filePath;

    @StringColumn(name = "FILE", nullable = false, type = StringType.VARCHAR)
    @Comment("真实文件")
    String file;

    @DateColumn(name = "CREATE_DATE", nullable = false, type = DateType.TIMESTAMP)
    @Comment("创建日期")
    Date createDate;

    @DateColumn(name = "LAST_UPDATE_DATE", nullable = false, type = DateType.TIMESTAMP)
    @Comment("上次更新日期")
    Date lastUpdateDate;

    @StringColumn(name = "CREATE_UID", nullable = false, type = StringType.VARCHAR)
    @Comment("创建用户号")
    String createUid;

    @StringColumn(name = "LAST_UPDATE_UID", nullable = false, type = StringType.VARCHAR)
    @Comment("上次更新用户号")
    String lastUpdateUid;
}
