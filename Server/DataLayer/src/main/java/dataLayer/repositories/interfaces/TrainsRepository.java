package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Train;

import java.util.List;

public interface TrainsRepository {

    int create(Train train) throws Exception;
    void update(Train newVersion) throws Exception;
    void delete(int id) throws Exception;
    Train getById(int id) throws Exception;
    List<Train> getAll() throws Exception;
}
