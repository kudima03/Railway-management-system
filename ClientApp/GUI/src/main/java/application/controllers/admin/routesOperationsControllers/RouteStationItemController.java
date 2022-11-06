package application.controllers.admin.routesOperationsControllers;

import application.DateConverter;
import databaseEntities.Classes.RouteStation;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

public class RouteStationItemController {

    private RouteStation routeStation;

    private Pair<RoutesAddOrEditController, Node> parentMenu;

    private boolean isSelected = false;

    @FXML
    private HBox mainHBox;
    @FXML
    private Label arrivalTimeLabel;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label stationNameLabel;

    @FXML
    void onItemClicked(MouseEvent event) {

        if (isSelected){
            mainHBox.setStyle("");
            parentMenu.getKey().setSelectedRouteStation(null);
        } else {
            if (parentMenu.getKey().getSelectedRouteStation() != null) return;
            mainHBox.setStyle("-fx-background-color: #716F68;");
            parentMenu.getKey().setSelectedRouteStation(routeStation);
        }
        isSelected = !isSelected;

    }

    public void setRouteStation(RouteStation routeStation) {
        this.routeStation = routeStation;
        arrivalTimeLabel.setText(DateConverter.convert(routeStation.getArrivalDateTime()));
        departureTimeLabel.setText(DateConverter.convert(routeStation.getDepartureDateTime()));
        stationNameLabel.setText(routeStation.getStation().getName());
    }

    public void setParentMenu(Pair<RoutesAddOrEditController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }
}
