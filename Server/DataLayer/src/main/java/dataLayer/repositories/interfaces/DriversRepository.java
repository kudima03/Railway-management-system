package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Driver;

import java.util.List;

public interface DriversRepository {

    int create(Driver driver) throws Exception;

    void update(Driver newVersion) throws Exception;

    void delete(int id) throws Exception;

    Driver getById(int id) throws Exception;

    List<Driver> getAll() throws Exception;
}
