package databaseEntities.Classes;

import databaseEntities.Enums.Sex;

import java.io.Serializable;
import java.util.Date;

public class Passenger  implements Serializable {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;
    private DocumentType documentType;
    private String documentNumber;
    private String email;
    private Date dateOfBirth;
    private Sex sex;

    public Passenger() {

        name = "";
        surname = "";
        patronymic = "";
        phoneNumber = "";
        documentNumber = "";
        email = "";
        dateOfBirth = new Date(100, 0,1);
    }

    public Passenger(String name, String surname, String patronymic, String phoneNumber,
                     DocumentType documentType, String documentNumber, String email, Date dateOfBirth, Sex sex) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passenger passenger = (Passenger) o;

        if (id != passenger.id) return false;
        if (!name.equals(passenger.name)) return false;
        if (!surname.equals(passenger.surname)) return false;
        if (!patronymic.equals(passenger.patronymic)) return false;
        if (!phoneNumber.equals(passenger.phoneNumber)) return false;
        if (!documentType.equals(passenger.documentType)) return false;
        if (!documentNumber.equals(passenger.documentNumber)) return false;
        if (!email.equals(passenger.email)) return false;
        if (!dateOfBirth.equals(passenger.dateOfBirth)) return false;
        return sex == passenger.sex;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
