package entityManagers;

import databaseEntities.Classes.Train;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class TrainsManager {

    public int createTrain(Train obj) throws Exception {

        return DataRepository.trainsRepository.create(obj);
    }

    public void updateTrain(Train obj) throws Exception {

        DataRepository.trainsRepository.update(obj);
    }

    public void deleteTrain(int id) throws Exception {

        DataRepository.trainsRepository.delete(id);
    }

    public Train getById(int id) throws Exception {

        return DataRepository.trainsRepository.getById(id);
    }

    public List<Train> get(Predicate<Train> predicate) throws Exception {

        var collection = DataRepository.trainsRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
