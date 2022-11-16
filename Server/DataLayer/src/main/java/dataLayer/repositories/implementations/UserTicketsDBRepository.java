package dataLayer.repositories.implementations;

import databaseEntities.Classes.*;
import databaseEntities.Enums.Periodicity;
import databaseEntities.Enums.PurchaseStatus;
import databaseEntities.Enums.Sex;
import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.UserTicketsRepository;
import databaseEntities.Enums.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTicketsDBRepository implements UserTicketsRepository {

    private final DBConnectionManager dbConnectionManager;

    public UserTicketsDBRepository() {

        dbConnectionManager = new DBConnectionManager();
    }

    private Ticket convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Ticket();
        obj.setId(resultSet.getInt("id"));

        var passenger = new Passenger();
        passenger.setName(resultSet.getString("passengerName"));
        passenger.setSurname(resultSet.getString("passengerSurname"));
        passenger.setPatronymic(resultSet.getString("passengerPatronymic"));
        passenger.setPhoneNumber(resultSet.getString("passengerPhoneNumber"));
        passenger.setDocumentType(new DocumentType(
                resultSet.getInt("documentTypeId"),
                resultSet.getString("passengerDocumentTypeName")));
        passenger.setDocumentNumber(resultSet.getString("passengerDocumentNumber"));
        passenger.setDateOfBirth(resultSet.getDate("passengerDateOfBirth"));
        passenger.setEmail(resultSet.getString("passengerE-mail"));
        switch (resultSet.getInt("passengerSex")) {

            case 0: {
                passenger.setSex(Sex.Man);
                break;
            }
            case 1: {
                passenger.setSex(Sex.Woman);
                break;
            }
        }
        obj.setPassenger(passenger);

        var route = new Route();
        route.setId(resultSet.getInt("routeId"));
        route.setTrain(new Train(resultSet.getInt("trainId"),
                new TrainType(resultSet.getInt("trainTypeId"),
                        resultSet.getString("trainTypeName"),
                        resultSet.getFloat("costForStation")),
                resultSet.getInt("trainNumber")));
        route.setDriver(new Driver(resultSet.getString("driverName"),
                resultSet.getString("driverSurname"),
                resultSet.getString("driverPatronymic"),
                resultSet.getDate("driverDateOfBirth"),
                resultSet.getInt("driverExperience"),
                new User(resultSet.getInt("userId"),
                resultSet.getString("driverLogin"),
                resultSet.getString("driverPassword"),
                resultSet.getString("driverEmail"),
                UserType.DRIVER)));
        route.setNumber(resultSet.getInt("number"));
        switch (resultSet.getInt("periodicity")) {
            case 0: {
                route.setPeriodicity(Periodicity.Daily);
                break;
            }
            case 1: {
                route.setPeriodicity(Periodicity.Weekly);
                break;
            }
            case 2: {
                route.setPeriodicity(Periodicity.Monthly);
                break;
            }
        }

        obj.setRoute(route);

        var departureStation = new Station();
        departureStation.setId(resultSet.getInt("depatureStationId"));
        departureStation.setName(resultSet.getString("departureStationName"));
        departureStation.setAddress(resultSet.getString("departureStationAddress"));
        departureStation.setKmFromMinsk(resultSet.getInt("departureStationKmFromMinsk"));

        var arrivalStation = new Station();
        arrivalStation.setId(resultSet.getInt("arrivalStationId"));
        arrivalStation.setName(resultSet.getString("arrivalStationName"));
        arrivalStation.setAddress(resultSet.getString("arrivalStationAddress"));
        arrivalStation.setKmFromMinsk(resultSet.getInt("arrivalStationKmFromMinsk"));

        obj.setArrivalStation(arrivalStation);
        obj.setDepartureStation(departureStation);
        obj.setCost(resultSet.getFloat("cost"));
        obj.setClearanceTime(resultSet.getDate("clearanceDateTime"));
        switch (resultSet.getInt("status")) {
            case 0: {
                obj.setPurchaseStatus(PurchaseStatus.WaitingForPayment);
                break;
            }
            case 1: {
                obj.setPurchaseStatus(PurchaseStatus.Paid);
                break;
            }
        }
        obj.setRouteLength(resultSet.getInt("routeLength"));
        return obj;
    }

    private List<Ticket> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Ticket>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private Ticket convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Ticket();
        return convertResultSetToObj(resultSet);
    }

    @Override
    public void addTicketToUser(int userId, int ticketId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO users_tickets" +
                        "(userId, ticketId) " +
                        "VALUES (?, ?);");

        insertStatement.setInt(1, userId);
        insertStatement.setInt(2, ticketId);
        insertStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int userId, int ticketId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from users_tickets " +
                        "where userId=? and ticketId =?");
        deleteStatement.setInt(1, userId);
        deleteStatement.setInt(2, ticketId);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Ticket getById(int userId, int ticketId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "tickets.id as id," +
                        "passengers.name as passengerName," +
                        "passengers.surname as passengerSurname," +
                        "passengers.patronymic as passengerPatronymic," +
                        "passengers.phoneNumber as passengerPhoneNumber," +
                        "passengers.documentTypeId," +
                        "document_types.typeName as passengerDocumentTypeName," +
                        "passengers.documentNumber as passengerDocumentNumber," +
                        "passengers.dateOfBirth as passengerDateOfBirth," +
                        "passengers.`e-mail` as `passengerE-mail`," +
                        "passengers.sex as passengerSex," +
                        "routeId," +
                        "trainId," +
                        "trains.typeId as trainTypeId," +
                        "train_types.name as trainTypeName," +
                        "trains.number as trainNumber," +
                        "drivers.name as driverName," +
                        "drivers.surname as driverSurname," +
                        "drivers.patronymic as driverPatronymic," +
                        "drivers.dateOfBirth as driverDateOfBirth," +
                        "drivers.experience as driverExperience," +
                        "drivers.userId as userId," +
                        "routes.number as number," +
                        "routes.periodicity as periodicity," +
                        "depatureStationId," +
                        "departureStation.name as departureStationName," +
                        "departureStation.address as departureStationAddress," +
                        "departureStation.kmFromMinsk as departureStationKmFromMinsk," +
                        "arrivalStationId," +
                        "arrivalStation.name as arrivalStationName," +
                        "arrivalStation.address as arrivalStationAddress," +
                        "arrivalStation.kmFromMinsk as arrivalStationKmFromMinsk," +
                        "cost," +
                        "costForStation," +
                        "clearanceDateTime," +
                        "status," +
                        "routeLength, " +
                        "users.login as driverLogin, " +
                        "users.password as driverPassword, " +
                        "users.`e-mail` as driverEmail " +
                        "from users_tickets " +
                        "inner join tickets on users_tickets.ticketId = tickets.id " +
                        "inner join passengers on tickets.passengerId = passengers.id " +
                        "inner join document_types on passengers.documentTypeId = document_types.id " +
                        "inner join routes on tickets.routeId = routes.id " +
                        "inner join stations arrivalStation on tickets.arrivalStationId = arrivalStation.id " +
                        "inner join stations departureStation on tickets.depatureStationId = departureStation.id " +
                        "inner join trains on routes.trainId = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join users on drivers.userId = users.id " +
                        " where userId = ? and ticketId = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, userId);
        statement.setInt(2, ticketId);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Ticket> getAll(int userId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "tickets.id as id," +
                        "passengers.name as passengerName," +
                        "passengers.surname as passengerSurname," +
                        "passengers.patronymic as passengerPatronymic," +
                        "passengers.phoneNumber as passengerPhoneNumber," +
                        "passengers.documentTypeId," +
                        "document_types.typeName as passengerDocumentTypeName," +
                        "passengers.documentNumber as passengerDocumentNumber," +
                        "passengers.dateOfBirth as passengerDateOfBirth," +
                        "passengers.`e-mail` as `passengerE-mail`," +
                        "passengers.sex as passengerSex," +
                        "routeId," +
                        "trainId," +
                        "trains.typeId as trainTypeId," +
                        "train_types.name as trainTypeName," +
                        "trains.number as trainNumber," +
                        "drivers.name as driverName," +
                        "drivers.surname as driverSurname," +
                        "drivers.patronymic as driverPatronymic," +
                        "drivers.dateOfBirth as driverDateOfBirth," +
                        "drivers.experience as driverExperience," +
                        "drivers.userId as userId," +
                        "routes.number as number," +
                        "routes.periodicity as periodicity," +
                        "depatureStationId," +
                        "departureStation.name as departureStationName," +
                        "departureStation.address as departureStationAddress," +
                        "departureStation.kmFromMinsk as departureStationKmFromMinsk," +
                        "arrivalStationId," +
                        "arrivalStation.name as arrivalStationName," +
                        "arrivalStation.address as arrivalStationAddress," +
                        "arrivalStation.kmFromMinsk as arrivalStationKmFromMinsk," +
                        "cost," +
                        "clearanceDateTime," +
                        "status," +
                        "costForStation," +
                        "routeLength, " +
                        "users.login as driverLogin, " +
                        "users.password as driverPassword, " +
                        "users.`e-mail` as driverEmail " +
                        "from users_tickets " +
                        "inner join tickets on users_tickets.ticketId = tickets.id " +
                        "inner join passengers on tickets.passengerId = passengers.id " +
                        "inner join document_types on passengers.documentTypeId = document_types.id " +
                        "inner join routes on tickets.routeId = routes.id " +
                        "inner join stations arrivalStation on tickets.arrivalStationId = arrivalStation.id " +
                        "inner join stations departureStation on tickets.depatureStationId = departureStation.id " +
                        "inner join trains on routes.trainId = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join users on drivers.userId = users.id " +
                        "where users_tickets.userId = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, userId);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}
