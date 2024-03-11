package com.flyway.migration.demo.config;

import com.flyway.migration.demo.dto.TenantInfoDto;
import com.flyway.migration.demo.entity.commondb.DataSourceConfig;
import com.flyway.migration.demo.repo.DataSourceConfigRepo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientDataSourceUtil {

    private final DataSourceConfigRepo dataSourceConfigRepo;

    public DataSource fetchDataSourceForClient(String clientName) {
        Optional<DataSourceConfig> dataSource = dataSourceConfigRepo.findByName(clientName);
        if (dataSource.isEmpty()) {
            throw new RuntimeException("Cannot Find Data Source for the client");
        }

        TenantInfoDto tenantInfoDto = TenantInfoDto.builder()
                .name(dataSource.get().getName())
                .driverClassName(dataSource.get().getDriverClassName())
                .url(dataSource.get().getUrl())
                .username(dataSource.get().getUsername())
                .password(dataSource.get().getPassword())
                .build();

        HikariConfig hikariConfig = DataSourceConfigUtil.setDataSourceEnvConfig(tenantInfoDto);
        return new HikariDataSource(hikariConfig);
    }
}
