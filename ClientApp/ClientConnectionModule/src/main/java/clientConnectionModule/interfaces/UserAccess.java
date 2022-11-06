package clientConnectionModule.interfaces;

import customContainers.Pair;
import databaseEntities.Classes.*;
import responcesFromServer.ResponseFromServer;

import java.util.Date;
import java.util.List;

public interface UserAccess {

    List<Pair<RouteStation, RouteStation>> getDistanceTimeTable(int startStationId, int endStationId, Date date) throws Exception;

    void exitToAuthorization() throws Exception;

    List<Station> getAllStations() throws Exception;

    List<RouteStation> getAllRouteStations(int routeId) throws Exception;

    List<Passenger> getAllPassengers() throws Exception;

    ResponseFromServer createPassenger(Passenger passenger) throws Exception;

    ResponseFromServer updatePassenger(Passenger passengerToEdit) throws Exception;
    ResponseFromServer deletePassenger(int passengerId) throws Exception;

    float calculateCost(int routeId, int departureStationId, int arrivalStationId) throws Exception;

    Ticket createTicket(int passengerId, int routeId, int depStationId, int arrStationId) throws Exception;

    ResponseFromServer payTicket(int ticketId) throws Exception;

    List<Ticket> getAllTickets() throws Exception;

    List<Rule> getRules() throws Exception;

    ResponseFromServer editProfile(String newPassword, String newEmail) throws Exception;

    List<DocumentType> getAllDocumentTypes() throws Exception;

}
