package entityManagers;

import databaseEntities.Classes.Rule;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class RulesManager {

    public int createRule(Rule obj) throws Exception {

        return DataRepository.rulesRepository.create(obj);
    }

    public void updateRule(Rule obj) throws Exception {

        DataRepository.rulesRepository.update(obj);
    }

    public void deleteRule(Rule obj) throws Exception {

        DataRepository.rulesRepository.delete(obj.getId());
    }

    public Rule getById(int id) throws Exception {

        return DataRepository.rulesRepository.getById(id);
    }

    public List<Rule> get(Predicate<Rule> predicate) throws Exception {

        var collection = DataRepository.rulesRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
