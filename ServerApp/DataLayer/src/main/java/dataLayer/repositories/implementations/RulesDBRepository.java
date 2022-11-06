package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.RulesRepository;
import databaseEntities.Classes.Rule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RulesDBRepository implements RulesRepository {


    private final DBConnectionManager dbConnectionManager;

    public RulesDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private Rule convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Rule();
        return convertResultSetToObj(resultSet);
    }

    private Rule convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Rule();
            obj.setId(resultSet.getInt("id"));
            obj.setText(resultSet.getString("text"));
        return obj;
    }

    private List<Rule> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Rule>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from rules;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(Rule rule) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement("INSERT INTO rules" +
                " (text) " +
                "VALUES (?);");

        insertStatement.setString(1, rule.getText());
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
    }

    @Override
    public void update(Rule newVersion) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE rules SET " +
                        "text = ? where id = ?");
        updateStatement.setString(1, newVersion.getText());
        updateStatement.setInt(2, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from rules where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Rule getById(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM rules where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Rule> getAll() throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM rules;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}