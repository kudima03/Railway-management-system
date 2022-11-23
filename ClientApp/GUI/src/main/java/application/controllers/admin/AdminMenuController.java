package application.controllers.admin;

import application.AlertManager;
import application.Application;
import application.controllers.admin.driversOperationsControllers.DriversController;
import application.controllers.admin.routesOperationsControllers.RoutesController;
import application.controllers.admin.stationsOperationsControllers.StationsController;
import application.controllers.admin.statisticsOperationsControllers.StatisticsController;
import application.controllers.admin.trainsOperationsControllers.TrainsController;
import application.controllers.common.AuthorizationController;
import clientConnectionModule.interfaces.AdminAccess;
import clientConnectionModule.interfaces.SingUpAccess;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class AdminMenuController {

    private AdminAccess access;

    @FXML
    private VBox output;
    @FXML
    void onDriversOperationsClicked(MouseEvent event) {

        Pair<DriversController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.driversView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputNode(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onReportsClicked(MouseEvent event) {

        Pair<StatisticsController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.statisticsView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputNode(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
    }

    @FXML
    void onRoutesOperationsClicked(MouseEvent event) {

        Pair<RoutesController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.routesView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onStationsOperationsClicked(MouseEvent event) {

        Pair<StationsController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.stationsView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onTrainsOperationsClicked(MouseEvent event) {

        Pair<TrainsController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.trainsView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputNode(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void onExitLabelClicked(MouseEvent mouseEvent) {
        try {
            access.exit();
            Pair<AuthorizationController, Scene> pair = Application.viewLoader.getScene(Application.viewLoader.authorizationView, 480, 600);
            pair.getKey().setAccess((SingUpAccess) access);
            Application.viewLoader.setSceneToStage(pair.getValue(), "Авторизация");
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }
}
