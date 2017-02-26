package org.wing4j.network.ftp.server;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/2/25.
 */
@Data
@ToString
public class Connecton {
    /**
     * 地址
     */
    String address;
    /**
     * 用户号
     */
    String userId;
    /**
     * 密码
     */
    String password;
    /**
     * 权限
     */
    final List<String> authorities = new ArrayList<>();
}
