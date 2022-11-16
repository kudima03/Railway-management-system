package entityManagers;

import dataLayer.dbManagers.DataRepository;
import databaseEntities.Classes.Ticket;

import java.util.List;
import java.util.function.Predicate;

public class UserTicketsManager {

    public void addTicketToUser(int ticketId, int userId) throws Exception {

        DataRepository.userTicketsRepository.addTicketToUser(userId, ticketId);
    }

    public void deleteTicketFromUser(int ticketId, int userId) throws Exception {

        DataRepository.userTicketsRepository.delete(ticketId, userId);
    }

    public Ticket getById(int userId, int ticketId) throws Exception {

        return DataRepository.userTicketsRepository.getById(userId, ticketId);
    }

    public List<Ticket> get(Predicate<Ticket> predicate, int userId) throws Exception {

        var collection = DataRepository.userTicketsRepository.getAll(userId);
        return collection.stream().filter(predicate).toList();
    }
}
