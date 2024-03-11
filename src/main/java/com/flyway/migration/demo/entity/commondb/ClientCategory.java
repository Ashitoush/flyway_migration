package com.flyway.migration.demo.entity.commondb;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "client_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClientCategory_Seq_GEN")
    @SequenceGenerator(name = "ClientCategory_Seq_GEN", sequenceName = "ClientCategory_Seq", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "category_name_loc_lang", nullable = false)
    private String categoryNameLocLang;
}
