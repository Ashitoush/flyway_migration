package com.flyway.migration.demo.service;

import com.flyway.migration.demo.entity.commondb.ClientMaster;

public interface ClientService {
    Integer createClient(ClientMaster clientMaster);
}
