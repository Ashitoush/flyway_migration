package com.flyway.migration.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TenantInfoDto {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String tenantId;

    private String name;
}