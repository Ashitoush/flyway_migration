package com.flyway.migration.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
@RequiredArgsConstructor
public class DatabaseManager {

    private final DataSource dataSource;
    private final ClientDataSourceUtil clientDataSourceUtil;
    private static final String PASSWORD = "postgres";
    private static final String DEFAULT_DATABASE = "flyway_master_db";

    /**
     * Creates a new database for the new client and also inserts the client information in the data_source_config table
     * in the default database and creates the datasource object for the new database of the client and returns it.
     */
    public DataSource createDatabaseForClient(String clientName, Integer clientMasterId, String databaseName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String createDatabaseSql = "CREATE DATABASE " + databaseName;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createDatabaseSql);
            }

            String url = connection.getMetaData().getURL().replace(DEFAULT_DATABASE, databaseName) + "?ApplicationName=bims";
            String userName = connection.getMetaData().getUserName();
            String driverName = DriverManager.getDriver(url).getClass().getName();

            int id = 0;
            try (PreparedStatement idStatement = connection.prepareStatement("SELECT COALESCE(MAX(id), 0) + 1 FROM data_source_config")) {
                try (ResultSet resultSet = idStatement.executeQuery()) {
                    if (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                }
            }

            String insertSql = "INSERT INTO public.data_source_config (id, driver_class_name, initialize, name, password, url, username, client_master_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, driverName);
                preparedStatement.setBoolean(3, true);
                preparedStatement.setString(4, clientName);
                preparedStatement.setString(5, PASSWORD);
                preparedStatement.setString(6, url);
                preparedStatement.setString(7, userName);
                preparedStatement.setInt(8, clientMasterId);

                preparedStatement.executeUpdate();
            }
        }

        return clientDataSourceUtil.fetchDataSourceForClient(clientName);
    }

    /**
     *
     * Drops the database of the new client which was recently created
     * when any error occurs while registering the new client.
     */
    public void dropDatabaseForClient(String databaseName) {
        try (Connection connection = dataSource.getConnection()) {
            /**
             * Need to terminate all the active connection to the database being dropped,
             * so that there is no issue while dropping the database.
             */
            String terminateSqlConnection = "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity " +
                    "WHERE pg_stat_activity.datname = ? AND pid <> pg_backend_pid()";
            try (PreparedStatement terminateStatement = connection.prepareStatement(terminateSqlConnection)) {
                terminateStatement.setString(1, databaseName);
                terminateStatement.executeQuery();
            }

            String dropDatabaseSql = "DROP DATABASE " + databaseName;
            try (Statement dropStatement = connection.createStatement()) {
                dropStatement.executeUpdate(dropDatabaseSql);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String createDatabaseName(String clientName) {
        return clientName.trim()
                .replaceAll("[^a-zA-Z0-9_\\s-]", "")
                .replace(' ', '_')
                .concat("_db").toLowerCase();
    }
}
