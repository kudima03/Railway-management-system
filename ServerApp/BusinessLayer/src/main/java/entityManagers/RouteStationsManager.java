package entityManagers;

import databaseEntities.Classes.RouteStation;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class RouteStationsManager {

    public void createRouteStation(RouteStation obj) throws Exception {

        DataRepository.routeStationsRepository.addStationToRoute(obj);
    }

    public void updateRouteStation(int routeId, int stationId, RouteStation obj) throws Exception {

        DataRepository.routeStationsRepository.update(routeId, stationId, obj);
    }

    public void deleteRouteStation(RouteStation obj) throws Exception {

        DataRepository.routeStationsRepository.delete(obj.getRoute().getId(), obj.getStation().getId());
    }

    public RouteStation get(int routeId, int stationId) throws Exception {

        return DataRepository.routeStationsRepository.get(routeId, stationId);
    }

    public List<RouteStation> get(Predicate<RouteStation> predicate, int stationId) throws Exception {

        var collection = DataRepository.routeStationsRepository.getAll(stationId);
        return collection.stream().filter(predicate).toList();
    }

    public List<RouteStation> get(int routeId) throws Exception {

        return DataRepository.routeStationsRepository.getAll(routeId);
    }

}
