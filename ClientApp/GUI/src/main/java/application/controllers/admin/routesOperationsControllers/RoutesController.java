package application.controllers.admin.routesOperationsControllers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.AdminAccess;
import customContainers.Pair;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class RoutesController {

    private AdminAccess access;

    private VBox output;

    @FXML
    private VBox routesVBox;

    @FXML
    void onAddClick(MouseEvent event) {

        javafx.util.Pair<RoutesAddOrEditController, ScrollPane> pair =
                Application.viewLoader.getItem(Application.viewLoader.routesAddOrEditView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutput(output);
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

    public void loadData() {

        routesVBox.getChildren().clear();
        try {
            var routes = access.getAllRoutes();
            for (var route : routes) {

                javafx.util.Pair<RouteItemController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.routeItemView);
                pair.getKey().setAccess(access);
                pair.getKey().setOutput(output);
                pair.getKey().setParentMenu(new javafx.util.Pair<>(this, output.getChildren().get(0)));
                var routeStations = access.getAllRouteStationsFromRoute(route.getId());
                pair.getKey().setRouteInfo(new Pair<>(route, routeStations));
                routesVBox.getChildren().add(pair.getValue());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }
}
