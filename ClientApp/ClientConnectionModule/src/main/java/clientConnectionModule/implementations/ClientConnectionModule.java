package clientConnectionModule.implementations;

import clientConnectionModule.interfaces.*;
import commandsToServer.AdminCommand;
import commandsToServer.CommonCommand;
import commandsToServer.DriverCommand;
import commandsToServer.UserCommand;
import customContainers.Pair;
import databaseEntities.Classes.*;
import databaseEntities.Enums.UserType;
import responcesFromServer.ResponseFromServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class ClientConnectionModule implements AdminAccess, UserAccess, DriverAccess, RegistrationAccess, SingUpAccess {

    private Socket connectionSocket;
    private String serverIp;
    private int serverPort;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ClientConnectionModule(String serverIp, int serverPort) {

        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public boolean connectToServer() throws IOException {

        connectionSocket = new Socket(serverIp, serverPort);
        //Вот здесь мы ставим тайм-аут ожидания 3 сек
        //Если серв не отвечает более чем 3 сек мы отключаемся
        //connectionSocket.setSoTimeout(3000);
        if (!connectionSocket.isConnected()) return false;
        objectOutputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(connectionSocket.getInputStream());
        return true;
    }

    private void sendObject(Serializable object) throws IOException {

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    private <T> T receiveObject() throws Exception {

        return (T) objectInputStream.readObject();
    }

    //Inherited from CommonAccess /////////////////////////////////////////////////////////////////////////
    @Override
    public UserType singUp(String login, String password) throws Exception {

        sendObject(CommonCommand.AUTHORIZE);
        sendObject(login);
        sendObject(password);
        return receiveObject();
    }

    @Override
    public ResponseFromServer registration(String login, String password, String email, UserType userType) throws Exception {

        sendObject(CommonCommand.REGISTER);
        sendObject(userType);
        sendObject(login);
        sendObject(password);
        sendObject(email);
        return receiveObject();
    }

    @Override
    public boolean checkIfLoginExists(String login) throws Exception {

        sendObject(CommonCommand.CHECK_IF_LOGIN_EXISTS);
        sendObject(login);
        ResponseFromServer response = receiveObject();
        return response == ResponseFromServer.SUCCESSFULLY;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //Inherited from UserAccess ///////////////////////////////////////////////////////////////////
    @Override
    public List<Pair<RouteStation, RouteStation>> getDistanceTimeTable(int startStationId, int endStationId, Date date) throws Exception {

        sendObject(UserCommand.GET_TIME_TABLE);
        sendObject(startStationId);
        sendObject(endStationId);
        sendObject(date);
        return receiveObject();
    }

    @Override
    public void exitToAuthorization() throws Exception {
        sendObject(UserCommand.EXIT_TO_AUTHORIZATION);
    }

    @Override
    public List<Station> getAllStations() throws Exception {

        sendObject(UserCommand.GET_ALL_STATIONS);
        return receiveObject();
    }

    @Override
    public List<RouteStation> getAllRouteStations(int routeId) throws Exception {
        sendObject(UserCommand.GET_ROUTE_STATIONS);
        sendObject(routeId);
        return receiveObject();
    }

    @Override
    public List<Passenger> getAllPassengers() throws Exception {
        sendObject(UserCommand.GET_PASSENGERS);
        return receiveObject();
    }

    @Override
    public ResponseFromServer createPassenger(Passenger passenger) throws Exception {
        sendObject(UserCommand.CREATE_PASSENGER);
        sendObject(passenger);
        return receiveObject();
    }

    @Override
    public ResponseFromServer updatePassenger(Passenger passengerToEdit) throws Exception {
        sendObject(UserCommand.UPDATE_PASSENGER);
        sendObject(passengerToEdit);
        return receiveObject();
    }

    @Override
    public ResponseFromServer deletePassenger(int passengerId) throws Exception {
        sendObject(UserCommand.DELETE_PASSENGER);
        sendObject(passengerId);
        return receiveObject();
    }

    @Override
    public float calculateCost(int routeId, int departureStationId, int arrivalStationId) throws Exception {

        sendObject(UserCommand.CALCULATE_COST);
        sendObject(routeId);
        sendObject(departureStationId);
        sendObject(arrivalStationId);
        return receiveObject();
    }

    @Override
    public Ticket createTicket(int passengerId, int routeId, int depStationId, int arrStationId) throws Exception {
        sendObject(UserCommand.CREATE_TICKET);
        sendObject(passengerId);
        sendObject(routeId);
        sendObject(depStationId);
        sendObject(arrStationId);
        return receiveObject();
    }

    @Override
    public ResponseFromServer payTicket(int ticketId) throws Exception {
        sendObject(UserCommand.PAY_TICKET);
        sendObject(ticketId);
        return receiveObject();
    }

    @Override
    public List<Ticket> getAllTickets() throws Exception {
        sendObject(UserCommand.GET_ALL_TICKETS);
        return receiveObject();
    }

    @Override
    public List<Rule> getRules() throws Exception {
        sendObject(UserCommand.GET_RULES);
        return receiveObject();
    }

    @Override
    public ResponseFromServer editProfile(String newPassword, String newEmail) throws Exception {
        sendObject(UserCommand.EDIT_PROFILE);
        sendObject(newPassword);
        sendObject(newEmail);
        return receiveObject();
    }

    @Override
    public List<DocumentType> getAllDocumentTypes() throws Exception {
        sendObject(UserCommand.GET_DOCUMENT_TYPES);
        return receiveObject();
    }
    /////////////////////////////////////////////////////////////////////////

    //Inherited from AdminAccess ////////////////////////////////////////////

    @Override
    public List<Driver> getAllDrivers() throws Exception {
        sendObject(AdminCommand.GET_ALL_DRIVERS);
        return receiveObject();
    }

    @Override
    public ResponseFromServer createDriver(Driver obj) throws Exception {
        sendObject(AdminCommand.CREATE_DRIVER);
        sendObject(obj);
        return receiveObject();
    }

    @Override
    public ResponseFromServer editDriver(Driver newVersion) throws Exception {
        sendObject(AdminCommand.EDIT_DRIVER);
        sendObject(newVersion);
        return receiveObject();
    }

    @Override
    public ResponseFromServer deleteDriver(int id) throws Exception {
       sendObject(AdminCommand.DELETE_DRIVER);
       sendObject(id);
       return receiveObject();
    }

    @Override
    public List<TrainType> getAllTrainTypes() throws Exception {
        sendObject(AdminCommand.GET_ALL_TRAIN_TYPES);
        return receiveObject();
    }

    @Override
    public ResponseFromServer createStation(Station obj) throws Exception {
        sendObject(AdminCommand.CREATE_STATION);
        sendObject(obj);
        return receiveObject();
    }

    @Override
    public ResponseFromServer editStation(Station newVersion) throws Exception {
        sendObject(AdminCommand.EDIT_STATION);
        sendObject(newVersion);
        return receiveObject();
    }

    @Override
    public ResponseFromServer deleteStation(int id) throws Exception {
        sendObject(AdminCommand.DELETE_STATION);
        sendObject(id);
        return receiveObject();
    }

    @Override
    public List<Station> getAllStationsAdmin() throws Exception {
        sendObject(AdminCommand.GET_ALL_STATIONS);
        return receiveObject();
    }

    @Override
    public List<Train> getAllTrains() throws Exception {
        sendObject(AdminCommand.GET_ALL_TRAINS);
        return receiveObject();
    }

    @Override
    public ResponseFromServer createTrain(Train obj) throws Exception {
        sendObject(AdminCommand.CREATE_TRAIN);
        sendObject(obj);
        return receiveObject();
    }

    @Override
    public ResponseFromServer editTrain(Train newVersion) throws Exception {
        sendObject(AdminCommand.EDIT_TRAIN);
        sendObject(newVersion);
        return receiveObject();
    }

    @Override
    public ResponseFromServer deleteTrain(int id) throws Exception {
        sendObject(AdminCommand.DELETE_TRAIN);
        sendObject(id);
        return receiveObject();
    }

    @Override
    public ResponseFromServer createRoute(Route route) throws Exception {
        sendObject(AdminCommand.CREATE_ROUTE);
        sendObject(route);
        return receiveObject();
    }

    @Override
    public ResponseFromServer editRoute(Route newVer) throws Exception {
        sendObject(AdminCommand.UPDATE_ROUTE);
        sendObject(newVer);
        return receiveObject();
    }

    @Override
    public ResponseFromServer deleteRoute(int id) throws Exception {
        sendObject(AdminCommand.DELETE_ROUTE);
        sendObject(id);
        return receiveObject();
    }

    @Override
    public List<Route> getAllRoutes() throws Exception {
        sendObject(AdminCommand.GET_ALL_ROUTES);
        return receiveObject();
    }

    @Override
    public ResponseFromServer addRouteStationToRoute(RouteStation routeStation) throws Exception {
        sendObject(AdminCommand.ADD_ROUTE_STATION_TO_ROUTE);
        sendObject(routeStation);
        return receiveObject();
    }

    @Override
    public ResponseFromServer deleteRouteStationFromRoute(RouteStation routeStation) throws Exception {
        sendObject(AdminCommand.DELETE_ROUTE_STATION_TO_ROUTE);
        sendObject(routeStation);
        return receiveObject();
    }

    @Override
    public ResponseFromServer editRouteStation(int routeId, int stationId, RouteStation routeStation) throws Exception {
        sendObject(AdminCommand.EDIT_ROUTE_STATION);
        sendObject(routeId);
        sendObject(stationId);
        sendObject(routeStation);
        return receiveObject();
    }

    @Override
    public List<RouteStation> getAllRouteStationsFromRoute(int routeId) throws Exception {
        sendObject(AdminCommand.GET_ALL_ROUTE_STATIONS_FROM_ROUTE);
        sendObject(routeId);
        return receiveObject();
    }

    @Override
    public void exit() throws Exception {
        sendObject(AdminCommand.EXIT);
    }

    @Override
    public boolean checkIfLoginExistsAdmin(String login) throws Exception {
        sendObject(AdminCommand.CHECK_IF_LOGIN_EXISTS);
        sendObject(login);
        return receiveObject() == ResponseFromServer.SUCCESSFULLY;
    }
    ////////////////////////////////////////////////////////////////////////////

    //Inherited from DriverAccess
    @Override
    public void driverExit() throws Exception {
        sendObject(DriverCommand.EXIT);
    }

    @Override
    public List<Pair<Route, List<RouteStation>>> getRoutes() throws Exception {
        sendObject(DriverCommand.GET_MINE_ROUTES);
        return receiveObject();
    }

    @Override
    public Driver getProfile() throws Exception {
        sendObject(DriverCommand.GET_MINE_PROFILE);
        return receiveObject();
    }

    @Override
    public ResponseFromServer updateProfile(Driver obj) throws Exception {
        sendObject(DriverCommand.UPDATE_PROFILE);
        sendObject(obj);
        return receiveObject();
    }

    @Override
    public int getRouteTicketsAmountForLastMonth(int routeId) throws Exception {
        sendObject(AdminCommand.GET_ROUTE_TICKETS_AMOUNT_FOR_LAST_MONTH);
        sendObject(routeId);
        return receiveObject();
    }
    /////////////////////////////////////////////////////////
}
