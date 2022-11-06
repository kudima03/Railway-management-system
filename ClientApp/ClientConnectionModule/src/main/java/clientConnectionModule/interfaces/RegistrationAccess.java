package clientConnectionModule.interfaces;

import databaseEntities.Enums.UserType;
import responcesFromServer.ResponseFromServer;

public interface RegistrationAccess {

    ResponseFromServer registration(String login, String password, String email, UserType userType) throws Exception;

    boolean checkIfLoginExists(String login) throws Exception;

}
