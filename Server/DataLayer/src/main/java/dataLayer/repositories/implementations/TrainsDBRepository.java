package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.TrainsRepository;
import databaseEntities.Classes.Train;
import databaseEntities.Classes.TrainType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainsDBRepository implements TrainsRepository {

    private final DBConnectionManager dbConnectionManager;

    public TrainsDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private Train convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Train();

        obj.setId(resultSet.getInt("id"));
        obj.setNumber(resultSet.getInt("number"));
        obj.setType(new TrainType(resultSet.getInt("typeId"),
                resultSet.getString("typeName"),
                resultSet.getFloat("costForStation")));
        return obj;
    }

    private List<Train> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Train>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private Train convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Train();
        return convertResultSetToObj(resultSet);
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from trains;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(Train train) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement("INSERT INTO trains" +
                " (typeId, number) " +
                "VALUES (?, ?);");

        insertStatement.setInt(1, train.getType().getId());
        insertStatement.setInt(2, train.getNumber());
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
    }

    @Override
    public void update(Train newVersion) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement("UPDATE trains SET " +
                "typeId = ?, number = ? where id = ?;");
        updateStatement.setInt(1, newVersion.getType().getId());
        updateStatement.setInt(2, newVersion.getNumber());
        updateStatement.setInt(3, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws Exception {

        try {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from trains where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Train getById(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select trains.id, typeId, number, tt.name as typeName, costForStation" +
                        " from trains inner join train_types tt on trains.typeId = tt.id" +
                        " where trains.id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Train> getAll() throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select trains.id, typeId, number, tt.name as typeName, costForStation" +
                        " from trains " +
                        "inner join train_types tt on trains.typeId = tt.id;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
