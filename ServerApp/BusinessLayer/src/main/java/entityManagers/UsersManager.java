package entityManagers;

import databaseEntities.Classes.User;
import dataLayer.dbManagers.DataRepository;

import java.util.List;
import java.util.function.Predicate;

public class UsersManager {

    public int createUser(User obj) throws Exception {

        return DataRepository.usersRepository.create(obj);
    }

    public void updateUser(User obj) throws Exception {

        DataRepository.usersRepository.update(obj);
    }

    public void deleteUser(User obj) throws Exception {

        DataRepository.usersRepository.delete(obj.getId());
    }

    public User getById(int id) throws Exception {

        return DataRepository.usersRepository.getById(id);
    }

    public User get(String login, String password) throws Exception {

        return  DataRepository.usersRepository.get(login, password);
    }

    public User get(String login) throws Exception {

        return DataRepository.usersRepository.get(login);
    }

    public List<User> get(Predicate<User> predicate) throws Exception {

        var collection = DataRepository.usersRepository.getAll();
        return collection.stream().filter(predicate).toList();
    }
}
