package application.controllers.user.passengers;

import application.Application;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.Passenger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class PassengerInfoItemController {

    private Pair<PassengerChoiceController, Node> parentMenu;

    private UserAccess access;

    private VBox outputPane;

    @FXML
    private Label documentNumberLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label phoneNumberLabel;

    private Passenger passenger;

    @FXML
    void onEditClick(MouseEvent event) {

        Pair<AddOrEditPassengersController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditPassengerView);
        pair.getKey().setAccess(access);
        pair.getKey().setPrevMenu(parentMenu);
        pair.getKey().setOutputAnchorPane(outputPane);
        pair.getKey().setData(passenger);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onPassengerItemClicked(MouseEvent event) {

        outputPane.getChildren().clear();
        parentMenu.getKey().getPrevMenu().getKey().setPassenger(passenger);
        outputPane.getChildren().add(parentMenu.getKey().getPrevMenu().getValue());
    }

    public void setData(Passenger passenger){

        this.passenger = passenger;
        documentNumberLabel.setText(passenger.getDocumentNumber());
        fullNameLabel.setText(passenger.getSurname() + " " + passenger.getName() + " " + passenger.getPatronymic());
        phoneNumberLabel.setText(passenger.getPhoneNumber());
    }

    public void setParentMenu(Pair<PassengerChoiceController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }
    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
}
