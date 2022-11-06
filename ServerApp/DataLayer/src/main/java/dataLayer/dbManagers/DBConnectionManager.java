package dataLayer.dbManagers;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class DBConnectionManager {

    private static final AtomicReference<ArrayList<Connection>> dbConnections;

    private static final AtomicReference<ArrayList<Boolean>> isDbConnectionFree;

    private static final String dbServerConnectionString;

    private static final String databaseConnectionString;

    private static final String dbName;

    private static final String userName;

    private static final String password;

    private static final int maxAmountOfDbConnections;

    private static Properties getPropertiesFromConfig() throws IOException {

        var properties = new Properties();
        String propFileName = "ServerApp/DataLayer/src/main/resources/config.properties";
        var inputStream = new FileInputStream(propFileName);
        if (inputStream == null)
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        properties.load(inputStream);
        return properties;
    }

    static {

        try {
            var properties = getPropertiesFromConfig();
            dbServerConnectionString = properties.getProperty("dbServerConnectionString");
            dbName = properties.getProperty("dbName");
            userName = properties.getProperty("userName");
            password = properties.getProperty("password");
            maxAmountOfDbConnections = Integer.parseInt(properties.getProperty("maxAmountOfDbConnections"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        databaseConnectionString = dbServerConnectionString + dbName;

        var dbConnectionsList = new ArrayList<Connection>();
        var isDbConnectionFreeList = new ArrayList<Boolean>();

        dbConnections = new AtomicReference<>(dbConnectionsList);
        isDbConnectionFree = new AtomicReference<>(isDbConnectionFreeList);

        for (int i = 0; i < 5; i++) {
            isDbConnectionFreeList.add(true);
        }

        try {
            DBInitializer initializer = new DBInitializer(dbServerConnectionString,
                    dbName, userName, password);
            initializer.initialize();

            for (int i = 0; i < 5; i++) {

                dbConnectionsList.add(DriverManager.getConnection(databaseConnectionString,
                        userName, password));
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static synchronized Connection getFreeConnection() {

        int indexOfFreeConnection = isDbConnectionFree.get().indexOf(true);
        if (indexOfFreeConnection != -1) {

            isDbConnectionFree.get().set(indexOfFreeConnection, false);
            return dbConnections.get().get(indexOfFreeConnection);
        } else {
            return null;
        }
    }

    private static synchronized Connection createNewConnection() throws SQLException {

        if (dbConnections.get().size() < maxAmountOfDbConnections) {

            var newConnection = DriverManager.getConnection(databaseConnectionString, userName, password);
            dbConnections.get().add(newConnection);
            isDbConnectionFree.get().add(false);
            return newConnection;
        } else {
            return null;
        }
    }

    public synchronized Connection getConnection() throws SQLException, InterruptedException {

        var connection = getFreeConnection();
        if (connection != null) return connection;
        connection = createNewConnection();
        if (connection != null) return connection;
        wait(3000);
        return getFreeConnection();
    }

    public synchronized void releaseConnection(Connection connection) {

        isDbConnectionFree.get().set(dbConnections.get().indexOf(connection), true);
        notify();
    }
}
