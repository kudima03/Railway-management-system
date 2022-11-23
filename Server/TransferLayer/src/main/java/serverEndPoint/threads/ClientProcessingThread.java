package serverEndPoint.threads;

import commandsToServer.AdminCommand;
import commandsToServer.CommonCommand;
import commandsToServer.DriverCommand;
import commandsToServer.UserCommand;
import customContainers.Pair;
import databaseEntities.Classes.*;
import databaseEntities.Enums.PurchaseStatus;
import databaseEntities.Enums.UserType;
import entityManagers.*;
import responcesFromServer.ResponseFromServer;
import serverEndPoint.ConnectedClientInfo;
import services.CostCalculator;
import services.TicketPurchaseService;
import services.TimeTableService;
import services.UserEditService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//В этом потоке происходит взаимодействие с клиентом
public class ClientProcessingThread extends Thread {

    private final ConnectedClientInfo clientInfo;

    private final ObjectOutputStream objectOutputStream;

    private final ObjectInputStream objectInputStream;

    public ClientProcessingThread(ConnectedClientInfo clientInfo) throws IOException {
        this.clientInfo = clientInfo;
        var socket = clientInfo.getConnectionSocket();
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    private void sendObject(Serializable object) throws IOException {

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    private <T> T receiveObject() throws IOException, ClassNotFoundException {

        return (T) objectInputStream.readObject();
    }

    @Override
    public void run() {

        while (true) {
            try {
                switch (clientLobby()) {
                    case ADMIN -> {
                        adminProcessing();
                    }
                    case USER -> {
                        clientProcessing();
                    }
                    case DRIVER -> {
                        driverProcessing();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void interrupt() {
        try {
            //Заканчиваем работу
            clientInfo.getConnectionSocket().close();
        } catch (IOException e) { //Аналогично
            throw new RuntimeException(e);
        }
        super.interrupt();
    }

    public ConnectedClientInfo getClientInfo() {
        return clientInfo;
    }

    private UserType clientLobby() throws Exception {

        UsersManager usersManager = new UsersManager();
        while (true) {

            CommonCommand command = receiveObject();
            switch (command) {
                case AUTHORIZE -> {

                    String login = receiveObject();
                    String password = receiveObject();
                    var user = usersManager.get(login, password);
                    clientInfo.setIdInDB(user.getId());
                    var userType = user.getUserType();
                    sendObject(userType);
                    if (userType == UserType.UNDEFINED) continue;
                    return userType;
                }
                case CHECK_IF_LOGIN_EXISTS -> {

                    String login = receiveObject();
                    var user = usersManager.get(login);
                    if (user.getId() == 0) {
                        sendObject(ResponseFromServer.NOT_FOUND);
                    } else {
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    }
                }
                case REGISTER -> {

                    UserType userType = receiveObject();
                    String login = receiveObject();
                    String password = receiveObject();
                    String email = receiveObject();
                    try {
                        int id = usersManager.createUser(new User(0, login, password, email, userType));
                        clientInfo.setIdInDB(id);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                        continue;
                    }
                    sendObject(ResponseFromServer.SUCCESSFULLY);
                    return userType;
                }
                default -> {
                    sendObject(ResponseFromServer.UNKNOWN_COMMAND);
                }
            }
        }
    }

    private void adminProcessing() throws IOException, ClassNotFoundException {

        DriversManager driversManager = new DriversManager();
        TrainTypesManager trainTypesManager = new TrainTypesManager();
        StationsManager stationsManager = new StationsManager();
        TrainsManager trainsManager = new TrainsManager();
        RoutesManager routesManager = new RoutesManager();
        RouteStationsManager routeStationsManager = new RouteStationsManager();
        UsersManager usersManager = new UsersManager();
        TicketsManager ticketsManager = new TicketsManager();
        PassengersManager passengersManager = new PassengersManager();
        DocumentTypesManager documentTypesManager = new DocumentTypesManager();
        while (true) {

            AdminCommand command = receiveObject();
            switch (command) {

                case GET_ALL_DRIVERS -> {
                    try {
                        var drivers = driversManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(drivers));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case CREATE_DRIVER -> {
                    Driver newDriver = receiveObject();
                    try {
                        driversManager.createDriver(newDriver);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case EDIT_DRIVER -> {
                    Driver newVersion = receiveObject();
                    try {
                        driversManager.updateDriver(newVersion);
                        usersManager.updateUser(newVersion.getUser());
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case DELETE_DRIVER -> {
                    int id = receiveObject();
                    try {
                        driversManager.deleteDriver(id);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case GET_ALL_TRAIN_TYPES -> {
                    try {
                        var trainTypes = trainTypesManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(trainTypes));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_ALL_STATIONS -> {
                    try {
                        var stations = stationsManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(stations));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case CREATE_STATION -> {
                    Station station = receiveObject();
                    try {
                        stationsManager.createStation(station);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case EDIT_STATION -> {
                    Station newVer = receiveObject();
                    try {
                        stationsManager.updateStation(newVer);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case DELETE_STATION -> {
                    int id = receiveObject();
                    try {
                        stationsManager.deleteStation(id);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case GET_ALL_TRAINS -> {
                    try {
                        var trains = trainsManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(trains));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case CREATE_TRAIN -> {
                    Train train = receiveObject();
                    try {
                        trainsManager.createTrain(train);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case EDIT_TRAIN -> {
                    Train newVer = receiveObject();
                    try {
                        trainsManager.updateTrain(newVer);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case DELETE_TRAIN -> {
                    int id = receiveObject();
                    try {
                        trainsManager.deleteTrain(id);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case GET_ALL_ROUTES -> {
                    try {
                        var routes = routesManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(routes));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case CREATE_ROUTE -> {
                    Route route = receiveObject();
                    try {
                        routesManager.createRoute(route);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case DELETE_ROUTE -> {
                    int id = receiveObject();
                    try {
                        routesManager.deleteRoute(id);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case UPDATE_ROUTE -> {
                    Route newVersion = receiveObject();
                    try {
                        routesManager.updateRoute(newVersion);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case ADD_ROUTE_STATION_TO_ROUTE -> {
                    RouteStation routeStation = receiveObject();
                    try {
                        routeStationsManager.createRouteStation(routeStation);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case DELETE_ROUTE_STATION_TO_ROUTE -> {
                    RouteStation routeStation = receiveObject();
                    try {
                        routeStationsManager.deleteRouteStation(routeStation);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case EDIT_ROUTE_STATION -> {
                    int routeId = receiveObject();
                    int stationId = receiveObject();
                    RouteStation routeStation = receiveObject();
                    try {
                        routeStationsManager.updateRouteStation(routeId, stationId, routeStation);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case GET_ALL_ROUTE_STATIONS_FROM_ROUTE -> {
                    int id = receiveObject();
                    try {
                        var routeStations = routeStationsManager.get(id);
                        sendObject(new ArrayList<>(routeStations));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case EXIT -> {
                    return;
                }
                case CHECK_IF_LOGIN_EXISTS -> {
                    String login = receiveObject();
                    try {
                        var res = usersManager.get(login).getId() != 0;
                        if (res) {
                            sendObject(ResponseFromServer.SUCCESSFULLY);
                        } else {
                            sendObject(ResponseFromServer.NOT_FOUND);
                        }
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case GET_ROUTE_TICKETS_AMOUNT_FOR_LAST_MONTH -> {
                    int routeId = receiveObject();
                    try {

                        int count = ticketsManager.get((var ticket) ->
                                ticket.getRoute().getId() == routeId
                                        && (new Date().getTime() - ticket.getClearanceTime().getTime()) / (24 * 60 * 60 * 1000) <= 30
                                        && ticket.getPurchaseStatus() == PurchaseStatus.Paid
                        ).size();
                        sendObject(count);
                    } catch (Exception e) {
                        sendObject(0);
                    }
                }
                case GET_ALL_DOCUMENT_TYPES -> {
                    try {
                        var documentTypes = documentTypesManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(documentTypes));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_ALL_PASSENGERS -> {
                    try {
                        var passengers = passengersManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(passengers));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_ALL_TICKETS -> {
                    try {
                        var tickets = ticketsManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(tickets));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
            }
        }
    }

    private void clientProcessing() throws IOException, ClassNotFoundException {

        StationsManager stationsManager = new StationsManager();
        TimeTableService timeTableService = new TimeTableService();
        RouteStationsManager routeStationsManager = new RouteStationsManager();
        UserPassengersManager userPassengersManager = new UserPassengersManager();
        PassengersManager passengersManager = new PassengersManager();
        RoutesManager routesManager = new RoutesManager();
        TicketPurchaseService ticketPurchaseService = new TicketPurchaseService();
        TicketsManager ticketsManager = new TicketsManager();
        UserTicketsManager userTicketsManager = new UserTicketsManager();
        RulesManager rulesManager = new RulesManager();
        DocumentTypesManager documentTypesManager = new DocumentTypesManager();
        while (true) {

            UserCommand command = receiveObject();
            switch (command) {

                case EXIT_TO_AUTHORIZATION -> {
                    return;
                }
                case GET_ALL_STATIONS -> {
                    try {
                        var stations = stationsManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(stations));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_TIME_TABLE -> {
                    int startStationId = receiveObject();
                    int endStationId = receiveObject();
                    Date date = receiveObject();
                    try {
                        var routeDistanceInfo =
                                timeTableService.getRouteDistanceInfo(startStationId, endStationId, date);
                        sendObject(routeDistanceInfo);
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_ROUTE_STATIONS -> {
                    int routeId = receiveObject();
                    try {
                        var routeStations = routeStationsManager.get(routeId);
                        sendObject(new ArrayList<>(routeStations));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_PASSENGERS -> {
                    try {
                        var passengers = userPassengersManager.getAll(clientInfo.getIdInDB());
                        sendObject(new ArrayList<>(passengers));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case CREATE_PASSENGER -> {
                    Passenger passenger = receiveObject();
                    try {
                        int passengerId = passengersManager.createPassenger(passenger);
                        userPassengersManager.addPassengerToUser(passengerId, clientInfo.getIdInDB());
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case UPDATE_PASSENGER -> {
                    Passenger passengerToEdit = receiveObject();
                    try {
                        passengersManager.updatePassenger(passengerToEdit);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case DELETE_PASSENGER -> {
                    int id = receiveObject();
                    try {
                        userPassengersManager.deletePassengerFromUser(id, clientInfo.getIdInDB());
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case CALCULATE_COST -> {
                    int routeId = receiveObject();
                    int depStationId = receiveObject();
                    int arrStationId = receiveObject();
                    try {
                        var route = routesManager.getById(routeId);
                        var arrStation = routeStationsManager.get(routeId, arrStationId);
                        var depStation = routeStationsManager.get(routeId, depStationId);
                        var cost = CostCalculator.calculateCost(route, depStation, arrStation);
                        sendObject(cost);
                    } catch (Exception e) {
                        sendObject(0F);
                    }
                }
                case CREATE_TICKET -> {
                    int passengerId = receiveObject();
                    int routeId = receiveObject();
                    int depStationId = receiveObject();
                    int arrStationId = receiveObject();
                    try {
                        int ticketId = ticketPurchaseService.createTicket(passengerId, routeId, depStationId, arrStationId);
                        userTicketsManager.addTicketToUser(ticketId, clientInfo.getIdInDB());
                        var ticket = ticketsManager.getById(ticketId);
                        sendObject(ticket);
                    } catch (Exception e) {
                        sendObject(new Ticket());
                    }
                }
                case PAY_TICKET -> {
                    int ticketId = receiveObject();
                    try {
                        ticketPurchaseService.setTicketStatusPaid(ticketId);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case GET_ALL_TICKETS -> {
                    try {
                        var userTickets = userTicketsManager.get(Objects::nonNull, clientInfo.getIdInDB());
                        sendObject(new ArrayList<>(userTickets));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_RULES -> {
                    try {
                        var rules = rulesManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(rules));
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case EDIT_PROFILE -> {
                    String newPassword = receiveObject();
                    String newEmail = receiveObject();
                    try {
                        UserEditService.updateUser(clientInfo.getIdInDB(), newPassword, newEmail);
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
                case GET_DOCUMENT_TYPES -> {
                    try {
                        var types = documentTypesManager.get(Objects::nonNull);
                        sendObject(new ArrayList<>(types));
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
            }
        }
    }

    private void driverProcessing() throws IOException, ClassNotFoundException {

        RoutesManager routesManager = new RoutesManager();
        RouteStationsManager routeStationsManager = new RouteStationsManager();
        DriversManager driversManager = new DriversManager();
        UsersManager usersManager = new UsersManager();
        while (true) {
            DriverCommand command = receiveObject();
            switch (command) {

                case EXIT -> {
                    return;
                }
                case GET_MINE_ROUTES -> {
                    try {
                        var routes = routesManager.get((var route) ->
                                route.getDriver().getUser().getId() == clientInfo.getIdInDB());
                        ArrayList<Pair<Route, List<RouteStation>>> pairs = new ArrayList<>();
                        for (var route : routes) {
                            var list = routeStationsManager.get(route.getId());
                            pairs.add(new Pair<>(route, list));
                        }
                        sendObject(pairs);
                    } catch (Exception e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_MINE_PROFILE -> {
                    try {
                        var profile = driversManager.get((var driver) ->
                                driver.getUser().getId() == clientInfo.getIdInDB()).get(0);
                        sendObject(profile);
                    } catch (Exception e) {
                        sendObject(new Driver());
                    }
                }
                case UPDATE_PROFILE -> {
                    Driver driver = receiveObject();
                    try {
                        usersManager.updateUser(driver.getUser());
                        sendObject(ResponseFromServer.SUCCESSFULLY);
                    } catch (Exception e) {
                        sendObject(ResponseFromServer.ERROR);
                    }
                }
            }
        }
    }
}