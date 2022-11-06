package databaseEntities.Classes;

import java.io.Serializable;

public class Train  implements Serializable {

    private int id;
    private TrainType type;
    private int number;

    public Train() {
    }

    public Train(int id, TrainType type, int number) {
        this.id = id;
        this.type = type;
        this.number = number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TrainType getType() {
        return type;
    }

    public void setType(TrainType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Train train = (Train) o;

        if (id != train.id) return false;
        if (number != train.number) return false;
        return type.equals(train.type);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
