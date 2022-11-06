package dataLayer.dbManagers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBInitializer {

    private Connection dbServerConnection;

    private final String dbServerUri;

    private final String dbName;

    private final String userName;

    private final String password;

    public DBInitializer(String dbServerUri, String dbName, String userName, String password) {
        this.dbServerUri = dbServerUri;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }

    public void initialize() throws SQLException, IOException {

        this.dbServerConnection = DriverManager.getConnection(dbServerUri, userName, password);
        ensureCreated();
    }

    private void ensureCreated() throws IOException, SQLException {

        var schemaCreationBatchLine = "CREATE SCHEMA IF NOT EXISTS `" + dbName + "`;";
        var useBatchLine = "USE `" + dbName + "`;";
        var fileContent = Files.readString(Path.of(System.getProperty("user.dir")
                + "/ServerApp/DataLayer/src/main/resources/GenerationScripts/GenerationScript.sql"));
        var allScript = schemaCreationBatchLine + useBatchLine +fileContent;
        var batches = allScript.split(";");
        var preparedStatement = dbServerConnection.createStatement();
        for (var batch : batches) {
            preparedStatement.addBatch(batch);
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }
}
