package com.flyway.migration.demo.entity.commondb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@Table(name = "data_source_config")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "driver_class_name")
    private String driverClassName;

    @Column(name = "initialize")
    private boolean initialize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_master_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_Data_Source_Config_Client_Master_Id"))
    private ClientMaster clientMaster;
}
