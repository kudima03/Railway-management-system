package application.controllers.driver.timeTable;

import application.DateConverter;
import customContainers.Pair;
import databaseEntities.Classes.Route;
import databaseEntities.Classes.RouteStation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class TimeTableItemController {

    @FXML
    private Label arrivalStationLabel;

    @FXML
    private Label arrivalTimeLabel;

    @FXML
    private Label departureStationLabel;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label routeNumberLabel;

    @FXML
    private Label trainTypeLabel;

    public void setData(Pair<Route, List<RouteStation>> pair){

        var route = pair.getFirstValue();
        var routeStations = pair.getSecondValue();

        routeNumberLabel.setText(String.valueOf(route.getNumber()));
        trainTypeLabel.setText(route.getTrain().getType().getName());
        if (routeStations.size() == 0){

            departureTimeLabel.setText("Не задано");
            arrivalTimeLabel.setText("Не задано");
            departureStationLabel.setText("Не задано");
            departureTimeLabel.setText("Не задано");
            return;
        }

        var firstRouteStation = routeStations.stream().min((var element1, var element2) ->
                element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get();
        var lastRouteStation = routeStations.stream().max((var element1, var element2) ->
                element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get();

        departureStationLabel.setText(firstRouteStation.getStation().getName());
        departureTimeLabel.setText(DateConverter.convert(firstRouteStation.getDepartureDateTime()));

        arrivalStationLabel.setText(lastRouteStation.getStation().getName());
        arrivalTimeLabel.setText(DateConverter.convert(lastRouteStation.getDepartureDateTime()));
    }

}
