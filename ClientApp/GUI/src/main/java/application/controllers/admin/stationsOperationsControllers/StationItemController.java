package application.controllers.admin.stationsOperationsControllers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.AdminAccess;
import databaseEntities.Classes.Station;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class StationItemController {

    private AdminAccess access;

    private Station station;

    private VBox output;

    private Pair<StationsController, Node> parentMenu;

    @FXML
    private Label stationAddressLabel;

    @FXML
    private Label stationNameLabel;

    @FXML
    void onDeleteClicked(MouseEvent event) {

        try {

            switch (access.deleteStation(station.getId())){

                case ERROR -> {
                    AlertManager.showWarningAlert("Ошибка удаления",
                            "Невозможно удалить станцию, т.к она входит в маршрут движения");
                }
            }
            parentMenu.getKey().loadData();

        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }

    @FXML
    void onEditClicked(MouseEvent event) {

        Pair<StationsAddOrEditController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditStationView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
        pair.getKey().setStation(station);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setStation(Station station) {
        this.station = station;
        stationAddressLabel.setText(station.getAddress());
        stationNameLabel.setText(station.getName());
    }

    public void setOutput(VBox output) {
        this.output = output;
    }

    public void setParentMenu(Pair<StationsController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }
}
