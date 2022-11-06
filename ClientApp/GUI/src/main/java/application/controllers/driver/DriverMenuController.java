package application.controllers.driver;

import application.AlertManager;
import application.Application;
import application.controllers.common.AuthorizationController;
import application.controllers.driver.profile.ProfileController;
import application.controllers.driver.timeTable.TimeTableController;
import clientConnectionModule.interfaces.DriverAccess;
import clientConnectionModule.interfaces.SingUpAccess;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class DriverMenuController {

    private DriverAccess access;

    @FXML
    private VBox outputVBox;

    @FXML
    void onExitClick(MouseEvent event) {
        try {
            access.driverExit();
            Pair<AuthorizationController, Scene> pair = Application.viewLoader.getScene(Application.viewLoader.authorizationView, 480, 600);
            pair.getKey().setAccess((SingUpAccess) access);
            Application.viewLoader.setSceneToStage(pair.getValue(), "Авторизация");
        } catch (Exception e) {
            AlertManager.showErrorAlert("ConnectionError", "");
        }
    }

    @FXML
    void onMyProfileClick(MouseEvent event) {

        Pair<ProfileController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.profileView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(outputVBox);
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onMyTimeTableClick(MouseEvent event) {

        Pair<TimeTableController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.driverTimeTableView);
        pair.getKey().setAccess(access);
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setAccess(DriverAccess access) {
        this.access = access;
    }
}
