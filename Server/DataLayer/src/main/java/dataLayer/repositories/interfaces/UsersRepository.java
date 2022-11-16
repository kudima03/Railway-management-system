package dataLayer.repositories.interfaces;

import databaseEntities.Classes.User;

import java.util.List;

public interface UsersRepository {

    int create(User user) throws Exception;
    void update(User newVersion) throws Exception;
    void delete(int id) throws Exception;
    User getById(int id) throws Exception;
    User get(String login, String password) throws Exception;
    User get (String login) throws Exception;
    List<User> getAll() throws Exception;
}
