package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.UserPassengersRepository;
import databaseEntities.Classes.DocumentType;
import databaseEntities.Classes.Passenger;
import databaseEntities.Enums.Sex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPassengersDBRepository implements UserPassengersRepository {

    private final DBConnectionManager dbConnectionManager;

    public UserPassengersDBRepository() {

        dbConnectionManager = new DBConnectionManager();
    }

    private Passenger convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Passenger();
            obj.setId(resultSet.getInt("id"));
            obj.setName(resultSet.getString("name"));
            obj.setSurname(resultSet.getString("surname"));
            obj.setPatronymic(resultSet.getString("patronymic"));
            obj.setPhoneNumber(resultSet.getString("phoneNumber"));
            obj.setDocumentType(new DocumentType(
                    resultSet.getInt("documentTypeId"),
                    resultSet.getString("documentTypeName")));
            obj.setDocumentNumber(resultSet.getString("documentNumber"));
            obj.setDateOfBirth(resultSet.getDate("dateOfBirth"));
            obj.setEmail(resultSet.getString("e-mail"));
            switch (resultSet.getInt("sex")){

                case 0:{
                    obj.setSex(Sex.Man);
                    break;
                }
                case 1:{
                    obj.setSex(Sex.Woman);
                    break;
                }
            }
        return obj;
    }

    private List<Passenger> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Passenger>();
        resultSet.beforeFirst();
        while (resultSet.next()){

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private Passenger convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Passenger();
        return convertResultSetToObj(resultSet);
    }


    @Override
    public void addPassengerToUser(int userId, int passengerId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO user_passengers" +
                        "(userId, passengerId) " +
                        "VALUES (?, ?);");

        insertStatement.setInt(1, userId);
        insertStatement.setInt(1, passengerId);
        insertStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int userId, int passengerId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from user_passengers " +
                        "where userId=? and passengerId =?");
        deleteStatement.setInt(1, userId);
        deleteStatement.setInt(2, passengerId);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Passenger getById(int userId, int passengerId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "id," +
                        "name," +
                        "surname," +
                        "patronymic," +
                        "phoneNumber," +
                        "documentTypeId," +
                        "document_types.typeName as documentTypeName," +
                        "documentNumber," +
                        "`e-mail`," +
                        "sex," +
                        "dateOfBirth " +
                        "from user_passengers " +
                        "inner join passengers on user_passengers.passengerId = passengers.id " +
                        "inner join document_types on passengers.documentTypeId = document_types.id " +
                        "where user_passengers.userId = ? and user_passengers.passengerId = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, userId);
        statement.setInt(2, passengerId);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Passenger> getAll(int userId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "passengers.id as id," +
                        "name," +
                        "surname," +
                        "patronymic," +
                        "phoneNumber," +
                        "documentTypeId," +
                        "document_types.typeName as documentTypeName," +
                        "documentNumber," +
                        "`e-mail`," +
                        "sex," +
                        "dateOfBirth " +
                        "from user_passengers " +
                        "inner join passengers on user_passengers.passengerId = passengers.id " +
                        "inner join document_types on passengers.documentTypeId = document_types.id " +
                        "where user_passengers.userId = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, userId);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
