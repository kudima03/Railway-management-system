package application.controllers.common;

import application.AlertManager;
import application.Application;
import application.controllers.admin.AdminMenuController;
import application.controllers.driver.DriverMenuController;
import application.controllers.user.UserMenuController;
import clientConnectionModule.interfaces.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class AuthorizationController {

    private SingUpAccess access;

    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Label registerLabel;

    @FXML
    private Button singUpButton;

    @FXML
    void onRegisterLabelClicked(MouseEvent event) {

        Pair<RegistrationController, Scene> pair = Application.viewLoader.getScene(
                Application.viewLoader.registrationView,
                400,
                500);
        pair.getKey().setAccess((RegistrationAccess) access);
        Application.viewLoader.setSceneToStage(pair.getValue(), "Регистрация");
    }

    @FXML
    void onSingUpButtonClicked(MouseEvent event) {

        var login = loginInput.getText();
        var password = passwordInput.getText();
        if (login.isBlank() || password.isBlank()) {
            AlertManager.showWarningAlert("Внимание", "Все поля должны быть заполнены.");
            return;
        }
        try {

            switch (access.singUp(login, password)) {

                case UNDEFINED -> {
                    AlertManager.showWarningAlert("Внимание", "Неправильный логин или пароль.");
                }
                case ADMIN -> {
                    Pair<AdminMenuController, Scene> pair = Application.viewLoader.getScene(
                            Application.viewLoader.adminView,
                            755,
                            600);
                    pair.getKey().setAccess((AdminAccess) access);
                    Application.viewLoader.setSceneToStage(pair.getValue(), "Меню администратора");
                }
                case USER -> {
                    Pair<UserMenuController, Scene> pair = Application.viewLoader.getScene(
                            Application.viewLoader.userView,
                            920,
                            720);
                    pair.getKey().setAccess((UserAccess) access);
                    Application.viewLoader.setSceneToStage(pair.getValue(), "Меню пользователя");
                }
                case DRIVER -> {
                    Pair<DriverMenuController, Scene> pair = Application.viewLoader.getScene(
                            Application.viewLoader.driverMenuView,
                            678,
                            455);
                    pair.getKey().setAccess((DriverAccess) access);
                    Application.viewLoader.setSceneToStage(pair.getValue(), "Меню машиниста");
                }
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка", e.getMessage());
        }
    }

    public void setAccess(SingUpAccess singUpAccess) {
        this.access = singUpAccess;
    }
}
