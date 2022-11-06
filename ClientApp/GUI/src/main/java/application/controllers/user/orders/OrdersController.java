package application.controllers.user.orders;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.UserAccess;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class OrdersController {

    private UserAccess access;

    private VBox outputPane;
    @FXML
    private VBox ticketsVBox;

    public void initialize(){

        ticketsVBox.setAlignment(Pos.TOP_CENTER);
    }

    public void loadData() {

        ticketsVBox.getChildren().clear();
        try {
            var tickets = access.getAllTickets();
            if (tickets.size() == 0) return;
            tickets.sort((var obj, var obj2)-> obj.getPurchaseStatus().ordinal() > obj.getPurchaseStatus().ordinal()? 1:0);
            var routeStations = access.getAllRouteStations(tickets.get(0).getRoute().getId());
            for (var ticket : tickets) {

                var routeStation = routeStations.stream().filter(
                        (var obj)-> obj.getStation().getId() == ticket.getDepartureStation().getId()).toList().get(0);
                Pair<OrderItemController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.orderItemView);
                pair.getKey().setAccess(access);
                pair.getKey().setTicket(ticket, routeStation);
                pair.getKey().setOutputPane(outputPane);
                pair.getKey().setParentMenu(new Pair<>(this, outputPane.getChildren().get(0)));
                ticketsVBox.getChildren().add(pair.getValue());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
}
