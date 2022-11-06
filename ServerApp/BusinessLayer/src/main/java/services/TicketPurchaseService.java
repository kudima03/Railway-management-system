package services;

import dataLayer.dbManagers.DataRepository;
import databaseEntities.Classes.Ticket;
import databaseEntities.Enums.PurchaseStatus;

import java.util.Date;

public class TicketPurchaseService {

    public int createTicket(int passengerId, int routeId, int depStId, int arrivalStId) throws Exception {

        Ticket ticket = new Ticket();

        var passenger = DataRepository.passengersRepository.getById(passengerId);
        var route = DataRepository.routesRepository.getById(routeId);
        var departureStation = DataRepository.stationsRepository.getById(depStId);
        var arrivalStation = DataRepository.stationsRepository.getById(arrivalStId);

        ticket.setPassenger(passenger);
        ticket.setRoute(route);
        ticket.setDepartureStation(departureStation);
        ticket.setArrivalStation(arrivalStation);
        ticket.setPurchaseStatus(PurchaseStatus.WaitingForPayment);

        ticket.setCost(CostCalculator.calculateCost(route,
                DataRepository.routeStationsRepository.get(routeId, depStId),
                DataRepository.routeStationsRepository.get(routeId, arrivalStId)));

        ticket.setClearanceTime(new Date(System.currentTimeMillis()));

        ticket.setRouteLength(Math.abs(arrivalStation.getKmFromMinsk() - departureStation.getKmFromMinsk()));
        return DataRepository.ticketsRepository.create(ticket);
    }

    public void setTicketStatusPaid(int ticketId) throws Exception {

        var ticket = DataRepository.ticketsRepository.getById(ticketId);
        ticket.setPurchaseStatus(PurchaseStatus.Paid);
        ticket.setClearanceTime(new Date());
        DataRepository.ticketsRepository.update(ticket);
    }

}
