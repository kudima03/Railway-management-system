package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.TrainTypesRepository;
import databaseEntities.Classes.TrainType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainTypesDBRepository implements TrainTypesRepository {

    private final DBConnectionManager dbConnectionManager;

    public TrainTypesDBRepository() {

        dbConnectionManager = new DBConnectionManager();
    }

    private TrainType convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var trainType = new TrainType();
            trainType.setId(resultSet.getInt("id"));
            trainType.setName(resultSet.getString("name"));
            trainType.setCostForStation(resultSet.getFloat("costForStation"));
        return trainType;
    }

    private List<TrainType> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<TrainType>();
        resultSet.beforeFirst();
        while (resultSet.next()){

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private TrainType convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new TrainType();
        return convertResultSetToObj(resultSet);
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from train_types;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }
    @Override
    public int create(TrainType trainType) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement("INSERT INTO train_types" +
                " (name, costForStation) " +
                "VALUES (?, ?);");

        insertStatement.setString(1, trainType.getName());
        insertStatement.setFloat(2, trainType.getCostForStation());
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
    }

    @Override
    public void update(TrainType newVersion) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE train_types SET " +
                "name = ?, costForStation = ? where id = ?");
        updateStatement.setString(1, newVersion.getName());
        updateStatement.setFloat(2, newVersion.getCostForStation());
        updateStatement.setInt(3, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from train_types where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public TrainType getById(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM train_types where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<TrainType> getAll() throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM train_types;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
