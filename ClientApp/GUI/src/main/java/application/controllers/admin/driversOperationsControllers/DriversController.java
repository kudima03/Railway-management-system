package application.controllers.admin.driversOperationsControllers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.AdminAccess;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class DriversController {

    private AdminAccess access;

    private VBox outputNode;

    @FXML
    private VBox driversVBox;

    @FXML
    void onAddClicked(MouseEvent event) {

        Pair<AddOrEditDriverController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditDriverView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(outputNode);
        outputNode.getChildren().clear();
        outputNode.getChildren().add(pair.getValue());
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutputNode(VBox outputNode) {
        this.outputNode = outputNode;
    }

    public void loadData() {

        driversVBox.getChildren().clear();
        try {
            var drivers = access.getAllDrivers();
            for (var driver : drivers) {
                Pair<DriverItemController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.driverItemView);
                pair.getKey().setDriver(driver);
                pair.getKey().setAccess(access);
                pair.getKey().setOutput(outputNode);
                pair.getKey().setParentMenu(new Pair<>(this, outputNode.getChildren().get(0)));
                driversVBox.getChildren().add(pair.getValue());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }
}
