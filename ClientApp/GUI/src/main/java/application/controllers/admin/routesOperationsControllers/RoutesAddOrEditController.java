package application.controllers.admin.routesOperationsControllers;

import application.AlertManager;
import application.Application;
import application.AutoCompleteComboBoxListener;
import application.DateConverter;
import clientConnectionModule.interfaces.AdminAccess;
import customContainers.Pair;
import databaseEntities.Classes.*;
import databaseEntities.Enums.Periodicity;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoutesAddOrEditController {


    private AdminAccess access;

    private VBox output;

    private Pair<Route, List<RouteStation>> routeInfo;

    @FXML
    private Button addButton;

    @FXML
    private TextField arrivalTimeInput;

    @FXML
    private Button confirmButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField departureTimeInput;

    @FXML
    private ComboBox<Driver> driverComboBox;

    @FXML
    private ComboBox<String> periodicityComboBox;

    @FXML
    private TextField platformNumberInput;

    @FXML
    private TextField routeNumberInput;

    @FXML
    private VBox routeStationsVBox;

    @FXML
    private ComboBox<Station> stationComboBox;

    @FXML
    private TextField trackNumberInput;

    @FXML
    private ComboBox<Train> trainComboBox;

    @FXML
    public Button createRouteButton;

    @FXML
    public AnchorPane stationsBindingAnchorPane;

    private AutoCompleteComboBoxListener<Driver> driverCmbListener;
    private AutoCompleteComboBoxListener<Station> stationCmbListener;
    private AutoCompleteComboBoxListener<Train> trainsCmbListener;

    private List<Driver> drivers;

    private List<Station> stations;

    private List<Train> trains;

    private RouteStation selectedRouteStation;

    public void initialize() {

        Route route = new Route();
        route.setDriver(null);
        route.setTrain(null);
        route.setNumber(0);
        routeInfo = new Pair<>(route, new ArrayList<>());
        stationsBindingAnchorPane.setDisable(true);

        var driversCmdConverter = new StringConverter<Driver>() {
            @Override
            public String toString(Driver driver) {
                if (driver == null) return "";
                return driver.getSurname() + " " + driver.getName() + " " + driver.getPatronymic();
            }

            @Override
            public Driver fromString(String s) {
                var fullName = s.split(" ");
                if (fullName.length != 3) return null;
                var matches =
                        drivers.stream().filter(
                                (var driver) ->
                                        driver.getSurname().equals(fullName[0]) &&
                                                driver.getName().equals(fullName[1]) &&
                                                driver.getPatronymic().equals(fullName[2])
                        ).toList();
                if (matches.size() != 1) return null;
                return matches.get(0);
            }
        };
        var stationsCmdConverter = new StringConverter<Station>() {
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
        var trainsCmdConverter = new StringConverter<Train>() {
            @Override
            public String toString(Train train) {
                if (train == null) return "";
                return train.getNumber() + " " + train.getType().getName();
            }

            @Override
            public Train fromString(String s) {
                var values = s.split(" ");
                var matches =
                        trains.stream().filter(
                                (var train) ->
                                        String.valueOf(train.getNumber()).equals(values[0])
                        ).toList();
                if (matches.size() != 1) return null;
                return matches.get(0);
            }
        };

        driverComboBox.setConverter(driversCmdConverter);
        stationComboBox.setConverter(stationsCmdConverter);
        trainComboBox.setConverter(trainsCmdConverter);
    }

    @FXML
    void onAddButtonClick(MouseEvent event) {

        var station = stationComboBox.getSelectionModel().getSelectedItem();
        var arrivalTime = new Pair<Integer, Integer>();
        var departureTime = new Pair<Integer, Integer>();
        int platform = 0;
        try {
            arrivalTime = DateConverter.stringToHHMM(arrivalTimeInput.getText(), ":");
            departureTime = DateConverter.stringToHHMM(departureTimeInput.getText(), ":");
            platform = Integer.parseInt(platformNumberInput.getText());
        } catch (Exception e) {
            AlertManager.showWarningAlert("Неверный ввод времени отправления или прибытия", "");
            return;
        }
        var track = trackNumberInput.getText();


        boolean isFirstRouteStation = routeInfo.getSecondValue().size() == 0;
        if (isFirstRouteStation) {

            var newRouteStation = new RouteStation();
            newRouteStation.setRoute(routeInfo.getFirstValue());
            newRouteStation.setStation(station);
            newRouteStation.setOrdinalNumber(1);
            var departureDateTime = new Date();
            departureDateTime.setHours(departureTime.getFirstValue());
            departureDateTime.setMinutes(departureTime.getSecondValue());
            newRouteStation.setDepartureDateTime(departureDateTime);
            var arrivalDateTime = new Date();
            arrivalDateTime.setHours(arrivalTime.getFirstValue());
            arrivalDateTime.setMinutes(arrivalTime.getSecondValue());
            newRouteStation.setArrivalDateTime(arrivalDateTime);
            newRouteStation.setTrackNumber(track);
            newRouteStation.setPlatformNumber(platform);
            routeInfo.getSecondValue().add(newRouteStation);
            try {
                switch (access.addRouteStationToRoute(newRouteStation)){
                    case ERROR -> {
                        AlertManager.showErrorAlert("Ошибка", "Ошибка добавления станции");
                        return;
                    }
                }
            } catch (Exception e) {
                AlertManager.showErrorAlert("Ошибка", e.getMessage());
                return;
            }
        } else {

            var lastRouteStation = routeInfo.getSecondValue().stream().max((var element1, var element2) ->
                    element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get();
            var newRouteStation = new RouteStation();
            newRouteStation.setRoute(routeInfo.getFirstValue());
            newRouteStation.setStation(station);
            newRouteStation.setOrdinalNumber(lastRouteStation.getOrdinalNumber() + 1);
            var departureDateTime = new Date(lastRouteStation.getDepartureDateTime().getTime());
            departureDateTime.setHours(departureTime.getFirstValue());
            departureDateTime.setMinutes(departureTime.getSecondValue());
            newRouteStation.setDepartureDateTime(departureDateTime);
            var arrivalDateTime = new Date(lastRouteStation.getArrivalDateTime().getTime());
            arrivalDateTime.setHours(arrivalTime.getFirstValue());
            arrivalDateTime.setMinutes(arrivalTime.getSecondValue());
            newRouteStation.setArrivalDateTime(arrivalDateTime);
            newRouteStation.setStopTime(new Date(departureDateTime.getTime() - arrivalDateTime.getTime()));
            newRouteStation.setTrackNumber(track);
            newRouteStation.setPlatformNumber(platform);
            routeInfo.getSecondValue().add(newRouteStation);
            try {
                switch (access.addRouteStationToRoute(newRouteStation)){
                    case ERROR -> {
                        AlertManager.showErrorAlert("Ошибка", "Ошибка добавления станции");
                        return;
                    }
                }
            } catch (Exception e) {
                AlertManager.showErrorAlert("Ошибка", e.getMessage());
            }
        }
        refreshRouteStationsVBox();
    }

    @FXML
    void onConfirmButtonClick(MouseEvent event) {

        if (selectedRouteStation == null) return;
        var station = stationComboBox.getSelectionModel().getSelectedItem();
        var arrivalTime = new Pair<Integer, Integer>();
        var departureTime = new Pair<Integer, Integer>();
        int platform = 0;
        try {
            arrivalTime = DateConverter.stringToHHMM(arrivalTimeInput.getText(), ":");
            departureTime = DateConverter.stringToHHMM(departureTimeInput.getText(), ":");
            platform = Integer.parseInt(platformNumberInput.getText());
        } catch (Exception e) {
            AlertManager.showWarningAlert("Неверный ввод времени отправления или прибытия", "");
            return;
        }
        var track = trackNumberInput.getText();

        int routeId = selectedRouteStation.getRoute().getId();
        int stationId = selectedRouteStation.getStation().getId();
        selectedRouteStation.setRoute(routeInfo.getFirstValue());
        selectedRouteStation.setStation(station);
        selectedRouteStation.setOrdinalNumber(1);
        var departureDateTime = new Date(selectedRouteStation.getDepartureDateTime().getTime());
        departureDateTime.setHours(departureTime.getFirstValue());
        departureDateTime.setMinutes(departureTime.getSecondValue());
        selectedRouteStation.setDepartureDateTime(departureDateTime);
        var arrivalDateTime = new Date(selectedRouteStation.getArrivalDateTime().getTime());
        arrivalDateTime.setHours(arrivalTime.getFirstValue());
        arrivalDateTime.setMinutes(arrivalTime.getSecondValue());
        selectedRouteStation.setArrivalDateTime(arrivalDateTime);
        selectedRouteStation.setTrackNumber(track);
        selectedRouteStation.setPlatformNumber(platform);
        try {
            switch (access.editRouteStation(routeId, stationId, selectedRouteStation)){
                case ERROR -> {
                    AlertManager.showWarningAlert("Ошибка", "Ошибка редактирования");
                    return;
                }
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка", e.getMessage());
        }
        refreshRouteStationsVBox();
        selectedRouteStation = null;
    }

    @FXML
    void onDeleteButtonClick(MouseEvent event) {

        try {
            switch (access.deleteRouteStationFromRoute(selectedRouteStation)){
                case ERROR -> {
                    AlertManager.showWarningAlert("Ошибка", "Ошибка удаления");
                    return;
                }
            }
            routeInfo.getSecondValue().remove(selectedRouteStation);
            refreshRouteStationsVBox();
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", e.getMessage());
        }
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutput(VBox output) {
        this.output = output;
    }

    public void setRouteInfo(Pair<Route, List<RouteStation>> routeInfo) {
        this.routeInfo = routeInfo;
        var route = routeInfo.getFirstValue();
        driverComboBox.getSelectionModel().select(route.getDriver());
        trainComboBox.getSelectionModel().select(route.getTrain());
        routeNumberInput.setText(String.valueOf(route.getNumber()));
        periodicityComboBox.getSelectionModel().select(route.getPeriodicity().ordinal() + 1);
        createRouteButton.setDisable(true);
        stationsBindingAnchorPane.setDisable(false);
        refreshRouteStationsVBox();
    }

    private void refreshRouteStationsVBox() {

        routeStationsVBox.getChildren().clear();
        var routeStations = routeInfo.getSecondValue();
        for (var routeStation : routeStations) {
            javafx.util.Pair<RouteStationItemController, HBox> pair =
                    Application.viewLoader.getItem(Application.viewLoader.routeStationItemView);
            pair.getKey().setRouteStation(routeStation);
            pair.getKey().setParentMenu(new javafx.util.Pair<>(this, output.getChildren().get(0)));
            routeStationsVBox.getChildren().add(pair.getValue());
        }
    }

    public void loadData() {

        try {
            stations = access.getAllStationsAdmin();
            drivers = access.getAllDrivers();
            trains = access.getAllTrains();
            driverComboBox.setItems(FXCollections.observableList(drivers));
            stationComboBox.setItems(FXCollections.observableList(stations));
            trainComboBox.setItems(FXCollections.observableList(trains));
            driverCmbListener = new AutoCompleteComboBoxListener<>(driverComboBox);
            stationCmbListener = new AutoCompleteComboBoxListener<>(stationComboBox);
            trainsCmbListener = new AutoCompleteComboBoxListener<>(trainComboBox);
            var values = new ArrayList<String>();
            values.add("Ежедневный");
            values.add("Еженедельный");
            values.add("Ежемесячный");
            periodicityComboBox.setItems(FXCollections.observableList(values));
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }

    public void setSelectedRouteStation(RouteStation selectedRouteStation) {
        this.selectedRouteStation = selectedRouteStation;
        if (selectedRouteStation == null) {

            stationComboBox.getSelectionModel().select(0);
            arrivalTimeInput.setText("");
            departureTimeInput.setText("");
            trackNumberInput.setText("");
            platformNumberInput.setText("");
            addButton.setDisable(false);
            confirmButton.setDisable(true);
            deleteButton.setDisable(true);
        } else {
            stationComboBox.getSelectionModel().select(selectedRouteStation.getStation());
            arrivalTimeInput.setText(DateConverter.convert(selectedRouteStation.getArrivalDateTime()));
            departureTimeInput.setText(DateConverter.convert(selectedRouteStation.getDepartureDateTime()));
            trackNumberInput.setText(selectedRouteStation.getTrackNumber());
            platformNumberInput.setText(String.valueOf(selectedRouteStation.getPlatformNumber()));
            addButton.setDisable(true);
            confirmButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    public RouteStation getSelectedRouteStation() {
        return selectedRouteStation;
    }

    public void onCreateRouteButtonClick(MouseEvent mouseEvent) {

        int routeNumber = 0;
        var driver = driverComboBox.getSelectionModel().getSelectedItem();
        var train = trainComboBox.getSelectionModel().getSelectedItem();
        int index = periodicityComboBox.getSelectionModel().getSelectedIndex();
        Periodicity periodicity;
        switch (index) {
            case 0 -> {
                periodicity = Periodicity.Daily;
            }
            case 1 -> {
                periodicity = Periodicity.Weekly;
            }
            case 2 -> {
                periodicity = Periodicity.Monthly;
            }
            default -> {
                AlertManager.showWarningAlert("Ошибка выбора периодичности", "");
                return;
            }
        }

        try {
            routeNumber = Integer.parseInt(routeNumberInput.getText());
        } catch (NumberFormatException e) {
            AlertManager.showWarningAlert("Неверный формат номера маршрута", "");
            return;
        }
        if (driver == null) {
            AlertManager.showWarningAlert("Выберите машиниста", "");
            return;
        }
        if (train == null) {
            AlertManager.showWarningAlert("Выберите поезд", "");
            return;
        }
        routeInfo.setFirstValue(new Route(0, train, driver, routeNumber, periodicity));
        try {
            access.createRoute(routeInfo.getFirstValue());
            var createdRoute = access.getAllRoutes().stream().filter(
                    (var route)->route.getNumber() == routeInfo.getFirstValue().getNumber())
                    .toList().get(0);
            routeInfo.setFirstValue(createdRoute);

        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", e.getMessage());
            return;
        }
        stationsBindingAnchorPane.setDisable(false);
        createRouteButton.setDisable(true);
    }
}