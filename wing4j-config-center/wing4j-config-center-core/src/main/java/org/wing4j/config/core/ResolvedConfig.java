package org.wing4j.config.core;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by wing4j on 2017/2/15.
 */
@Data
@ToString
@Builder
@EqualsAndHashCode
public class ResolvedConfig {
    Config config;
    Project project;
    String profile;
}
