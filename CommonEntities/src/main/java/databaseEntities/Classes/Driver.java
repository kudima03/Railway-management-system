package databaseEntities.Classes;

import java.io.Serializable;
import java.util.Date;

public class Driver implements Serializable {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private Date dateOfBirth;
    private int experience;
    private byte[] binaryPhoto;
    private User user;

    public Driver() {

        name = "";
        surname = "";
        patronymic = "";
        dateOfBirth = new Date(100, 0, 1);
        binaryPhoto = new byte[0];
        user = new User();
    }

    public Driver(String name, String surname, String patronymic, Date dateOfBirth, int experience, byte[] binaryPhoto, User user) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.experience = experience;
        this.binaryPhoto = binaryPhoto;
        this.user = user;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public byte[] getBinaryPhoto() {
        return binaryPhoto;
    }

    public void setBinaryPhoto(byte[] binaryPhoto) {
        this.binaryPhoto = binaryPhoto;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
