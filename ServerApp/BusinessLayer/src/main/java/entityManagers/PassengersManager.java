package entityManagers;

import databaseEntities.Classes.Passenger;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class PassengersManager {

    public int createPassenger(Passenger obj) throws Exception {

        return DataRepository.passengersRepository.create(obj);
    }

    public void updatePassenger(Passenger obj) throws Exception {

        DataRepository.passengersRepository.update(obj);
    }

    public void deletePassenger(int passengerId) throws Exception {

        DataRepository.passengersRepository.delete(passengerId);
    }

    public Passenger getById(int id) throws Exception {

        return DataRepository.passengersRepository.getById(id);
    }

    public List<Passenger> get(Predicate<Passenger> predicate) throws Exception {

        var collection = DataRepository.passengersRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
