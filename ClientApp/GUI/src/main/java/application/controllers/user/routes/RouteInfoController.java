package application.controllers.user.routes;

import application.Application;
import application.controllers.user.ticketSearch.TimeTableController;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.RouteStation;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.List;

public class RouteInfoController {

    @FXML
    private VBox outputVbox;
    @FXML
    private Label backLabel;
    private UserAccess userAccess;
    private List<RouteStation> routeStationList;

    private VBox outputPane;

    private Pair<TimeTableController, Node> prevMenu;

    public void setAccess(UserAccess userAccess) {
        this.userAccess = userAccess;
    }
    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
    public void loadData(){

        var firstRouteStation = routeStationList.stream().min((var element1, var element2) ->
                element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get();
        for (var routeStation: routeStationList) {

            Pair<RouteInfoItemController, HBox> pair = Application.viewLoader.getItem(Application.viewLoader.routeInfoItemView);
            pair.getKey().setData(routeStation, firstRouteStation.getDepartureDateTime());
            outputVbox.getChildren().add(pair.getValue());
        }

        for (int i = 0; i < 40; i++) {

        }
    }

    public void setPrevMenu(Pair<TimeTableController, Node> prevMenu) {
        this.prevMenu = prevMenu;
    }

    public void setRouteStationList(List<RouteStation> routeStation) {
        this.routeStationList = routeStation;
    }


    public void onBackLabelClicked(MouseEvent mouseEvent) {

        outputPane.getChildren().clear();
        outputPane.getChildren().add(prevMenu.getValue());
    }
}
