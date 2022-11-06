package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Passenger;

import java.util.List;

public interface PassengersRepository {

    int create(Passenger passenger) throws Exception;
    void update(Passenger newVersion) throws Exception;
    void delete(int id) throws Exception;
    Passenger getById(int id) throws Exception;
    List<Passenger> getAll() throws Exception;
}
