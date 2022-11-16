package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.RoutesRepository;
import databaseEntities.Classes.*;
import databaseEntities.Enums.Periodicity;
import databaseEntities.Enums.UserType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoutesDBRepository implements RoutesRepository {

    private final DBConnectionManager dbConnectionManager;

    public RoutesDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private Route convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Route();
            obj.setId(resultSet.getInt("id"));

            obj.setTrain(new Train(resultSet.getInt("trainId"),
                    new TrainType(resultSet.getInt("trainId"),
                            resultSet.getString("trainTypeName"),
                            resultSet.getFloat("costForStation")),
                    resultSet.getInt("trainNumber")));

            obj.setDriver(new Driver(resultSet.getString("driverName"),
                    resultSet.getString("driverSurname"),
                    resultSet.getString("driverPatronymic"),
                    resultSet.getDate("driverDateOfBirth"),
                    resultSet.getInt("driverExperience"),
                    new User(resultSet.getInt("userId"),
                            resultSet.getString("driverLogin"),
                            resultSet.getString("driverPassword"),
                            resultSet.getString("driverEmail"),
                            UserType.DRIVER)));

            obj.setNumber(resultSet.getInt("number"));

            switch (resultSet.getInt("periodicity")) {
                case 0: {
                    obj.setPeriodicity(Periodicity.Daily);
                    break;
                }
                case 1: {
                    obj.setPeriodicity(Periodicity.Weekly);
                    break;
                }
                case 2: {
                    obj.setPeriodicity(Periodicity.Monthly);
                    break;
                }
            }
        return obj;
    }

    private List<Route> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Route>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private Route convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Route();
        return convertResultSetToObj(resultSet);
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from routes;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(Route route) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO routes" +
                " (id, trainId, driverId, number, periodicity) " +
                "VALUES (0, ?, ?, ?, ?);");

        insertStatement.setInt(1, route.getTrain().getId());
        insertStatement.setInt(2, route.getDriver().getId());
        insertStatement.setInt(3, route.getNumber());
        insertStatement.setInt(4, route.getPeriodicity().ordinal());
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
    }

    @Override
    public void update(Route newVersion) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE routes SET " +
                "trainId = ?, driverId = ?, number = ?, periodicity = ? where id = ?");
        updateStatement.setInt(1, newVersion.getTrain().getId());
        updateStatement.setInt(2, newVersion.getDriver().getId());
        updateStatement.setInt(2, newVersion.getNumber());
        updateStatement.setInt(3, newVersion.getPeriodicity().ordinal());
        updateStatement.setInt(4, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from routes where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Route getById(int id) throws Exception {
        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "routes.id," +
                        "routes.trainId," +
                        "train_types.name as 'trainTypeName'," +
                        "trains.number as 'trainNumber'," +
                        "drivers.name as 'driverName'," +
                        "drivers.surname as 'driverSurname'," +
                        "drivers.patronymic as 'driverPatronymic'," +
                        "drivers.dateOfBirth as 'driverDateOfBirth'," +
                        "drivers.experience as 'driverExperience'," +
                        "routes.number," +
                        "routes.periodicity," +
                        "costForStation," +
                        "userId, " +
                        "users.login as driverLogin," +
                        "users.password as driverPassword, " +
                        "users.`e-mail` as driverEmail " +
                        "from routes " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join trains on routes.trainId  = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join users on drivers.userId = users.id " +
                        "where routes.id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Route> getAll() throws Exception {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "routes.id," +
                        "routes.trainId," +
                        "train_types.name as 'trainTypeName'," +
                        "trains.number as 'trainNumber'," +
                        "drivers.name as 'driverName'," +
                        "drivers.surname as 'driverSurname'," +
                        "drivers.patronymic as 'driverPatronymic'," +
                        "drivers.dateOfBirth as 'driverDateOfBirth'," +
                        "drivers.experience as 'driverExperience'," +
                        "routes.number," +
                        "routes.periodicity," +
                        "costForStation," +
                        "userId, " +
                        "users.login as driverLogin," +
                        "users.password as driverPassword, " +
                        "users.`e-mail` as driverEmail " +
                        "from routes " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join trains on routes.trainId  = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join users on drivers.userId = users.id;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
