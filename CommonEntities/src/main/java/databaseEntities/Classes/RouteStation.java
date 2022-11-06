package databaseEntities.Classes;

import java.io.Serializable;
import java.util.Date;

public class RouteStation implements Serializable {

    private Route route;
    private Station station;
    private int ordinalNumber; //From route start
    private Date arrivalDateTime;
    private Date stopTime;
    private Date departureDateTime;
    private String trackNumber; //Путь
    private int platformNumber;

    public RouteStation() {

        route = new Route();
        station = new Station();
        arrivalDateTime = new Date(100, 0, 1);
        departureDateTime = new Date(100, 0, 1);
        stopTime = new Date(100, 0, 1);
        trackNumber = "";
    }

    public RouteStation(Station station, int ordinalNumber, Date arrivalDateTime, Date stopTime,
                        Date departureDateTime, String trackNumber, int platformNumber) {
        this.station = station;
        this.ordinalNumber = ordinalNumber;
        this.arrivalDateTime = arrivalDateTime;
        this.stopTime = stopTime;
        this.departureDateTime = departureDateTime;
        this.trackNumber = trackNumber;
        this.platformNumber = platformNumber;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(Date arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(int platformNumber) {
        this.platformNumber = platformNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteStation that = (RouteStation) o;

        if (ordinalNumber != that.ordinalNumber) return false;
        if (platformNumber != that.platformNumber) return false;
        if (!route.equals(that.route)) return false;
        if (!station.equals(that.station)) return false;
        if (!arrivalDateTime.equals(that.arrivalDateTime)) return false;
        if (!stopTime.equals(that.stopTime)) return false;
        if (!departureDateTime.equals(that.departureDateTime)) return false;
        return trackNumber.equals(that.trackNumber);
    }

    @Override
    public int hashCode() {
        int result = route.hashCode();
        result = 31 * result + station.hashCode();
        result = 31 * result + ordinalNumber;
        result = 31 * result + arrivalDateTime.hashCode();
        result = 31 * result + stopTime.hashCode();
        result = 31 * result + departureDateTime.hashCode();
        result = 31 * result + trackNumber.hashCode();
        result = 31 * result + platformNumber;
        return result;
    }
}
