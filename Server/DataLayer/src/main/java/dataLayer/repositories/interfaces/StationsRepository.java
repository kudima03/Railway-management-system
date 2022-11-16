package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Station;

import java.util.List;

public interface StationsRepository {
    int create(Station station) throws Exception;
    void update(Station newVersion) throws Exception;
    void delete(int id) throws Exception;
    Station getById(int id) throws Exception;
    List<Station> getAll() throws Exception;
}
