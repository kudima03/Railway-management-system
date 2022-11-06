package application.controllers.user.profile;

import application.Application;
import application.controllers.user.orders.OrdersController;
import application.controllers.user.passengers.PassengerChoiceController;
import clientConnectionModule.interfaces.UserAccess;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class UserProfileController {

    private UserAccess access;

    private VBox outputPane;


    @FXML
    void onEditProfileClicked(MouseEvent event) {

        Pair<EditProfileController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.editProfileView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputPane);
        pair.getKey().setPreviousMenu(new Pair<>(this, outputPane.getChildren().get(0)));
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
    }

    @FXML
    void onOrdersClicked(MouseEvent event) {

        Pair<OrdersController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.ordersView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputPane);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onPassengerClicked(MouseEvent event) {

        Pair<PassengerChoiceController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.passengerChoiceView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputPane);
        pair.getKey().setPrevMenu1(new Pair<>(this, outputPane.getChildren().get(0)));
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
}