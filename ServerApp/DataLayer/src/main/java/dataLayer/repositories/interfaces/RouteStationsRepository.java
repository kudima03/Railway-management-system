package dataLayer.repositories.interfaces;

import databaseEntities.Classes.RouteStation;

import java.util.Date;
import java.util.List;

public interface RouteStationsRepository {

    void addStationToRoute(RouteStation routeStation) throws Exception;
    void update(int routeId, int stationId, RouteStation newVersion) throws Exception;
    void delete(int routeId, int stationId) throws Exception;
    RouteStation get(int routeId, int stationId) throws Exception;
    List<RouteStation> get(int stationId, Date date) throws Exception;
    List<RouteStation> getAll(int routeId) throws Exception;
}
