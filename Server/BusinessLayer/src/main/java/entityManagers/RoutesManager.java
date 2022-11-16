package entityManagers;

import databaseEntities.Classes.Route;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class RoutesManager {

    public int createRoute(Route obj) throws Exception {

        return DataRepository.routesRepository.create(obj);
    }

    public void updateRoute(Route obj) throws Exception {

        DataRepository.routesRepository.update(obj);
    }

    public void deleteRoute(int id) throws Exception {

        DataRepository.routesRepository.delete(id);
    }

    public Route getById(int id) throws Exception {

        return DataRepository.routesRepository.getById(id);
    }

    public List<Route> get(Predicate<Route> predicate) throws Exception {

        var collection = DataRepository.routesRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
