package org.wing4j.common.utils;

import lombok.Data;

/**
 * Created by wing4j on 2016/12/17.
 */
@Data
public class DemoJavaBean2 extends DemoJavaBean1{
    transient String field2;
}
