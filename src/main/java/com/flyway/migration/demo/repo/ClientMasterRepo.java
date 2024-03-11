package com.flyway.migration.demo.repo;

import com.flyway.migration.demo.entity.commondb.ClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClientMasterRepo extends JpaRepository<ClientMaster, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from client_master where id = ?1")
    void deleteClientMasterById(Integer id);
}
