package com.flyway.migration.demo.entity.commondb;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "client_master")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClientMaster_Seq_GEN")
    @SequenceGenerator(name = "ClientMaster_Seq_GEN", sequenceName = "ClientMaster_Seq", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "client_alias", nullable = false)
    private String clientAlias;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_name_loc_lang", nullable = false)
    private String clientNameLocLang;

    @Column(name = "client_image")
    private String clientImage;

    @Column(name = "license_key")
    private String licenseKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_Client_Category_Id"))
    private ClientCategory clientCategory;
}
