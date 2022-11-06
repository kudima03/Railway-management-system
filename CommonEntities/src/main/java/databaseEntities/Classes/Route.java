package databaseEntities.Classes;

import databaseEntities.Enums.Periodicity;

import java.io.Serializable;

public class Route  implements Serializable {

    private int id;
    private Train train;
    private Driver driver;
    private int number;
    private Periodicity periodicity;

    public Route() {
        train = new Train();
        driver = new Driver();
    }

    public Route(int id, Train train, Driver driver, int number, Periodicity periodicity) {
        this.id = id;
        this.train = train;
        this.driver = driver;
        this.number = number;
        this.periodicity = periodicity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (id != route.id) return false;
        if (number != route.number) return false;
        if (!train.equals(route.train)) return false;
        if (!driver.equals(route.driver)) return false;
        return periodicity == route.periodicity;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
