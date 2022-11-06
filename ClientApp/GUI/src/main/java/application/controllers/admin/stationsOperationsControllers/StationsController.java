package application.controllers.admin.stationsOperationsControllers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.AdminAccess;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class StationsController {

    private AdminAccess access;

    private VBox output;

    @FXML
    private VBox stationsVBox;

    public void loadData() {

        try {
            stationsVBox.getChildren().clear();
            var stations = access.getAllStationsAdmin();
            for (var station : stations) {

                Pair<StationItemController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.stationItemView);
                pair.getKey().setStation(station);
                pair.getKey().setAccess(access);
                pair.getKey().setOutput(output);
                pair.getKey().setParentMenu(new Pair<>(this, output.getChildren().get(0)));
                stationsVBox.getChildren().add(pair.getValue());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutput(VBox output) {
        this.output = output;
    }

    public void onAddStationClick(MouseEvent mouseEvent) {

        Pair<StationsAddOrEditController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditStationView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
    }
}
