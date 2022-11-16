package application.controllers.admin.driversOperationsControllers;

import application.AlertManager;
import application.Application;
import application.InputValidation;
import clientConnectionModule.interfaces.AdminAccess;
import databaseEntities.Classes.Driver;
import databaseEntities.Classes.User;
import databaseEntities.Enums.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddOrEditDriverController {

    private AdminAccess access;

    private Driver driver;

    private VBox output;

    @FXML
    public Label titleLabel;
    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private TextField experienceInput;

    @FXML
    private TextField loginInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField patronymicInput;

    @FXML
    private ImageView profileImagePreview;

    @FXML
    private TextField surnameInput;

    @FXML
    public TextField emailInput;

    @FXML
    void onConfirmButtonClicked(MouseEvent event) {

        var login = loginInput.getText();
        var password = passwordInput.getText();
        var email = emailInput.getText();
        var surname = surnameInput.getText();
        var name = nameInput.getText();
        var patronymic = patronymicInput.getText();
        var dateOfBirthLocal = dateOfBirthPicker.getValue();
        int experience = 0;

        try {
            experience = Integer.parseInt(experienceInput.getText());
        } catch (NumberFormatException e) {
            AlertManager.showWarningAlert("Неверный формат опыта", "");
            return;
        }

        if (!InputValidation.isLoginCorrect(login)) {
            AlertManager.showWarningAlert("Неверный формат логина", "");
            return;
        }

        if (!InputValidation.isPasswordCorrect(password)) {
            AlertManager.showWarningAlert("Неверный формат пароля", "");
            return;
        }

        if (!InputValidation.isEmailCorrect(email.toCharArray())) {
            AlertManager.showWarningAlert("Неверный формат пароля", "");
            return;
        }

        if (dateOfBirthLocal == null) {
            AlertManager.showWarningAlert("Выберите дату", "");
            return;
        }

        try {

            if (driver == null && access.checkIfLoginExistsAdmin(login)) {
                AlertManager.showWarningAlert("Логин существует", "");
                return;
            }
            var dateOfBirth = Date.from(Instant.from(dateOfBirthLocal.atStartOfDay(ZoneId.systemDefault())));
            try {

                if (driver == null) {

                    var response = access.createDriver(new Driver(name, surname, patronymic, dateOfBirth, experience,
                            new User(0, login, password, email, UserType.DRIVER)));
                    switch (response) {
                        case ERROR -> {
                            AlertManager.showWarningAlert("Ошибка", "Ошибка создания");
                        }
                    }
                } else {

                    driver.setName(name);
                    driver.setSurname(surname);
                    driver.setPatronymic(patronymic);
                    driver.setDateOfBirth(dateOfBirth);
                    driver.setExperience(experience);
                    driver.getUser().setLogin(login);
                    driver.getUser().setPassword(password);
                    driver.getUser().setEmail(email);
                    switch (access.editDriver(driver)) {
                        case ERROR -> {
                            AlertManager.showWarningAlert("Ошибка", "Ошибка редактирования");
                        }
                    }
                }

                Pair<DriversController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.driversView);
                pair.getKey().setAccess(access);
                pair.getKey().setOutputNode(output);
                output.getChildren().clear();
                output.getChildren().add(pair.getValue());
                pair.getKey().loadData();
            } catch (Exception e) {
                AlertManager.showErrorAlert("Ошибка подключения", "");
            }
        } catch (Exception e) {
            AlertManager.showWarningAlert("Ошибка", e.getMessage());
        }
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        titleLabel.setText("Редактирование машиниста");
        var date = driver.getDateOfBirth();
        dateOfBirthPicker.setValue(LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate()));
        experienceInput.setText(String.valueOf(driver.getExperience()));
        nameInput.setText(driver.getName());
        surnameInput.setText(driver.getSurname());
        patronymicInput.setText(driver.getPatronymic());
        passwordInput.setText(driver.getUser().getPassword());
        loginInput.setText(driver.getUser().getLogin());
        emailInput.setText(driver.getUser().getEmail());
    }

    public void setOutput(VBox output) {
        this.output = output;
    }
}
