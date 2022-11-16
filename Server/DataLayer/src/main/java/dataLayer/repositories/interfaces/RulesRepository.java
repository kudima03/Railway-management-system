package dataLayer.repositories.interfaces;

import databaseEntities.Classes.Rule;

import java.util.List;

public interface RulesRepository {

    int create(Rule rule) throws Exception;
    void update(Rule newVersion) throws Exception;
    void delete(int id) throws Exception;
    Rule getById(int id) throws Exception;
    List<Rule> getAll() throws Exception;
}
