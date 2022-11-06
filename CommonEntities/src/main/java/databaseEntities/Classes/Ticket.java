package databaseEntities.Classes;

import databaseEntities.Enums.PurchaseStatus;

import java.io.Serializable;
import java.util.Date;

public class Ticket  implements Serializable {

    private int id;
    private Passenger passenger;
    private Route route;
    private Station departureStation;
    private Station arrivalStation;
    private float cost;
    private Date clearanceTime;
    private PurchaseStatus purchaseStatus;
    private int routeLength;

    public Ticket() {

        passenger = new Passenger();
        route = new Route();
        departureStation = new Station();
        arrivalStation = new Station();
        clearanceTime = new Date(100, 0, 1);
    }

    public Ticket(Passenger passenger, Route route, Station departureStation, Station arrivalStation,
                  float cost, Date clearanceTime, PurchaseStatus purchaseStatus, int routeLength) {
        this.passenger = passenger;
        this.route = route;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.cost = cost;
        this.clearanceTime = clearanceTime;
        this.purchaseStatus = purchaseStatus;
        this.routeLength = routeLength;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(Station departureStation) {
        this.departureStation = departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(Station arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getClearanceTime() {
        return clearanceTime;
    }

    public void setClearanceTime(Date clearanceTime) {
        this.clearanceTime = clearanceTime;
    }

    public PurchaseStatus getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(PurchaseStatus purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public int getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(int routeLength) {
        this.routeLength = routeLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != ticket.id) return false;
        if (Float.compare(ticket.cost, cost) != 0) return false;
        if (routeLength != ticket.routeLength) return false;
        if (!passenger.equals(ticket.passenger)) return false;
        if (!route.equals(ticket.route)) return false;
        if (!departureStation.equals(ticket.departureStation)) return false;
        if (!arrivalStation.equals(ticket.arrivalStation)) return false;
        if (!clearanceTime.equals(ticket.clearanceTime)) return false;
        return purchaseStatus == ticket.purchaseStatus;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
