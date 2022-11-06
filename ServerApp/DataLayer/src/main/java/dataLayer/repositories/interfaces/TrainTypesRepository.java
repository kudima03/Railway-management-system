package dataLayer.repositories.interfaces;

import databaseEntities.Classes.TrainType;

import java.util.List;

public interface TrainTypesRepository {

    int create(TrainType trainType) throws Exception;
    void update(TrainType newVersion) throws Exception;
    void delete(int id) throws Exception;
    TrainType getById(int id) throws Exception;
    List<TrainType> getAll() throws Exception;
}
