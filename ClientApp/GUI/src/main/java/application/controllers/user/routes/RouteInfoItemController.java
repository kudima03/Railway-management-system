package application.controllers.user.routes;

import application.DateConverter;
import databaseEntities.Classes.RouteStation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Date;

public class RouteInfoItemController {

    @FXML
    private Label arrivalTimeLabel;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label routeTimeLabel;

    @FXML
    private Label stationNameLabel;

    @FXML
    private Label stopTimeLabel;

    public void setData(RouteStation routeStation, Date routeStartTime) {
        var arrivalTime = DateConverter.convert(routeStation.getArrivalDateTime());
        if (arrivalTime.equals("0:00")){
            arrivalTime = "-";
        }
        var depTime = DateConverter.convert(routeStation.getDepartureDateTime());
        if (depTime.equals("0:00")){
            depTime = "-";
        }
        var stopTime = DateConverter.convert(routeStation.getStopTime());
        if (stopTime.equals("0:00")){
            stopTime = "-";
        }
        arrivalTimeLabel.setText(arrivalTime);
        departureTimeLabel.setText(depTime);
        routeTimeLabel.setText(DateConverter.convertDifference(routeStartTime, routeStation.getArrivalDateTime()));
        stationNameLabel.setText(routeStation.getStation().getName());
        stopTimeLabel.setText(stopTime);
    }

}
