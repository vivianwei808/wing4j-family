package org.wing4j.orm.test.spring.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TestDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = TestDataSourceUtils.lookupDataSource();
        return dataSource;
    }
}
