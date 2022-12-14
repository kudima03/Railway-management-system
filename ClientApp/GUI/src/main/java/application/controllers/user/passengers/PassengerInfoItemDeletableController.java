package application.controllers.user.passengers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.Passenger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class PassengerInfoItemDeletableController {

    private UserAccess access;

    private VBox outputPane;

    private Pair<PassengerChoiceController, Node> parentMenu;

    private Passenger passenger;

    @FXML
    private Label documentNumberLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    void onDeleteClick(MouseEvent event) {

        try {
            switch (access.deletePassenger(passenger.getId())) {
                case ERROR -> {
                    AlertManager.showWarningAlert("Невозможно удалить пассажира", "На пассажира оформлены билеты");
                    return;
                }
            }
            parentMenu.getKey().loadData();

        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }

    @FXML
    void onPassengerItemClicked(MouseEvent event) {

        Pair<AddOrEditPassengersController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditPassengerView);
        pair.getKey().setAccess(access);
        pair.getKey().setPrevMenu1(parentMenu);
        pair.getKey().setOutputAnchorPane(outputPane);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
        pair.getKey().setData(passenger);
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }

    public void setParentMenu(Pair<PassengerChoiceController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setData(Passenger passenger) {
        this.passenger = passenger;
        documentNumberLabel.setText(passenger.getDocumentNumber());
        fullNameLabel.setText(passenger.getSurname() + " " + passenger.getName() + " " + passenger.getPatronymic());
        phoneNumberLabel.setText(passenger.getPhoneNumber());
    }
}