package entityManagers;

import dataLayer.dbManagers.DataRepository;
import databaseEntities.Classes.Station;

import java.util.List;
import java.util.function.Predicate;

public class StationsManager {

    public int createStation(Station obj) throws Exception {

        return DataRepository.stationsRepository.create(obj);
    }

    public void updateStation(Station obj) throws Exception {

        DataRepository.stationsRepository.update(obj);
    }

    public void deleteStation(int id) throws Exception {

        DataRepository.stationsRepository.delete(id);
    }

    public Station getById(int id) throws Exception {

        return DataRepository.stationsRepository.getById(id);
    }

    public List<Station> get(Predicate<Station> predicate) throws Exception {

        var collection = DataRepository.stationsRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
