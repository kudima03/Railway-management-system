package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Route;

import java.util.List;

public interface RoutesRepository {

    int create(Route route) throws Exception;
    void update(Route newVersion) throws Exception;
    void delete(int id) throws Exception;
    Route getById(int id) throws Exception;
    List<Route> getAll() throws Exception;
}
