package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Ticket;

import java.util.List;

public interface UserTicketsRepository {

    void addTicketToUser(int userId, int ticketId) throws Exception;
    void delete(int userId, int ticketId) throws Exception;
    Ticket getById(int userId, int ticketId) throws Exception;
    List<Ticket> getAll(int userId) throws Exception;
}
