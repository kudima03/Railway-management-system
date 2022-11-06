package application.controllers.user.orders;

import application.Application;
import application.DateConverter;
import application.PDFManager;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.RouteStation;
import databaseEntities.Classes.Ticket;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;


public class OrderItemController {
    @FXML
    public Label statusLabel;
    @FXML
    public HBox statusHBox;
    private UserAccess access;

    private VBox outputPane;

    private Pair<OrdersController, Node> parentMenu;

    private Ticket ticket;

    @FXML
    private Label departureDateTime;

    @FXML
    private Label endStationLabel;

    @FXML
    private Label startStationLabel;

    @FXML
    private Label trainNumber;

    @FXML
    private Label trainTypeName;


    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }

    public void setParentMenu(Pair<OrdersController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setTicket(Ticket ticket, RouteStation depSt) {

        departureDateTime.setText(DateConverter.convert(depSt.getDepartureDateTime()));
        startStationLabel.setText(ticket.getDepartureStation().getName());
        endStationLabel.setText(ticket.getArrivalStation().getName());
        trainNumber.setText(String.valueOf(ticket.getRoute().getNumber()));
        trainTypeName.setText(ticket.getRoute().getTrain().getType().getName());
        switch (ticket.getPurchaseStatus()) {

            case WaitingForPayment -> {
                statusLabel.setText("Ждёт оплаты");
                statusHBox.setStyle("-fx-background-color: #EBCB3C");
            }
            case Paid -> {
                statusLabel.setText("Оплачен");
                statusHBox.setStyle("-fx-background-color: #24d447");
            }
        }
        this.ticket = ticket;
    }

    public void onItemClicked(MouseEvent mouseEvent) {

        switch (ticket.getPurchaseStatus()) {

            case WaitingForPayment -> {

                Pair<OrderPaymentController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.orderPaymentView);
                pair.getKey().setAccess(access);
                pair.getKey().setOutputPane(outputPane);
                pair.getKey().setOutputPane(outputPane);
                pair.getKey().setParentMenu(parentMenu);
                pair.getKey().setTicket(ticket);
                outputPane.getChildren().clear();
                outputPane.getChildren().add(pair.getValue());
            }
            case Paid -> {
                PDFManager.openPdfTicketByDefaultSystemViewer(ticket);
            }
        }
    }
}
