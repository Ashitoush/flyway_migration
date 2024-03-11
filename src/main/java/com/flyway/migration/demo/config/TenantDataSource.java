package com.flyway.migration.demo.config;

import com.flyway.migration.demo.dto.TenantInfoDto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TenantDataSource {

    private Map<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public Map<String, DataSource> getAll() {
        List<TenantInfoDto> tenantInfoDtoList = getTenantInfo(null);
        Map<String, DataSource> result = new HashMap<>();
        for (TenantInfoDto tenantInfoDto : tenantInfoDtoList) {
            DataSource dataSource1 = getDataSource(tenantInfoDto.getName());
            result.put(tenantInfoDto.getName(), dataSource1);
        }

        return result;
    }

    private DataSource getDataSource(String name) {

        if (dataSources.get(name) != null) {
            return dataSources.get(name);
        }

        List<TenantInfoDto> tenantInfoDtos = getTenantInfo(name);
        DataSource dataSource1 = createDataSource(tenantInfoDtos.get(0));

        if (dataSource1 != null) {
            dataSources.put(name, dataSource1);
        }

        return dataSource1;
    }

    private DataSource createDataSource(TenantInfoDto tenantInfoDto) {
        HikariConfig hikariConfig = DataSourceConfigUtil.setDataSourceEnvConfig(tenantInfoDto);
        return new HikariDataSource(hikariConfig);
    }

    private List<TenantInfoDto> getTenantInfo(String tenantId) {
        String query = "select dsc.name as name,\n" +
                "       dsc.username as username,\n" +
                "       dsc.password as password,\n" +
                "       dsc.url as url,\n" +
                "       dsc.driver_class_name as driverClassName\n" +
                "from data_source_config dsc";

        if (tenantId != null) {
            query += " where dsc.name = ?";
        }

        List<TenantInfoDto> tenantInfoDtos = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query)) {
            if (tenantId != null) {
                preparedStatement.setString(1, tenantId);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TenantInfoDto tenantInfoDto = TenantInfoDto.builder()
                        .name(resultSet.getString("name"))
                        .driverClassName(resultSet.getString("driverClassName"))
                        .url(resultSet.getString("url"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .build();
                tenantInfoDtos.add(tenantInfoDto);
            }
            resultSet.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tenantInfoDtos;
    }


}
