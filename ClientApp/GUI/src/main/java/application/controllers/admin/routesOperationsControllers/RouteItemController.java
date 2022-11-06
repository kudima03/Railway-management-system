package application.controllers.admin.routesOperationsControllers;

import application.Application;
import application.DateConverter;
import clientConnectionModule.interfaces.AdminAccess;
import databaseEntities.Classes.Route;
import databaseEntities.Classes.RouteStation;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.List;

public class RouteItemController {

    private AdminAccess access;

    private VBox output;

    private Pair<RoutesController, Node> parentMenu;

    private customContainers.Pair<Route, List<RouteStation>> routeInfo;

    @FXML
    private Label arrivalStationNameLabel;

    @FXML
    private Label arrivalTimeLabel;

    @FXML
    private Label departureStationNameLabel;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label driverDateOfBirthLabel;

    @FXML
    private Label driverExperienceLabel;

    @FXML
    private Label driverFullNameLabel;

    @FXML
    private Label routeNumberLabel;

    @FXML
    private Label trainNumberLabel;

    @FXML
    private Label trainTypeLabel;

    @FXML
    void onItemClicked(MouseEvent event) {

        javafx.util.Pair<RoutesAddOrEditController, ScrollPane> pair =
                Application.viewLoader.getItem(Application.viewLoader.routesAddOrEditView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
        pair.getKey().setRouteInfo(routeInfo);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutput(VBox output) {
        this.output = output;
    }

    public void setParentMenu(Pair<RoutesController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setRouteInfo(customContainers.Pair<Route, List<RouteStation>> routeInfo) {

        this.routeInfo = routeInfo;
        var route = routeInfo.getFirstValue();
        var routeStations = routeInfo.getSecondValue();

        var driver = route.getDriver();
        driverFullNameLabel.setText(driver.getSurname() + " " + driver.getName() + " " + driver.getPatronymic());
        var dateOfBirth = driver.getDateOfBirth();
        driverDateOfBirthLabel.setText(dateOfBirth.getDate() + "." + (dateOfBirth.getMonth() + 1)
                + "." + (dateOfBirth.getYear() + 1900));
        driverExperienceLabel.setText(driver.getExperience() + " лет.");

        routeNumberLabel.setText(String.valueOf(route.getNumber()));
        trainNumberLabel.setText(String.valueOf(route.getTrain().getNumber()));
        trainTypeLabel.setText(route.getTrain().getType().getName());

        if (routeStations.size() == 0) {

            departureTimeLabel.setText("00:00");
            arrivalTimeLabel.setText("00:00");

            departureStationNameLabel.setText("Название не задано");
            arrivalStationNameLabel.setText("Название не задано");
            return;
        };
        var firstRouteStation = routeStations.stream().min((var element1, var element2) ->
                element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get();
        var lastRouteStation = routeStations.stream().max((var element1, var element2) ->
                element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get();
        departureTimeLabel.setText(DateConverter.convert(firstRouteStation.getDepartureDateTime()));
        arrivalTimeLabel.setText(DateConverter.convert(lastRouteStation.getArrivalDateTime()));

        departureStationNameLabel.setText(firstRouteStation.getStation().getName());
        arrivalStationNameLabel.setText(firstRouteStation.getStation().getName());
    }
}
