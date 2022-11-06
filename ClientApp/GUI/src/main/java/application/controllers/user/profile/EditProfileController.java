package application.controllers.user.profile;

import application.AlertManager;
import application.InputValidation;
import clientConnectionModule.interfaces.UserAccess;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class EditProfileController {

    private UserAccess access;

    private Pair<UserProfileController, Node> previousMenu;

    private VBox outputPane;

    @FXML
    private TextField newEmailAddressInput;

    @FXML
    private PasswordField newPasswordInput;

    @FXML
    private PasswordField newPasswordRepeatInput;

    @FXML
    private PasswordField oldPasswordInput;

    @FXML
    void onBackLabelClicked(MouseEvent event) {

        outputPane.getChildren().clear();
        outputPane.getChildren().add(previousMenu.getValue());
    }

    @FXML
    void onConfirmButtonClicked(MouseEvent event) {

        var oldPassword = oldPasswordInput.getText();
        var newPassword = newPasswordInput.getText();
        var newPasswordRepeat = newPasswordRepeatInput.getText();
        var newEmail = newEmailAddressInput.getText();

        if (!InputValidation.isPasswordCorrect(newPassword)){
            AlertManager.showWarningAlert("Внимание", "Нарушены правила пароля");
            return;
        }
        if (!newPassword.equals(newPasswordRepeat)){
            AlertManager.showWarningAlert("Внимание", "Пароли не совпадают");
            return;
        }
        if (!InputValidation.isEmailCorrect(newEmail.toCharArray())){
            AlertManager.showWarningAlert("Внимание", "Нарушен формат электронного адреса");
            return;
        }
        //TODO: Проверка на пароль действующего пользователя
        AlertManager.showInformationAlert("Успешно!", "");
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setPreviousMenu(Pair<UserProfileController, Node> previousMenu) {
        this.previousMenu = previousMenu;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }

    @FXML
    public void onPasswordRulesLabelClicked(MouseEvent mouseEvent) {
        AlertManager.showInformationAlert("Правила", "Правила\nПравила\nПравила\nПравила");
    }
}