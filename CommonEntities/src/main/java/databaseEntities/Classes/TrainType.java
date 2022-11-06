package databaseEntities.Classes;

import java.io.Serializable;

public class TrainType  implements Serializable {

    private int id;
    private String name;
    private float costForStation;

    public TrainType() {
        name = "";
    }

    public TrainType(int id, String name, float costForStation) {
        this.id = id;
        this.name = name;
        this.costForStation = costForStation;
    }

    public float getCostForStation() {
        return costForStation;
    }

    public void setCostForStation(float costForStation) {
        this.costForStation = costForStation;
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
        if (name == null) return;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainType trainType = (TrainType) o;

        if (id != trainType.id) return false;
        return name.equals(trainType.name);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
