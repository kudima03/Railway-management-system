package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Passenger;

import java.util.List;

public interface UserPassengersRepository {

    void addPassengerToUser(int userId, int passengerId) throws Exception;
    void delete(int userId, int passengerId) throws Exception;
    Passenger getById(int userId, int passengerId) throws Exception;
    List<Passenger> getAll(int userId) throws Exception;
}
