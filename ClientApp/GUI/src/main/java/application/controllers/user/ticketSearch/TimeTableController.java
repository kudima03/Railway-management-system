package application.controllers.user.ticketSearch;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.UserAccess;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Date;

public class TimeTableController {

    private UserAccess access;

    private int startStationId;

    private int endStationId;

    private Date date;

    private Pair<DistanceInfoInputController, Node> prevMenu;

    @FXML
    private VBox outputPane;

    @FXML
    private VBox outputVBox;
    @FXML
    public Label backLabel;
    @FXML
    private Label routeNameLabel;

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }

    public void setData(int startStationId, int endStationId, Date date) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.date = date;
    }

    public void setPrevMenu(Pair<DistanceInfoInputController, Node> prevMenu) {
        this.prevMenu = prevMenu;
    }

    public void loadData() {

        outputVBox.getChildren().clear();
        try {
            var timeTable = access.getDistanceTimeTable(startStationId, endStationId, date);
            if (timeTable.size() == 0) {
                AlertManager.showInformationAlert("Нет прямого сообщения между станциями", "");
                return;
            }

            var routeStations = access.getAllRouteStations(timeTable.get(0).getFirstValue().getRoute().getId());
            var lastRouteStation = routeStations.stream().max((var element1, var element2) ->
                    element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get().getStation();
            var firstRouteStation = routeStations.stream().min((var element1, var element2) ->
                    element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get().getStation();

            for (var item : timeTable) {

                var departureRouteStation = item.getFirstValue();
                var arrivalRouteStation = item.getSecondValue();

                Pair<TimeTableItemController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.timeTableItemView);
                var distanceCost = access.calculateCost(departureRouteStation.getRoute().getId(), departureRouteStation.getStation().getId(), arrivalRouteStation.getStation().getId());
                pair.getKey().setData(departureRouteStation, arrivalRouteStation, firstRouteStation, lastRouteStation, routeStations, distanceCost);
                pair.getKey().setAccess(access);
                pair.getKey().setOutputPane(outputPane);
                pair.getKey().setParentMenu(new Pair<>(this, outputPane.getChildren().get(0)));
                pair.getValue().setPadding(new Insets(0, 0, 3, 0));
                outputVBox.getChildren().add(pair.getValue());
            }
            routeNameLabel.setText(firstRouteStation.getName() + "-" + lastRouteStation.getName());
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка получения данных", "");
        }
    }

    public void onBackLabelClick(MouseEvent mouseEvent) {

        outputPane.getChildren().clear();
        outputPane.getChildren().add(prevMenu.getValue());
    }
}

