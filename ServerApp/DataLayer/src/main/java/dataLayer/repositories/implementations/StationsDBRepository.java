package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.StationsRepository;
import databaseEntities.Classes.Station;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationsDBRepository implements StationsRepository {

    private final DBConnectionManager dbConnectionManager;

    public StationsDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private Station convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Station();
        obj.setId(resultSet.getInt("id"));

        obj.setName(resultSet.getString("name"));

        obj.setAddress(resultSet.getString("address"));

        obj.setKmFromMinsk(resultSet.getInt("kmFromMinsk"));
        return obj;
    }

    private List<Station> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Station>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private Station convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Station();
        return convertResultSetToObj(resultSet);
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from stations;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(Station station) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO stations" +
                        " (name, address, kmFromMinsk) " +
                        "VALUES (?, ?, ?);");

        insertStatement.setString(1, station.getName());
        insertStatement.setString(2, station.getAddress());
        insertStatement.setInt(3, station.getKmFromMinsk());
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
    }

    @Override
    public void update(Station newVersion) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE stations SET " +
                        "name = ?, address=?, kmFromMinsk=? where id = ?");
        updateStatement.setString(1, newVersion.getName());
        updateStatement.setString(2, newVersion.getAddress());
        updateStatement.setInt(3, newVersion.getId());
        updateStatement.setInt(4, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from stations where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Station getById(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM stations where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Station> getAll() throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "SELECT * FROM stations;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
