package customContainers;

import java.io.Serializable;

public class Pair <T1, T2> implements Serializable {

    private T1 firstValue;
    private T2 secondValue;

    public Pair() {
    }

    public Pair(T1 firstValue, T2 secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public T1 getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(T1 firstValue) {
        this.firstValue = firstValue;
    }

    public T2 getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(T2 secondValue) {
        this.secondValue = secondValue;
    }
}
