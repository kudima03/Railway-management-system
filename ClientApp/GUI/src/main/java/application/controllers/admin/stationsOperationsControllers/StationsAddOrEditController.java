package application.controllers.admin.stationsOperationsControllers;

import application.AlertManager;
import application.Application;
import application.InputValidation;
import clientConnectionModule.interfaces.AdminAccess;
import databaseEntities.Classes.Station;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class StationsAddOrEditController {

    private AdminAccess access;

    private VBox output;

    private Station station;

    @FXML
    private TextField kmFromMinskInput;

    @FXML
    private TextField stationAddressInput;

    @FXML
    private TextField stationNameInput;

    @FXML
    private Label title;

    @FXML
    void onConfirmButtonClick(MouseEvent event) {

        var name = stationNameInput.getText();
        var address = stationAddressInput.getText();
        int kmFromMinsk = 0;
        try {
            kmFromMinsk = Integer.parseInt(kmFromMinskInput.getText());
        } catch (NumberFormatException e) {
            AlertManager.showWarningAlert("Неверный формат кол-ва километров", "");
            return;
        }

        if (!InputValidation.isStringContainsOnlyLetters(name)) {
            AlertManager.showWarningAlert("Неверный формат названия", "");
            return;
        }
        try {
            if (station == null) {
                access.createStation(new Station(name, address, kmFromMinsk));
            } else {
                station.setName(name);
                station.setAddress(address);
                station.setKmFromMinsk(kmFromMinsk);
                access.editStation(station);
            }
            Pair<StationsController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.stationsView);
            pair.getKey().setAccess(access);
            pair.getKey().setOutput(output);
            output.getChildren().clear();
            output.getChildren().add(pair.getValue());
            pair.getKey().loadData();
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutput(VBox output) {
        this.output = output;
    }

    public void setStation(Station station) {
        this.station = station;
        title.setText("Редактирование станции");
        stationNameInput.setText(station.getName());
        stationAddressInput.setText(station.getAddress());
        kmFromMinskInput.setText(String.valueOf(station.getKmFromMinsk()));
    }
}
