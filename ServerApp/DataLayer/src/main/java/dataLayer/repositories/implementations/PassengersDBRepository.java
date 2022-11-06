package dataLayer.repositories.implementations;

import databaseEntities.Classes.DocumentType;
import databaseEntities.Classes.Passenger;
import databaseEntities.Enums.Sex;
import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.PassengersRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PassengersDBRepository implements PassengersRepository {

    private final DBConnectionManager dbConnectionManager;

    public PassengersDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
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
        switch (resultSet.getInt("sex")) {

            case 0: {
                obj.setSex(Sex.Man);
                break;
            }
            case 1: {
                obj.setSex(Sex.Woman);
                break;
            }
        }
        return obj;
    }

    private List<Passenger> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Passenger>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private Passenger convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Passenger();
        return convertResultSetToObj(resultSet);
    }

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from passengers;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(Passenger passenger) throws SQLException, InterruptedException {

        try{


        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO passengers" +
                        " (id, name, surname, patronymic, phoneNumber," +
                        " documentTypeId, documentNumber, `e-mail`, sex," +
                        " dateOfBirth) " +
                        "VALUES (0, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        insertStatement.setString(1, passenger.getName());
        insertStatement.setString(2, passenger.getSurname());
        insertStatement.setString(3, passenger.getPatronymic());
        insertStatement.setString(4, passenger.getPhoneNumber());
        insertStatement.setInt(5, passenger.getDocumentType().getId());
        insertStatement.setString(6, passenger.getDocumentNumber());
        insertStatement.setString(7, passenger.getEmail());
        insertStatement.setInt(8, passenger.getSex().ordinal());
        insertStatement.setDate(9, new Date(passenger.getDateOfBirth().getTime()));
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
        } catch (Exception e){
            e.getCause();
            throw e;
        }
    }

    @Override
    public void update(Passenger newVersion) throws SQLException, InterruptedException {

        try {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE passengers SET " +
                        "name=?, surname=?, patronymic=?, " +
                        "phoneNumber=?, documentTypeId=?, documentNumber =?, " +
                        "`e-mail`=?, sex=?, dateOfBirth=?" +
                        " where id = ?");
        updateStatement.setString(1, newVersion.getName());
        updateStatement.setString(2, newVersion.getSurname());
        updateStatement.setString(3, newVersion.getPatronymic());
        updateStatement.setString(4, newVersion.getPhoneNumber());
        updateStatement.setInt(5, newVersion.getDocumentType().getId());
        updateStatement.setString(6, newVersion.getDocumentNumber());
        updateStatement.setString(7, newVersion.getEmail());
        updateStatement.setInt(8, newVersion.getSex().ordinal());
        updateStatement.setDate(9, new Date(newVersion.getDateOfBirth().getTime()));
        updateStatement.setInt(10, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from passengers where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Passenger getById(int id) throws SQLException, InterruptedException {

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
                            "from passengers\n" +
                            "inner join document_types on passengers.documentTypeId = document_types.id\n" +
                            "where passengers.id = ?;",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, id);
            statement.executeQuery();
            dbConnectionManager.releaseConnection(dbConnection);
            return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Passenger> getAll() throws SQLException, InterruptedException {

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
                        "from passengers\n" +
                        "inner join document_types on passengers.documentTypeId = document_types.id;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
