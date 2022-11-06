package application.controllers.driver.profile;

import application.AlertManager;
import application.Application;
import application.InputValidation;
import clientConnectionModule.interfaces.DriverAccess;
import databaseEntities.Classes.Driver;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class ChangePasswordController {

    private DriverAccess access;

    private Driver profile;

    private VBox output;
    @FXML
    private PasswordField newPasswordInput;

    @FXML
    private PasswordField passwordRepeatInput;

    @FXML
    void onSubmitButtonClick(MouseEvent event) {

        var password = newPasswordInput.getText();
        var repeat = passwordRepeatInput.getText();
        if (!InputValidation.isPasswordCorrect(password)){
            AlertManager.showWarningAlert("Ошибка", "Недопустимый пароль");
            return;
        }
        if (!password.equals(repeat)){
            AlertManager.showWarningAlert("Ошибка", "Пароли должны совпадать!");
            return;
        }
        profile.getUser().setPassword(password);
        try {
            access.updateProfile(profile);
        } catch (Exception e) {
            AlertManager.showWarningAlert("Ошибка соединения", "");
        }
        Pair<ProfileController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.profileView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setAccess(DriverAccess access) {
        this.access = access;
    }

    public void setProfile(Driver profile) {
        this.profile = profile;
    }

    public void setOutput(VBox output) {
        this.output = output;
    }
}
