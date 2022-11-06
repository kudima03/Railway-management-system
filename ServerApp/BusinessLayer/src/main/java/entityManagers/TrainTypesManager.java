package entityManagers;

import databaseEntities.Classes.TrainType;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class TrainTypesManager {

    public int createTrainType(TrainType obj) throws Exception {

        return DataRepository.trainTypesRepository.create(obj);
    }

    public void updateTrainType(TrainType obj) throws Exception {

        DataRepository.trainTypesRepository.update(obj);
    }

    public void deleteTrainType(int id) throws Exception {

        DataRepository.trainTypesRepository.delete(id);
    }

    public TrainType getById(int id) throws Exception {

        return DataRepository.trainTypesRepository.getById(id);
    }

    public List<TrainType> get(Predicate<TrainType> predicate) throws Exception {

        var collection = DataRepository.trainTypesRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }

}
