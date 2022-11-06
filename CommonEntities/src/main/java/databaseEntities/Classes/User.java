package databaseEntities.Classes;

import databaseEntities.Enums.UserType;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String login;
    private String password;
    private String email;
    private UserType userType;

    public User() {
        login = "";
        password = "";
        email = "";
        userType = UserType.UNDEFINED;
    }

    public User(int id, String login, String password, String email, UserType userType) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        if (id != user.id) return false;
        if (!login.equals(user.login)) return false;
        if (!password.equals(user.password)) return false;
        if (!email.equals(user.email)) return false;
        return userType.equals(user.userType);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
