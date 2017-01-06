package org.wing4j.common.logtrack;

import org.wing4j.common.utils.StringUtils;

import java.util.Arrays;

/**
 * Created by woate on 2017/1/6.
 */
public class PlaceHolder {
    String format;
    Object[] args;

    @Override
    public String toString() {
        return StringUtils.format(format, args);
    }
}
