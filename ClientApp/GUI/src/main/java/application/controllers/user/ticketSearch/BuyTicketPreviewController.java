package application.controllers.user.ticketSearch;

import application.AlertManager;
import application.Application;
import application.DateConverter;
import application.controllers.user.orders.OrdersController;
import application.controllers.user.passengers.PassengerChoiceController;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.Passenger;
import databaseEntities.Classes.RouteStation;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.List;

public class BuyTicketPreviewController {


    private UserAccess access;

    private Pair<TimeTableController, Node> prevMenu;

    private VBox outputPane;

    int amountOfTickets = 1;

    @FXML
    private Label arrivalStationNameLabel;

    @FXML
    public CheckBox checkBox;

    @FXML
    private Label arrivalTimeLabel;

    @FXML
    public Label amountOfTicketsLabel;

    @FXML
    private Button confirmOrderButton;

    @FXML
    private Button decrementButton;

    @FXML
    private Label departureStationNameLabel;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label fillPassengerInfoLabel;

    @FXML
    private Button incrementButton;

    @FXML
    private Label orderTotalCost;

    @FXML
    private Label routeEndStationLabel;

    @FXML
    private Label routeNumberLabel;

    @FXML
    private Label routeStartStationLabel;

    @FXML
    private Label trainTypeLabel;

    @FXML
    private Label travelTimeLabel;

    private Passenger passenger;

    private RouteStation depStation;

    private RouteStation arrStation;

    private float oneTicketCost;

    public void initialize(){
        decrementButton.setDisable(true);
        checkBox.setSelected(false);
        confirmOrderButton.setDisable(true);
    }
    @FXML
    void onConfirmOrderButtonClick(MouseEvent event) {

        if (passenger == null){
            AlertManager.showInformationAlert("Ошибка", "Выберите пассажира");
            return;
        }
        try {
            for (int i = 0; i < amountOfTickets; i++) {
                access.createTicket(passenger.getId(), depStation.getRoute().getId(), depStation.getStation().getId(), arrStation.getStation().getId());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
            return;
        }
        Pair<OrdersController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.ordersView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputPane);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }
    @FXML
    void onDecrementButtonClick(MouseEvent event) {
        if (amountOfTickets == 2){
            decrementButton.setDisable(true);
        }
        amountOfTickets--;
        amountOfTicketsLabel.setText(String.valueOf(amountOfTickets));
        orderTotalCost.setText(String.valueOf(amountOfTickets * oneTicketCost));
    }
    @FXML
    void onFillPassengerInfoLabelClick(MouseEvent event) {

        Pair<PassengerChoiceController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.passengerChoiceView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputPane);
        pair.getKey().setPrevMenu(new Pair<>(this, outputPane.getChildren().get(0)));
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }
    @FXML
    void onIncrementButtonClick(MouseEvent event) {

        if (amountOfTickets == 1){
            decrementButton.setDisable(false);
        }
        amountOfTickets++;
        amountOfTicketsLabel.setText(String.valueOf(amountOfTickets));
        orderTotalCost.setText(String.valueOf(amountOfTickets * oneTicketCost));
    }
    @FXML
    public void onBackClicked(MouseEvent mouseEvent) {

        outputPane.getChildren().clear();
        outputPane.getChildren().add(prevMenu.getValue());
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }
    public void setPrevMenu(Pair<TimeTableController, Node> prevMenu) {
        this.prevMenu = prevMenu;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
        confirmOrderButton.setDisable(!(checkBox.isSelected() && passenger != null));
    }

    public void setData(RouteStation depStation, RouteStation arrivalStation, List<RouteStation> allRouteStations, float oneTicketCost){

        var firstRouteStation = allRouteStations.stream().min((var element1, var element2) ->
                element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get();
        var lastRouteStation = allRouteStations.stream().min((var element1, var element2) ->
                element1.getOrdinalNumber() < element2.getOrdinalNumber() ? 1 : -1).get();
        this.depStation = depStation;
        this.arrStation = arrivalStation;
        this.oneTicketCost = oneTicketCost;
        departureTimeLabel.setText(DateConverter.convert(depStation.getDepartureDateTime()));
        arrivalTimeLabel.setText(DateConverter.convert(arrStation.getDepartureDateTime()));
        departureStationNameLabel.setText(depStation.getStation().getName());
        arrivalTimeLabel.setText(DateConverter.convert(arrivalStation.getArrivalDateTime()));
        arrivalStationNameLabel.setText(arrivalStation.getStation().getName());
        travelTimeLabel.setText(DateConverter.convertDifference(firstRouteStation.getDepartureDateTime(), arrivalStation.getArrivalDateTime()));
        trainTypeLabel.setText(depStation.getRoute().getTrain().getType().getName());
        orderTotalCost.setText(String.valueOf(oneTicketCost));
        routeNumberLabel.setText(String.valueOf(depStation.getRoute().getNumber()));
        routeStartStationLabel.setText(firstRouteStation.getStation().getName());
        routeEndStationLabel.setText(lastRouteStation.getStation().getName());
    }

    public void onCheckBoxClicked(MouseEvent mouseEvent) {
        confirmOrderButton.setDisable(!(checkBox.isSelected() && passenger != null));
    }
}
