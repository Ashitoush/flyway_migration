package com.flyway.migration.demo.config;

import com.flyway.migration.demo.dto.TenantInfoDto;
import com.zaxxer.hikari.HikariConfig;

public class DataSourceConfigUtil {

    public static HikariConfig setDataSourceEnvConfig(TenantInfoDto tenantInfoDto) {
        HikariConfig hikariConfig = new HikariConfig();

        String driverClassName = tenantInfoDto.getDriverClassName();
        String url = tenantInfoDto.getUrl();
        String username = tenantInfoDto.getUsername();
        String password = tenantInfoDto.getPassword();
        int minimumIdle = 5;
        int maximumPoolSize = 10;
        int idleTimeout = 10000;
        int maxLifeTime = 300000;
        int connectionTimeout = 30000;

        if (driverClassName != null) {
            hikariConfig.setDriverClassName(driverClassName);
        }

        if (url != null) {
            hikariConfig.setJdbcUrl(url);
        }

        if (username != null) {
            hikariConfig.setUsername(username);
        }

        if (password != null) {
            hikariConfig.setPassword(password);
        }

        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifeTime);
        hikariConfig.setConnectionTimeout(connectionTimeout);

        return hikariConfig;
    }
}
