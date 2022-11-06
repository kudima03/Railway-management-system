package entityManagers;

import databaseEntities.Classes.DocumentType;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class DocumentTypesManager {

    public void createDocumentType(DocumentType obj) throws Exception {

        DataRepository.documentTypesRepository.create(obj);
    }

    public void updateDocumentType(DocumentType obj) throws Exception {

        DataRepository.documentTypesRepository.update(obj);
    }

    public void deleteDocumentType(DocumentType obj) throws Exception {

        DataRepository.documentTypesRepository.delete(obj.getId());
    }

    public DocumentType getById(int id) throws Exception {

        return DataRepository.documentTypesRepository.getById(id);
    }

    public List<DocumentType> get(Predicate<DocumentType> predicate) throws Exception {

        var collection = DataRepository.documentTypesRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
