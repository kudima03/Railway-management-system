package databaseEntities.Classes;

import java.io.Serializable;

public class Rule  implements Serializable {

    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public Rule() {
        text = "";
    }

    public Rule(String text) {
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (id != rule.id) return false;
        return text.equals(rule.text);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
