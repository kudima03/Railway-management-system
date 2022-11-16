package dataLayer.dbManagers;

import dataLayer.repositories.implementations.*;
import dataLayer.repositories.interfaces.*;

public class DataRepository {

    public static final DocumentTypesRepository documentTypesRepository;
    public static final DriversRepository driversRepository;
    public static final PassengersRepository passengersRepository;
    public static final RoutesRepository routesRepository;
    public static final RouteStationsRepository routeStationsRepository;
    public static final RulesRepository rulesRepository;
    public static final StationsRepository stationsRepository;
    public static final TicketsRepository ticketsRepository;
    public static final TrainsRepository trainsRepository;
    public static final TrainTypesRepository trainTypesRepository;
    public static final UserPassengersRepository userPassengersRepository;
    public static final UsersRepository usersRepository;
    public static final UserTicketsRepository userTicketsRepository;

    static {
        documentTypesRepository = new DocumentTypesDBRepository();
        driversRepository = new DriversDBRepository();
        passengersRepository = new PassengersDBRepository();
        routesRepository = new RoutesDBRepository();
        routeStationsRepository = new RouteStationsDBRepository();
        rulesRepository = new RulesDBRepository();
        stationsRepository = new StationsDBRepository();
        ticketsRepository = new TicketsDBRepository();
        trainsRepository = new TrainsDBRepository();
        trainTypesRepository = new TrainTypesDBRepository();
        userPassengersRepository = new UserPassengersDBRepository();
        usersRepository = new UsersDBRepository();
        userTicketsRepository = new UserTicketsDBRepository();
    }
}
