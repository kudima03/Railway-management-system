package databaseEntities.Classes;

import java.io.Serializable;

public class DocumentType implements Serializable {
    private int id;
    private String typeName;

    public DocumentType() {
        typeName = "";
    }

    public DocumentType(String typeName) {
        this.typeName = typeName;
    }

    public DocumentType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
