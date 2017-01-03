package org.wing4j.orm.test.spring.datasource;

import lombok.extern.slf4j.Slf4j;
import org.wing4j.common.utils.EnvironmentUtils;
import org.wing4j.orm.test.Wing4jTestRuntimeException;

@Slf4j
public class TestDataSourceUtils {

    static ThreadLocal<String> currentDataSource = new ThreadLocal<String>();
    static final String H2_DATASOURCE = "h2DataSource";
    static final String MYSQL_DATASOURCE = "mysqlDataSource";

    public static String setDevDateSourceName(String name) {
        currentDataSource.set(name);
        return currentDataSource.get();
    }

    public static String lookupDataSource() {
        EnvironmentUtils.Environment environment = EnvironmentUtils.determineRuntime();
        if (MYSQL_DATASOURCE.equals(currentDataSource.get())
                && EnvironmentUtils.Environment.MAVEN_JUNIT == environment) {
            log.error("maven runtime environment not allow use mySql database!");
            throw new Wing4jTestRuntimeException("maven runtime environment not allow use mySql database!");
        }
        if ((MYSQL_DATASOURCE.equals(currentDataSource.get())
                || H2_DATASOURCE.equals(currentDataSource.get()))) {
            log.info("unit test use :{}!", currentDataSource.get());
            return currentDataSource.get();

        }
        if (EnvironmentUtils.Environment.MAVEN_JUNIT == environment) {
            log.info("unit test use :{}!", H2_DATASOURCE);
            return H2_DATASOURCE;
        }
        if (EnvironmentUtils.Environment.IDE_JUNIT == environment) {
            log.info("unit test use :{}!", H2_DATASOURCE);
            return H2_DATASOURCE;
        }
        return H2_DATASOURCE;
    }
}
