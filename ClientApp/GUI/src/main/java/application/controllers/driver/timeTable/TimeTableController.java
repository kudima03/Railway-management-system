package application.controllers.driver.timeTable;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.DriverAccess;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class TimeTableController {

    private DriverAccess access;
    @FXML
    private VBox itemsVBox;

    public void loadData(){

        itemsVBox.getChildren().clear();
        try {
            var routesInfo = access.getRoutes();
            for (var routeInfo : routesInfo) {

                Pair<TimeTableItemController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.driverTimeTableItemView);
                pair.getKey().setData(routeInfo);
                itemsVBox.getChildren().add(pair.getValue());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", e.getMessage());
        }
    }

    public void setAccess(DriverAccess access) {
        this.access = access;
    }
}
