package services;

import dataLayer.dbManagers.DataRepository;

public class UserEditService {

    public static void updateUser(int userId, String newPassword, String newEmail) throws Exception{

        var user = DataRepository.usersRepository.getById(userId);
        user.setPassword(newPassword);
        user.setEmail(newEmail);
    }
}
