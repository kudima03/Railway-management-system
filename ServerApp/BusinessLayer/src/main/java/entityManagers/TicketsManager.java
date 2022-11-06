package entityManagers;

import databaseEntities.Classes.Ticket;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class TicketsManager {

    public int createTicket(Ticket obj) throws Exception {

        return DataRepository.ticketsRepository.create(obj);
    }

    public void updateTicket(Ticket obj) throws Exception {

        DataRepository.ticketsRepository.update(obj);
    }

    public void deleteTicket(Ticket obj) throws Exception {

        DataRepository.ticketsRepository.delete(obj.getId());
    }

    public Ticket getById(int id) throws Exception {

        return DataRepository.ticketsRepository.getById(id);
    }

    public List<Ticket> get(Predicate<Ticket> predicate) throws Exception {

        var collection = DataRepository.ticketsRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
