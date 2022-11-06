package dataLayer.repositories.implementations;

import databaseEntities.Classes.DocumentType;
import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.DocumentTypesRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentTypesDBRepository implements DocumentTypesRepository {

    private final DBConnectionManager dbConnectionManager;

    public DocumentTypesDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private DocumentType convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        return convertResultSetToObj(resultSet);
    }

    private DocumentType convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new DocumentType();
        if (!resultSet.isAfterLast()) {

            obj.setId(resultSet.getInt("id"));

            obj.setTypeName(resultSet.getString("typeName"));
        }
        return obj;
    }

    private List<DocumentType> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<DocumentType>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    @Override
    public void create(DocumentType documentType) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO document_types" +
                        " (typeName) " +
                        "VALUES (?);");

        insertStatement.setString(1, documentType.getTypeName());
        insertStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void update(DocumentType newVersion) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE document_types SET typeName = ? where id = ?");
        updateStatement.setString(1, newVersion.getTypeName());
        updateStatement.setInt(2, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from document_types where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public DocumentType getById(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM document_types where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<DocumentType> getAll() throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM document_types;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
