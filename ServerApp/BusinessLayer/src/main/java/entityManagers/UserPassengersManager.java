package entityManagers;

import dataLayer.dbManagers.DataRepository;
import databaseEntities.Classes.Passenger;

import java.util.List;
import java.util.function.Predicate;

public class UserPassengersManager {

    public void addPassengerToUser(int passengerId, int userId) throws Exception {

        DataRepository.userPassengersRepository.addPassengerToUser(userId, passengerId);
    }

    public void deletePassengerFromUser(int passengerId, int userId) throws Exception {

        DataRepository.userPassengersRepository.delete(userId, passengerId);
    }

    public Passenger getById(int userId, int passengerId) throws Exception {

        return DataRepository.userPassengersRepository.getById(userId, passengerId);
    }

    public List<Passenger> get(Predicate<Passenger> predicate, int userId) throws Exception {

        var collection = DataRepository.userPassengersRepository.getAll(userId);
        return collection.stream().filter(predicate).toList();
    }

    public List<Passenger> getAll(int userId) throws Exception {

        return DataRepository.userPassengersRepository.getAll(userId);
    }
}
