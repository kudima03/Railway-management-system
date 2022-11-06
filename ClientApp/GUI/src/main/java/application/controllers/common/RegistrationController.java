package application.controllers.common;

import application.AlertManager;
import application.Application;
import application.InputValidation;
import application.controllers.user.UserMenuController;
import clientConnectionModule.interfaces.RegistrationAccess;
import clientConnectionModule.interfaces.SingUpAccess;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Enums.UserType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class RegistrationController {

    @FXML
    public PasswordField passwordRepeatInput;
    @FXML
    public TextField loginInput;
    @FXML
    public PasswordField passwordInput;
    public Label inputInfoLabel;
    @FXML
    private RegistrationAccess access;
    @FXML
    private TextField emailInput;
    @FXML
    private Button registerButton;

    @FXML
    void onBackLabelClicked(MouseEvent event) {

        Pair<AuthorizationController, Scene> pair = Application.viewLoader.getScene(
                Application.viewLoader.authorizationView,
                480,
                600);
        pair.getKey().setAccess((SingUpAccess)access);
        Application.viewLoader.setSceneToStage(pair.getValue(), "Авторизация");
    }

    @FXML
    void onRegisterButtonClicked(MouseEvent event) {

        var login = loginInput.getText();
        var password = passwordInput.getText();
        var passwordRepeat = passwordRepeatInput.getText();
        var email = emailInput.getText();

        if (!password.equals(passwordRepeat)) {
            AlertManager.showWarningAlert("Внимание", "Пароли не совпадают.");
            return;
        }

        if (!InputValidation.isLoginCorrect(login) ||
                !InputValidation.isPasswordCorrect(password) ||
                !InputValidation.isEmailCorrect(email.toCharArray())){

            AlertManager.showWarningAlert("Внимание", "Нарушены правила ввода.");
            return;
        }

        try {

           switch (access.registration(login, password, email, UserType.USER)) {

               case ERROR -> {
                   AlertManager.showErrorAlert("Ошибка", "Ошибка регистрации");
               }
               case SUCCESSFULLY -> {
                   Pair<UserMenuController, Scene> pair = Application.viewLoader.getScene(
                           Application.viewLoader.userView,
                           1200,
                           720);
                   pair.getKey().setAccess((UserAccess) access);
                   Application.viewLoader.setSceneToStage(pair.getValue(), "Меню пользователя");
               }
           }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка", "Ошибка получения данных");
        }
    }

    public void setAccess(RegistrationAccess access) {
        this.access = access;
    }

    public void onInputInfoLabelClicked(MouseEvent mouseEvent) {

        AlertManager.showInformationAlert("Правила ввода", "Правила");
    }

    public void onLoginInputKeyTyped(KeyEvent keyEvent) {

        if (!InputValidation.isLoginCorrect(loginInput.getText())) return;
        try {
           if(access.checkIfLoginExists(loginInput.getText())){
               //TODO: Do smth when login exists
           } else {
               //TODO: Do smth when login not exists
           }
        } catch (Exception e) {
            //Exception ignore
        }
    }
}
