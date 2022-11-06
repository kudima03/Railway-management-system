package databaseEntities.Classes;

import java.io.Serializable;

public class Station  implements Serializable {

    private int id;
    private String name;
    private String address;
    private int kmFromMinsk;

    public Station() {

        name = "";
        address = "";
    }

    public Station(String name, String address, int kmFromMinsk) {
        this.name = name;
        this.address = address;
        this.kmFromMinsk = kmFromMinsk;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getKmFromMinsk() {
        return kmFromMinsk;
    }

    public void setKmFromMinsk(int kmFromMinsk) {
        this.kmFromMinsk = kmFromMinsk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        if (id != station.id) return false;
        if (kmFromMinsk != station.kmFromMinsk) return false;
        if (!name.equals(station.name)) return false;
        return address.equals(station.address);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
