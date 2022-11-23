package clientConnectionModule.interfaces;

import databaseEntities.Classes.*;
import responcesFromServer.ResponseFromServer;

import java.util.List;

public interface AdminAccess extends RegistrationAccess {

    List<Driver> getAllDrivers() throws Exception;

    ResponseFromServer createDriver(Driver obj) throws Exception;

    ResponseFromServer editDriver(Driver newVersion) throws Exception;

    ResponseFromServer deleteDriver(int id) throws Exception;

    List<TrainType> getAllTrainTypes() throws Exception;

    List<Station> getAllStationsAdmin() throws Exception;

    ResponseFromServer createStation(Station obj) throws Exception;

    ResponseFromServer editStation(Station newVersion) throws Exception;

    ResponseFromServer deleteStation(int id) throws Exception;

    List<Train> getAllTrains() throws Exception;

    ResponseFromServer createTrain(Train obj) throws Exception;

    ResponseFromServer editTrain(Train newVersion) throws Exception;

    ResponseFromServer deleteTrain(int id) throws Exception;


    ResponseFromServer createRoute(Route route) throws Exception;

    ResponseFromServer editRoute(Route newVer) throws Exception;

    ResponseFromServer deleteRoute(int id) throws Exception;

    List<Route> getAllRoutes() throws Exception;

    ResponseFromServer addRouteStationToRoute(RouteStation routeStation) throws Exception;

    ResponseFromServer deleteRouteStationFromRoute(RouteStation routeStation) throws Exception;

    ResponseFromServer editRouteStation(int routeId, int stationId, RouteStation routeStation) throws Exception;

    List<RouteStation> getAllRouteStationsFromRoute(int routeId) throws Exception;

    List<DocumentType> getAllDocumentTypesAdmin() throws Exception;

    void exit() throws Exception;

    boolean checkIfLoginExistsAdmin(String login) throws Exception;

    int getRouteTicketsAmountForLastMonth(int routeId) throws Exception;

    List<Passenger> getAllPassengersAdmin() throws Exception;

    List<Ticket> getAllTicketsAdmin() throws Exception;
}
