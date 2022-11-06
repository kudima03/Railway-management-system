package dataLayer.repositories.implementations;

import dataLayer.dbManagers.DBConnectionManager;
import dataLayer.repositories.interfaces.RouteStationsRepository;
import databaseEntities.Classes.*;
import databaseEntities.Enums.Periodicity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteStationsDBRepository implements RouteStationsRepository {

    private final DBConnectionManager dbConnectionManager;

    public RouteStationsDBRepository() {
        this.dbConnectionManager = new DBConnectionManager();
    }

    private RouteStation convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var routeStation = new RouteStation();
        var route = new Route();
        route.setId(resultSet.getInt("routeId"));
        route.setNumber(resultSet.getInt("routeNumber"));
        switch (resultSet.getInt("routePeriodicity")) {
            case 0 -> {
                route.setPeriodicity(Periodicity.Daily);
            }
            case 1 -> {
                route.setPeriodicity(Periodicity.Weekly);
            }
            case 2 -> {
                route.setPeriodicity(Periodicity.Monthly);
            }
        }

        var routeDriver = new Driver();
        routeDriver.setId(resultSet.getInt("driverId"));
        routeDriver.setName(resultSet.getString("driverName"));
        routeDriver.setSurname(resultSet.getString("driverSurname"));
        routeDriver.setPatronymic(resultSet.getString("driverPatronymic"));
        routeDriver.setDateOfBirth(resultSet.getDate("driverDateOfBirth"));
        routeDriver.setExperience(resultSet.getInt("driverExperience"));
        routeDriver.setBinaryPhoto(resultSet.getBytes("driverPhoto"));

        route.setDriver(routeDriver);

        var train = new Train();
        train.setId(resultSet.getInt("trainId"));
        train.setNumber(resultSet.getInt("trainNumber"));
        train.setType(new TrainType(
                resultSet.getInt("trainTypeId"),
                resultSet.getString("trainTypeName"),
                resultSet.getFloat("costForStation")));

        route.setTrain(train);

        routeStation.setRoute(route);

        var station = new Station();
        station.setId(resultSet.getInt("stationId"));
        station.setName(resultSet.getString("stationName"));
        station.setAddress(resultSet.getString("stationAddress"));
        station.setKmFromMinsk(resultSet.getInt("kmFromMinsk"));

        routeStation.setStation(station);

        routeStation.setArrivalDateTime(new Date(resultSet.getTimestamp("arrivalDateTime").getTime()));
        routeStation.setDepartureDateTime(new Date(resultSet.getTimestamp("depatureDateTime").getTime()));
        routeStation.setStopTime(new Date(resultSet.getTimestamp("stopTime").getTime()));
        routeStation.setTrackNumber(resultSet.getString("trackNumber"));
        routeStation.setPlatformNumber(resultSet.getInt("platformNumber"));
        routeStation.setOrdinalNumber(resultSet.getInt("ordinalNumber"));
        return routeStation;
    }

    private List<RouteStation> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<RouteStation>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private RouteStation convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new RouteStation();
        return convertResultSetToObj(resultSet);
    }

    @Override
    public void addStationToRoute(RouteStation routeStation) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO route_stations" +
                        " (routeId, stationId, ordinalNumber, arrivalDateTime, stopTime," +
                        " depatureDateTime, track, platform) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

        insertStatement.setInt(1, routeStation.getRoute().getId());
        insertStatement.setInt(2, routeStation.getStation().getId());
        insertStatement.setInt(3, routeStation.getOrdinalNumber());
        insertStatement.setTimestamp(4, new Timestamp(routeStation.getArrivalDateTime().getTime()));
        insertStatement.setTimestamp(5, new Timestamp(routeStation.getStopTime().getTime()));
        insertStatement.setTimestamp(6, new Timestamp(routeStation.getDepartureDateTime().getTime()));
        insertStatement.setString(7, routeStation.getTrackNumber());
        insertStatement.setInt(8, routeStation.getPlatformNumber());
        insertStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void update(int routeId, int stationId, RouteStation newVersion) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var updateStatement = dbConnection.prepareStatement(
                "UPDATE route_stations SET " +
                        "routeId = ?, stationId = ?, ordinalNumber=?, arrivalDateTime=?, " +
                        "stopTime=?,depatureDateTime=?, track=?, platform=? " +
                        "where routeId = ? and stationId = ?;");

        updateStatement.setInt(1, newVersion.getRoute().getId());
        updateStatement.setInt(2, newVersion.getStation().getId());
        updateStatement.setInt(3, newVersion.getOrdinalNumber());
        updateStatement.setTimestamp(4, new Timestamp(newVersion.getArrivalDateTime().getTime()));
        updateStatement.setTimestamp(5, new Timestamp(newVersion.getStopTime().getTime()));
        updateStatement.setTimestamp(6, new Timestamp(newVersion.getDepartureDateTime().getTime()));
        updateStatement.setString(7, newVersion.getTrackNumber());
        updateStatement.setInt(8, newVersion.getPlatformNumber());
        updateStatement.setInt(9, routeId);
        updateStatement.setInt(10, stationId);
        updateStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public void delete(int routeId, int stationId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from route_stations where routeId=? and stationId = ?;");
        deleteStatement.setInt(1, routeId);
        deleteStatement.setInt(2, stationId);
        deleteStatement.executeUpdate();
        dbConnectionManager.releaseConnection(dbConnection);
    }

    @Override
    public RouteStation get(int routeId, int stationId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "routeId," +
                        "routes.number as routeNumber," +
                        "routes.periodicity as routePeriodicity," +
                        "driverId," +
                        "drivers.name as driverName," +
                        "drivers.surname as driverSurname," +
                        "drivers.patronymic as driverPatronymic," +
                        "drivers.dateOfBirth as driverDateOfBirth," +
                        "drivers.experience as driverExperience," +
                        "drivers.photo as driverPhoto," +
                        "trainId," +
                        "trains.number as trainNumber," +
                        "trains.typeId as trainTypeId," +
                        "train_types.name as trainTypeName," +
                        "stationId," +
                        "stations.name as stationName," +
                        "stations.address as stationAddress," +
                        "stations.kmFromMinsk," +
                        "route_stations.arrivalDateTime," +
                        "route_stations.depatureDateTime," +
                        "route_stations.stopTime," +
                        "route_stations.track as trackNumber," +
                        "route_stations.platform as platformNumber," +
                        "route_stations.ordinalNumber, " +
                        "costForStation " +
                        "from route_stations " +
                        "inner join routes on route_stations.routeId = routes.id " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join trains on routes.trainId = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join stations on route_stations.stationId = stations.id " +
                        "where routeId=? and stationId=?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, routeId);
        statement.setInt(2, stationId);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToSingleObj(statement.getResultSet());
    }


    @Override
    public List<RouteStation> get(int stationId, Date date) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "routeId," +
                        "routes.number as routeNumber," +
                        "routes.periodicity as routePeriodicity," +
                        "driverId," +
                        "drivers.name as driverName," +
                        "drivers.surname as driverSurname," +
                        "drivers.patronymic as driverPatronymic," +
                        "drivers.dateOfBirth as driverDateOfBirth," +
                        "drivers.experience as driverExperience," +
                        "drivers.photo as driverPhoto," +
                        "trainId," +
                        "trains.number as trainNumber," +
                        "trains.typeId as trainTypeId," +
                        "train_types.name as trainTypeName," +
                        "stationId," +
                        "stations.name as stationName," +
                        "stations.address as stationAddress," +
                        "stations.kmFromMinsk," +
                        "route_stations.arrivalDateTime," +
                        "route_stations.depatureDateTime," +
                        "route_stations.stopTime," +
                        "route_stations.track as trackNumber," +
                        "route_stations.platform as platformNumber," +
                        "route_stations.ordinalNumber," +
                        "costForStation " +
                        "from route_stations " +
                        "inner join routes on route_stations.routeId = routes.id " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join trains on routes.trainId = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join stations on route_stations.stationId = stations.id " +
                        "where stationId=? and (routes.periodicity = 0 or " +
                        "(periodicity = 1 and DATEDIFF(depatureDateTime, ?) % 7 = 0) or " +
                        "(periodicity = 2 and DAY(depatureDateTime) = DAY(?))) and " +
                        "depatureDateTime <= ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, stationId);
        var sqlDate = new java.sql.Date(date.getTime());
        statement.setDate(2, sqlDate);
        statement.setDate(3, sqlDate);
        statement.setDate(4, sqlDate);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }

    @Override
    public List<RouteStation> getAll(int routeId) throws SQLException, InterruptedException {

        var dbConnection = dbConnectionManager.getConnection();
        var statement = dbConnection.prepareStatement(
                "select " +
                        "routeId," +
                        "routes.number as routeNumber," +
                        "routes.periodicity as routePeriodicity," +
                        "driverId," +
                        "drivers.name as driverName," +
                        "drivers.surname as driverSurname," +
                        "drivers.patronymic as driverPatronymic," +
                        "drivers.dateOfBirth as driverDateOfBirth," +
                        "drivers.experience as driverExperience," +
                        "drivers.photo as driverPhoto," +
                        "trainId," +
                        "trains.number as trainNumber," +
                        "trains.typeId as trainTypeId," +
                        "train_types.name as trainTypeName," +
                        "stationId," +
                        "stations.name as stationName," +
                        "stations.address as stationAddress," +
                        "stations.kmFromMinsk," +
                        "route_stations.arrivalDateTime," +
                        "route_stations.depatureDateTime," +
                        "route_stations.stopTime," +
                        "route_stations.track as trackNumber," +
                        "route_stations.platform as platformNumber," +
                        "route_stations.ordinalNumber," +
                        "costForStation " +
                        "from route_stations " +
                        "inner join routes on route_stations.routeId = routes.id " +
                        "inner join drivers on routes.driverId = drivers.id " +
                        "inner join trains on routes.trainId = trains.id " +
                        "inner join train_types on trains.typeId = train_types.id " +
                        "inner join stations on route_stations.stationId = stations.id " +
                        "where routeId=?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, routeId);
        statement.executeQuery();
        dbConnectionManager.releaseConnection(dbConnection);
        return convertResultSetToList(statement.getResultSet());
    }
}