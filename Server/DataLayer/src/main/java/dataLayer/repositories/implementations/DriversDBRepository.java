package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.DriversRepository;
import databaseEntities.Classes.Driver;
import databaseEntities.Classes.User;
import databaseEntities.Enums.UserType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriversDBRepository implements DriversRepository {

    private final DBConnectionManager dbConnectionManager;

    public DriversDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private Driver convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Driver();
        if (!resultSet.isAfterLast()) {

            obj.setId(resultSet.getInt("drivers.id"));

            obj.setName(resultSet.getString("name"));

            obj.setSurname(resultSet.getString("surname"));

            obj.setPatronymic(resultSet.getString("patronymic"));

            obj.setDateOfBirth(new java.util.Date(resultSet.getTimestamp("dateOfBirth").getTime()));

            obj.setExperience(resultSet.getInt("experience"));

            var user = new User();
            user.setId(resultSet.getInt("userId"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("e-mail"));

            switch (resultSet.getInt("userType")) {
                case 0: {
                    user.setUserType(UserType.UNDEFINED);
                    break;
                }
                case 1: {
                    user.setUserType(UserType.ADMIN);
                    break;
                }
                case 2: {
                    user.setUserType(UserType.USER);
                    break;
                }
                case 3: {
                    user.setUserType(UserType.DRIVER);
                    break;
                }
            }
            obj.setUser(user);
            return obj;

        }
        return obj;
    }

    private List<Driver> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Driver>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private Driver convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        return convertResultSetToObj(resultSet);
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from drivers;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private int getUsersMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from users;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(Driver driver) throws SQLException, InterruptedException {

            var dbConnection = dbConnectionManager.getConnection();
            var userInsertStatement = dbConnection.prepareStatement(
                    "INSERT INTO users " +
                            "(id, login, password, `e-mail`, userType) " +
                            "VALUES (0, ?, ?, ?, ?)");
            userInsertStatement.setString(1, driver.getUser().getLogin());
            userInsertStatement.setString(2, driver.getUser().getPassword());
            userInsertStatement.setString(3, driver.getUser().getEmail());
            userInsertStatement.setInt(4, driver.getUser().getUserType().ordinal());
            userInsertStatement.executeUpdate();
            int userId = getUsersMaxId(dbConnection);
            driver.getUser().setId(userId);

            var insertStatement = dbConnection.prepareStatement(
                    "INSERT INTO drivers" +
                            " (id, name, surname, patronymic, dateOfBirth, experience, userId) " +
                            "VALUES (0, ?, ?, ?, ?, ?, ?);");

            insertStatement.setString(1, driver.getName());
            insertStatement.setString(2, driver.getSurname());
            insertStatement.setString(3, driver.getPatronymic());
            insertStatement.setDate(4, new Date(driver.getDateOfBirth().getTime()));
            insertStatement.setInt(5, driver.getExperience());
            insertStatement.setInt(6, userId);
            insertStatement.executeUpdate();
            int recentlyAddedId = getMaxId(dbConnection);
            dbConnectionManager.releaseConnection(dbConnection);
            return recentlyAddedId;
    }

    @Override
    public void update(Driver newVersion) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE drivers SET " +
                        "name=?, surname=?, patronymic =?, dateOfBirth = ?, experience = ?" +
                        " where id = ?");
        updateStatement.setString(1, newVersion.getName());
        updateStatement.setString(2, newVersion.getSurname());
        updateStatement.setString(3, newVersion.getPatronymic());
        updateStatement.setDate(4, new Date(newVersion.getDateOfBirth().getTime()));
        updateStatement.setInt(5, newVersion.getExperience());
        updateStatement.setInt(6, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from drivers where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Driver getById(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select * from drivers inner join users on drivers.userId = users.id" +
                        " where drivers.id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Driver> getAll() throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select * from drivers inner join users on drivers.userId = users.id",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
