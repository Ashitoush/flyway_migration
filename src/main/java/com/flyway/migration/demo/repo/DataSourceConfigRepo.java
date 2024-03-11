package com.flyway.migration.demo.repo;

import com.flyway.migration.demo.entity.commondb.DataSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DataSourceConfigRepo extends JpaRepository<DataSourceConfig, Integer> {
    Optional<DataSourceConfig> findByName(String name);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from data_source_config where client_master_id = ?1")
    void deleteDataSourceConfigByClientMaster(Integer clientId);
}
