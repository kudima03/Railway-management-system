package application.controllers.user.passengers;

import application.AlertManager;
import application.Application;
import application.controllers.user.ticketSearch.BuyTicketPreviewController;
import application.controllers.user.profile.UserProfileController;
import clientConnectionModule.interfaces.UserAccess;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class PassengerChoiceController {

    private UserAccess access;

    private Pair<BuyTicketPreviewController, Node> prevMenu;

    private Pair<UserProfileController, Node> prevMenu1;

    private VBox outputPane;

    @FXML
    private VBox passengersVBox;

    @FXML
    void onAddPassengerClick(MouseEvent event) {

        Pair<AddOrEditPassengersController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditPassengerView);
        pair.getKey().setAccess(access);
        pair.getKey().setPrevMenu(new Pair<>(this, outputPane.getChildren().get(0)));
        pair.getKey().setOutputAnchorPane(outputPane);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onBackLabelClicked(MouseEvent event) {

        outputPane.getChildren().clear();
        if (prevMenu != null) {
            outputPane.getChildren().add(prevMenu.getValue());
        } else if (prevMenu1 != null) {
            outputPane.getChildren().add(prevMenu1.getValue());
        }
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setPrevMenu(Pair<BuyTicketPreviewController, Node> prevMenu) {
        prevMenu1 = null;
        this.prevMenu = prevMenu;
    }

    public void setPrevMenu1(Pair<UserProfileController, Node> prevMenu) {
        this.prevMenu = null;
        this.prevMenu1 = prevMenu;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }

    public void loadData() {

        passengersVBox.getChildren().clear();
        try {

            var passengers = access.getAllPassengers();
            for (var passenger : passengers) {

                if (prevMenu != null) {
                    Pair<PassengerInfoItemController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.passengerInfoItemView);
                    pair.getKey().setData(passenger);
                    pair.getKey().setAccess(access);
                    pair.getKey().setOutputPane(outputPane);
                    pair.getKey().setParentMenu(new Pair<>(this, outputPane.getChildren().get(0)));
                    passengersVBox.getChildren().add(pair.getValue());
                } else if (prevMenu1 != null) {

                    Pair<PassengerInfoItemDeletableController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.passengerInfoDeletableItemView);
                    pair.getKey().setData(passenger);
                    pair.getKey().setAccess(access);
                    pair.getKey().setOutputPane(outputPane);
                    pair.getKey().setParentMenu(new Pair<>(this, outputPane.getChildren().get(0)));
                    passengersVBox.getChildren().add(pair.getValue());
                }
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка получения данных", "");
        }
    }

    public Pair<BuyTicketPreviewController, Node> getPrevMenu() {
        return prevMenu;
    }

}
