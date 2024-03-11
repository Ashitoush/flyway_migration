package com.flyway.migration.demo.repo;

import com.flyway.migration.demo.entity.commondb.ClientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCategoryRepo extends JpaRepository<ClientCategory, Integer> {

}
