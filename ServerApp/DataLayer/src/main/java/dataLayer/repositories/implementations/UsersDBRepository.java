package dataLayer.repositories.implementations;

import databaseEntities.Classes.User;
import databaseEntities.Enums.UserType;
import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.UsersRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDBRepository implements UsersRepository {

    private final DBConnectionManager dbConnectionManager;

    public UsersDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private User convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new User();
        obj.setId(resultSet.getInt("id"));
        obj.setLogin(resultSet.getString("login"));
        obj.setPassword(resultSet.getString("password"));
        obj.setEmail(resultSet.getString("e-mail"));

        switch (resultSet.getInt("userType")) {
            case 0: {
                obj.setUserType(UserType.UNDEFINED);
                break;
            }
            case 1: {
                obj.setUserType(UserType.ADMIN);
                break;
            }
            case 2: {
                obj.setUserType(UserType.USER);
                break;
            }
            case 3: {
                obj.setUserType(UserType.DRIVER);
                break;
            }
        }
        return obj;
    }

    private List<User> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<User>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private User convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new User();
        return convertResultSetToObj(resultSet);
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from users;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(User user) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO users" +
                        " (login, password, `e-mail`, userType) " +
                        "VALUES (?, ?, ?, ?);");

        insertStatement.setString(1, user.getLogin());
        insertStatement.setString(2, user.getPassword());
        insertStatement.setString(3, user.getEmail());
        insertStatement.setInt(4, user.getUserType().ordinal());
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
    }

    @Override
    public void update(User newVersion) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE users SET " +
                        "login = ?, password = ?, `e-mail` = ?, userType=? where id = ?;");
        updateStatement.setString(1, newVersion.getLogin());
        updateStatement.setString(2, newVersion.getPassword());
        updateStatement.setString(3, newVersion.getEmail());
        updateStatement.setInt(4, newVersion.getUserType().ordinal());
        updateStatement.setInt(5, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws SQLException, InterruptedException {

        //TODO: check table dependency
        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from users where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public User getById(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "users.id, login, password,`e-mail`, userType " +
                        "from users " +
                        "where users.id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public User get(String login, String password) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "users.id, login, password,`e-mail`, userType " +
                        "from users " +
                        "where users.login = ? and users.password = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public User get(String login) throws Exception {
        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "users.id, login, password,`e-mail`, userType " +
                        "from users " +
                        "where users.login = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, login);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<User> getAll() throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "users.id, login, password, `e-mail`, userType " +
                        "from users;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
