package entityManagers;

import databaseEntities.Classes.Driver;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class DriversManager {

    public int createDriver(Driver obj) throws Exception {

        return DataRepository.driversRepository.create(obj);
    }

    public void updateDriver(Driver obj) throws Exception {

        DataRepository.driversRepository.update(obj);
    }

    public void deleteDriver(int driverId) throws Exception {

        DataRepository.driversRepository.delete(driverId);
    }

    public Driver getById(int id) throws Exception {

        return DataRepository.driversRepository.getById(id);
    }

    public List<Driver> get(Predicate<Driver> predicate) throws Exception {

        var collection = DataRepository.driversRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
