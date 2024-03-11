package com.flyway.migration.demo.service.impl;

import com.flyway.migration.demo.config.DatabaseManager;
import com.flyway.migration.demo.entity.commondb.ClientMaster;
import com.flyway.migration.demo.repo.ClientMasterRepo;
import com.flyway.migration.demo.repo.DataSourceConfigRepo;
import com.flyway.migration.demo.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientMasterRepo clientMasterRepo;
    private final DataSourceConfigRepo dataSourceConfigRepo;
    private final DatabaseManager databaseManager;

    @Override
    public Integer createClient(ClientMaster clientMaster) {
        clientMaster = clientMasterRepo.save(clientMaster);
        createClientAndMigrate(clientMaster);
        return clientMaster.getId();
    }

//    Creates new client, database and migrates all the schemas to the new database
//    and seeds initial data to the new database
    public void createClientAndMigrate(ClientMaster clientMaster) {
        String databaseName = databaseManager.createDatabaseName(clientMaster.getClientName());
        boolean isErrorOccured = false;

        try {
            /**
             Creates New Database for client and also inserts database configuration data
             for the new client database in data_source_config
             */
            DataSource dataSource = databaseManager.createDatabaseForClient(clientMaster.getClientName(), clientMaster.getId(), databaseName);

            Flyway flyway = Flyway.configure().dataSource(dataSource).locations("/db/schema", "/db/migration", "/db/seeder").load();
            flyway.migrate();

        } catch (Exception exception) {
            exception.printStackTrace();
            isErrorOccured = true;
        } finally {
            if (isErrorOccured) {
                dataSourceConfigRepo.deleteDataSourceConfigByClientMaster(clientMaster.getId());
                clientMasterRepo.deleteClientMasterById(clientMaster.getId());
                databaseManager.dropDatabaseForClient(databaseName);
            }
        }
    }
}
