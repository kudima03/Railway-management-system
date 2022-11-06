package clientConnectionModule.interfaces;

import customContainers.Pair;
import databaseEntities.Classes.Driver;
import databaseEntities.Classes.Route;
import databaseEntities.Classes.RouteStation;
import responcesFromServer.ResponseFromServer;

import java.util.List;

public interface DriverAccess {

    void driverExit() throws Exception;
    List<Pair<Route, List<RouteStation>>> getRoutes() throws Exception;
    Driver getProfile()throws Exception;
    ResponseFromServer updateProfile(Driver obj) throws Exception;
}
