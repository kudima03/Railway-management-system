package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Ticket;

import java.util.List;

public interface TicketsRepository {

    int create(Ticket ticket) throws Exception;
    void update(Ticket newVersion) throws Exception;
    void delete(int id) throws Exception;
    Ticket getById(int id) throws Exception;
    List<Ticket> getAll() throws Exception;
}
