package application.controllers.user.ticketSearch;

import application.Application;
import application.DateConverter;
import application.controllers.user.routes.RouteInfoController;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.RouteStation;
import databaseEntities.Classes.Station;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.List;

public class TimeTableItemController {

    private UserAccess userAccess;

    private VBox outputPane;

    @FXML
    public Label departureTimeLabel;
    @FXML
    public Label departureStationNameLabel;
    @FXML
    public Label travelTimeLabel;
    @FXML
    public Label arrivalTimeLabel;
    @FXML
    public Label arrivalStationNameLabel;
    @FXML
    public Label routeNumberLabel;
    @FXML
    public Label routeStartStationLabel;
    @FXML
    public Label routeEndStationLabel;
    @FXML
    public Label trainTypeLabel;
    @FXML
    public Label buyTicketLabel;
    @FXML
    public Label routeLabel;

    private RouteStation departureStation;
    private RouteStation arrivalStation;
    private Station routeStartStation;
    private Station routeLastStation;
    private float distanceCost;
    private List<RouteStation> allRouteStations;

    private Pair<TimeTableController, Node> parentMenu;

    public void setOutputPane(VBox parentAnchorPane) {
        this.outputPane = parentAnchorPane;
    }

    public void setAccess(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    public void setParentMenu(Pair<TimeTableController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setData(RouteStation departureStation,
                        RouteStation arrivalStation,
                        Station routeStartStation,
                        Station routeLastStation,
                        List<RouteStation> routeStationsList,
                        float distanceCost) {

        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.allRouteStations = routeStationsList;
        this.distanceCost = distanceCost;
        departureTimeLabel.setText(DateConverter.convert(departureStation.getDepartureDateTime()));
        departureStationNameLabel.setText(departureStation.getStation().getName());
        travelTimeLabel.setText(DateConverter.convertDifference(arrivalStation.getArrivalDateTime(), departureStation.getDepartureDateTime()));
        arrivalTimeLabel.setText(DateConverter.convert(arrivalStation.getArrivalDateTime()));
        arrivalStationNameLabel.setText(arrivalStation.getStation().getName());
        routeNumberLabel.setText(String.valueOf(departureStation.getRoute().getNumber()));
        routeStartStationLabel.setText(routeStartStation.getName());
        routeEndStationLabel.setText(routeLastStation.getName());
        trainTypeLabel.setText(departureStation.getRoute().getTrain().getType().getName());
        buyTicketLabel.setText(String.valueOf(distanceCost));
    }

    public void onBuyTicketLabelClicked(MouseEvent mouseEvent) {

        Pair<BuyTicketPreviewController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.buyTicketPreviewView);
        pair.getKey().setAccess(userAccess);
        pair.getKey().setOutputPane(outputPane);
        pair.getKey().setPrevMenu(parentMenu);
        pair.getKey().setData(departureStation, arrivalStation, allRouteStations, distanceCost);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
    }

    public void onRouteLabelClicked(MouseEvent mouseEvent) {

        Pair<RouteInfoController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.routeInfoView);
        pair.getKey().setRouteStationList(allRouteStations);
        pair.getKey().setAccess(userAccess);
        pair.getKey().setOutputPane(outputPane);
        pair.getKey().setPrevMenu(parentMenu);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }
}
