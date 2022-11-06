package application.controllers.driver.profile;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.DriverAccess;
import databaseEntities.Classes.Driver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class ProfileController {

    private DriverAccess access;

    private VBox output;

    private Driver profile;

    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label experienceLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label loginLabel;

    @FXML
    void onChangePasswordClicked(MouseEvent event) {

        Pair<ChangePasswordController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.changePasswordView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
        pair.getKey().setProfile(profile);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
    }

    public void loadData(){

        try {
            profile = access.getProfile();
            fullNameLabel.setText(profile.getSurname() + " " + profile.getName() + " " + profile.getPatronymic());
            experienceLabel.setText(profile.getExperience() + " лет");
            loginLabel.setText(profile.getUser().getLogin());
            dateOfBirthLabel.setText(profile.getDateOfBirth().getDate() + "" + profile.getDateOfBirth().getMonth()
                    + " " + profile.getDateOfBirth().getYear());
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка", e.getMessage());
        }
    }

    public void setAccess(DriverAccess access) {
        this.access = access;
    }

    public void setOutput(VBox output) {
        this.output = output;
    }
}

