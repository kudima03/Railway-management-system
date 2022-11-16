package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.TicketsRepository;
import databaseEntities.Classes.*;
import databaseEntities.Enums.Periodicity;
import databaseEntities.Enums.PurchaseStatus;
import databaseEntities.Enums.Sex;
import databaseEntities.Enums.UserType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketsDBRepository implements TicketsRepository {

    private final DBConnectionManager dbConnectionManager;

    public TicketsDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private Ticket convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Ticket();
        obj.setId(resultSet.getInt("id"));

        var passenger = new Passenger();
        passenger.setId(resultSet.getInt("passengerId"));
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
        departureStation.setId(resultSet.getInt("departureStationId"));
        departureStation.setName(resultSet.getString("departureStationName"));
        departureStation.setAddress(resultSet.getString("departureStationAddress"));
        departureStation.setKmFromMinsk(resultSet.getInt("departureStationKmFromMinsk"));
        obj.setDepartureStation(departureStation);

        var arrivalStation = new Station();
        arrivalStation.setId(resultSet.getInt("arrivalStationId"));
        arrivalStation.setName(resultSet.getString("arrivalStationName"));
        arrivalStation.setAddress(resultSet.getString("arrivalStationAddress"));
        arrivalStation.setKmFromMinsk(resultSet.getInt("arrivalStationKmFromMinsk"));
        obj.setArrivalStation(arrivalStation);

        obj.setDepartureStation(departureStation);
        obj.setArrivalStation(arrivalStation);
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

    private int getMaxId(Connection dbConnection) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from tickets;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int create(Ticket ticket) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO tickets" +
                        " (passengerId, routeId, depatureStationId," +
                        " arrivalStationId, cost, clearanceDateTime," +
                        " status, routeLength) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

        insertStatement.setInt(1, ticket.getPassenger().getId());
        insertStatement.setInt(2, ticket.getRoute().getId());
        insertStatement.setInt(3, ticket.getDepartureStation().getId());
        insertStatement.setInt(4, ticket.getArrivalStation().getId());
        insertStatement.setFloat(5, ticket.getCost());
        insertStatement.setDate(6, new Date(ticket.getClearanceTime().getTime()));
        insertStatement.setInt(7, ticket.getPurchaseStatus().ordinal());
        insertStatement.setInt(8, ticket.getRouteLength());
        insertStatement.executeUpdate();
        int recentlyAddedId = getMaxId(dbConnection);
        dbConnectionManager.releaseConnection(dbConnection);
        return recentlyAddedId;
    }

    @Override
    public void update(Ticket newVersion) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE tickets SET " +
                        "passengerId=?, routeId=?, depatureStationId=?," +
                        "arrivalStationId=?, cost=?, clearanceDateTime=?," +
                        "status=?, routeLength=? where id=?");
        updateStatement.setInt(1, newVersion.getPassenger().getId());
        updateStatement.setInt(2, newVersion.getRoute().getId());
        updateStatement.setInt(3, newVersion.getDepartureStation().getId());
        updateStatement.setInt(4, newVersion.getArrivalStation().getId());
        updateStatement.setFloat(5, newVersion.getCost());
        updateStatement.setDate(6, new Date(newVersion.getClearanceTime().getTime()));
        updateStatement.setInt(7, newVersion.getPurchaseStatus().ordinal());
        updateStatement.setInt(8, newVersion.getRouteLength());
        updateStatement.setInt(9, newVersion.getId());
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from tickets where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public Ticket getById(int id) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "tickets.id as id," +
                        "passengers.id as passengerId," +
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
                        "depatureStationId as departureStationId," +
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
                        "routeLength," +
                        "costForStation," +
                        "login as driverLogin, " +
                        "password as driverPassword, " +
                        "users.`e-mail` as driverEmail " +
                        "from tickets " +
                        "inner join passengers on tickets.passengerId = passengers.id " +
                        "inner join document_types on passengers.documentTypeId = document_types.id " +
                        "inner join routes on tickets.routeId = routes.id " +
                        "inner join stations arrivalStation on tickets.arrivalStationId = arrivalStation.id " +
                        "inner join stations departureStation on tickets.depatureStationId = departureStation.id " +
                        "inner join trains on routes.trainId = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join users on drivers.userId = users.id " +
                        "where tickets.id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    @Override
    public List<Ticket> getAll() throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "tickets.id as id," +
                        "passengers.id as passengerId," +
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
                        "depatureStationId as departureStationId," +
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
                        "routeLength," +
                        "costForStation," +
                        "login as driverLogin, " +
                        "password as driverPassword, " +
                        "users.`e-mail` as driverEmail " +
                        "from tickets " +
                        "inner join passengers on tickets.passengerId = passengers.id " +
                        "inner join document_types on passengers.documentTypeId = document_types.id " +
                        "inner join routes on tickets.routeId = routes.id " +
                        "inner join stations arrivalStation on tickets.arrivalStationId = arrivalStation.id " +
                        "inner join stations departureStation on tickets.depatureStationId = departureStation.id " +
                        "inner join trains on routes.trainId = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join users on drivers.userId = users.id;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}