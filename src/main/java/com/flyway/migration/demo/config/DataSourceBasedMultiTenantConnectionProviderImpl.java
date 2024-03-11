package com.flyway.migration.demo.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final String DEFAULT_TENANT_ID = "flyway_master_db";
    private boolean init = false;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, DataSource> dataSourceMap = new HashMap<>();


    @PostConstruct
    public void load() {
        TenantDataSource tenantDataSource = applicationContext.getBean(TenantDataSource.class);

        dataSourceMap.put(DEFAULT_TENANT_ID, dataSource);

        dataSourceMap.putAll(tenantDataSource.getAll());

        dataSourceMap.entrySet().stream().filter(dataSource1 -> !dataSource1.getKey().equals(DEFAULT_TENANT_ID))
                .forEach(x -> {
                    Flyway flyway = Flyway.configure().dataSource(x.getValue()).locations("/db/schema", "/db/migration").load();
                    flyway.migrate();
                });
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSourceMap.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(Object o) {
        if (!init) {
            init = true;
        }

        return dataSourceMap.get(o) != null ? dataSourceMap.get(0) : dataSourceMap.get(DEFAULT_TENANT_ID);
    }
}
