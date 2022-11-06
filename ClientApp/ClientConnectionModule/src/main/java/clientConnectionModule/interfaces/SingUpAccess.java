package clientConnectionModule.interfaces;

import databaseEntities.Enums.UserType;

public interface SingUpAccess {

    UserType singUp(String login, String password) throws Exception;
}
