package dataLayer.repositories.interfaces;

import databaseEntities.Classes.DocumentType;

import java.util.List;

public interface DocumentTypesRepository {

    void create(DocumentType documentType) throws Exception;
    void update(DocumentType newVersion) throws Exception;
    void delete(int id) throws Exception;
    DocumentType getById(int id) throws Exception;
    List<DocumentType> getAll() throws Exception;
}
