package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class ViewLoader {
    public final String authorizationView = "common/authorization-view.fxml";
    public final String registrationView = "common/registration-view.fxml";
    public final String userView = "user/userMenu-view.fxml";
    public final String distanceInputView = "user/ticketSearch/distanceInfoInput-view.fxml";
    public final String timeTableView = "user/ticketSearch/timeTable-view.fxml";
    public final String timeTableItemView = "user/ticketSearch/timeTableItem-view.fxml";
    public final String routeInfoView = "user/routes/routeInfo-view.fxml";
    public final String routeInfoItemView = "user/routes/routeInfoItem-view.fxml";
    public final String passengerChoiceView = "user/passengers/passengerChoice-view.fxml";
    public final String buyTicketPreviewView = "user/ticketSearch/orderTicketPreview-view.fxml";
    public final String passengerInfoItemView = "user/passengers/passengerInfoItem-view.fxml";
    public final String passengerInfoDeletableItemView = "user/passengers/passengerInfoItemDeletable-view.fxml";
    public final String addOrEditPassengerView = "user/passengers/addOrEditPassenger-view.fxml";
    public final String orderPaymentView = "user/orders/orderPayment-view.fxml";
    public final String ordersView = "user/orders/orders-view.fxml";
    public final String orderItemView = "user/orders/orderItem-view.fxml";
    public final String userProfileView = "user/profile/userProfile-view.fxml";
    public final String rulesView = "user/rules/rules-view.fxml";
    public final String rulesItemView = "user/rules/ruleItem-view.fxml";
    public final String editProfileView = "user/profile/editProfile-view.fxml";
    public final String adminView = "admin/adminMenu-view.fxml";
    public final String trainItemView = "admin/trainsOperations/trainItem-view.fxml";
    public final String trainsView = "admin/trainsOperations/trains-view.fxml";
    public final String addOrEditTrainView = "admin/trainsOperations/addOrEditTrain-view.fxml";
    public final String driversView = "admin/driversOperations/drivers-view.fxml";
    public final String driverItemView = "admin/driversOperations/driverItem-view.fxml";
    public final String addOrEditDriverView = "admin/driversOperations/driversAddOrEdit-view.fxml";
    public final String stationItemView = "admin/stationsOperations/stationItem-view.fxml";
    public final String stationsView = "admin/stationsOperations/stations-view.fxml";
    public final String addOrEditStationView = "admin/stationsOperations/stationsAddOrEdit-view.fxml";
    public final String routeItemView = "admin/routesOperations/routeItem-view.fxml";
    public final String routesView = "admin/routesOperations/routes-view.fxml";
    public final String routesAddOrEditView = "admin/routesOperations/routesAddOrEdit-view.fxml";
    public final String routeStationItemView = "admin/routesOperations/routeStationItem-view.fxml";
    public final String statisticsView = "admin/statisticsOperations/statistics-view.fxml";
    public final String trainTypesNumericalRatioReportView = "admin/statisticsOperations/trainTypesNumericalRatioReport-view.fxml";
    public final String foreignPassengersNumericalRatioPieChartView = "admin/statisticsOperations/foreignPassengersNumericalRatioPieChart-view.fxml";
    public final String driverMenuView = "driver/driverMenu-view.fxml";
    public final String driverTimeTableView = "driver/timeTable/timeTable-view.fxml";
    public final String driverTimeTableItemView = "driver/timeTable/timeTableItem-view.fxml";
    public final String profileView = "driver/profile/profile-view.fxml";
    public final String changePasswordView = "driver/profile/changePassword-view.fxml";
    private final String pathPrefix = "/application/views/";
    private final Stage stage;
    public <ControllerType> Pair<ControllerType, Scene> getScene(String viewName, double width, double height) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pathPrefix + viewName));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            ControllerType controller = fxmlLoader.getController();
            return new Pair<>(controller, scene);
        } catch (IOException e) {
            AlertManager.showErrorAlert("View loading error", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public <ControllerType, ItemType> Pair<ControllerType, ItemType> getItem(String itemName) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(pathPrefix + itemName));
            ItemType item = fxmlLoader.load();
            ControllerType controller = fxmlLoader.getController();
            return new Pair<>(controller, item);
        } catch (IOException e) {
            AlertManager.showErrorAlert("View loading error", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setSceneToStage(Scene scene, String title) {

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public ViewLoader(Stage stage) {
        this.stage = stage;
    }
}
