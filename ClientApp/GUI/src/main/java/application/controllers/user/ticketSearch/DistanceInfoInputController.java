package application.controllers.user.ticketSearch;

import application.AlertManager;
import application.Application;
import application.AutoCompleteComboBoxListener;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.Station;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class DistanceInfoInputController {

    private UserAccess access;
    @FXML
    public Button findButton;

    @FXML
    private DatePicker dateInput;

    @FXML
    private ComboBox<Station> endStationInput;

    private AutoCompleteComboBoxListener<Station> endStationListener;

    @FXML
    private ComboBox<Station> startStationInput;
    private AutoCompleteComboBoxListener<Station> startStationListener;

    private VBox outputPane;

    private List<Station> stations;

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
    public void onFindButtonClicked(MouseEvent mouseEvent) {

        var startStation = startStationInput.getSelectionModel().getSelectedItem();
        var endStation = endStationInput.getSelectionModel().getSelectedItem();
        if (startStation == null){
            AlertManager.showWarningAlert("Введите станцию отправления", "");
            return;
        }
        if (endStation == null){
            AlertManager.showWarningAlert("Введите станцию прибытия", "");
            return;
        }
        LocalDate localDate = dateInput.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        try {

            Pair<TimeTableController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.timeTableView);
            pair.getKey().setData(startStation.getId(), endStation.getId(), date);
            pair.getKey().setAccess(access);
            pair.getKey().setOutputPane(outputPane);
            pair.getKey().setPrevMenu(new Pair<>(this, outputPane.getChildren().get(0)));
            outputPane.getChildren().clear();
            outputPane.getChildren().add(pair.getValue());
            pair.getKey().loadData();
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка получения данных", "");
        }
    }

    public void loadData(){

        StringConverter<Station> comboBoxConverter = new StringConverter<>() {
            @Override
            public String toString(Station station) {
                if (station == null) return "";
                return station.getName();
            }

            @Override
            public Station fromString(String s) {
                var matches =
                        stations.stream().filter((var station) -> station.getName().equals(s)).toList();
                if (matches.size() != 1) return null;
                return matches.get(0);
            }
        };
        endStationInput.setConverter(comboBoxConverter);
        startStationInput.setConverter(comboBoxConverter);
        try {
            stations = access.getAllStations();
            var list =  FXCollections.observableList(stations);
            startStationInput.setItems(list);
            endStationInput.setItems(list);
            endStationListener = new AutoCompleteComboBoxListener<>(endStationInput);
            startStationListener = new AutoCompleteComboBoxListener<>(startStationInput);
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка получения данных", "");
        }

    }

}
