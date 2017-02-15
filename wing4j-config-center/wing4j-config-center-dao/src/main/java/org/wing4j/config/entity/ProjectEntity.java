package org.wing4j.config.entity;

import java.util.Date;

/**
 * Created by wing4j on 2017/2/15.
 */
public class ProjectEntity {
    Integer id;
    String groupId;
    String artifactId;
    String version;
    Integer ownerGroup;
    String description;
    Boolean bPublic;
    Boolean bAllowDependency;
}
