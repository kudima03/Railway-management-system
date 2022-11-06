package application.controllers.user;

import application.AlertManager;
import application.Application;
import application.controllers.common.AuthorizationController;
import application.controllers.user.orders.OrdersController;
import application.controllers.user.profile.UserProfileController;
import application.controllers.user.rules.RulesController;
import application.controllers.user.ticketSearch.DistanceInfoInputController;
import clientConnectionModule.interfaces.SingUpAccess;
import clientConnectionModule.interfaces.UserAccess;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class UserMenuController {

    private UserAccess access;

    @FXML
    private VBox outputVBox;
    @FXML
    private Label exitLabel;

    @FXML
    private Label findLabel;

    @FXML
    private Label orderLabel;

    @FXML
    private Label profileLabel;

    @FXML
    private Label rulesLabel;

    @FXML
    private Label timeTableLabel;

    @FXML
    void onExitLabelClicked(MouseEvent event) {

        try {
            access.exitToAuthorization();
            Pair<AuthorizationController, Scene> pair = Application.viewLoader.getScene(Application.viewLoader.authorizationView, 480, 600);
            pair.getKey().setAccess((SingUpAccess) access);
            Application.viewLoader.setSceneToStage(pair.getValue(), "Авторизация");
        } catch (Exception e) {
            AlertManager.showErrorAlert("ConnectionError", "");
        }
    }

    @FXML
    void onFindLabelClicked(MouseEvent event) {

        Pair<DistanceInfoInputController, AnchorPane> inputPair =
                Application.viewLoader.getItem(Application.viewLoader.distanceInputView);
        inputPair.getKey().setAccess(access);
        inputPair.getKey().setOutputPane(outputVBox);
        inputPair.getValue().setPrefHeight(outputVBox.getHeight());
        inputPair.getValue();
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(inputPair.getValue());
        inputPair.getKey().loadData();
    }

    @FXML
    void onOrderLabelClicked(MouseEvent event) {

        Pair<OrdersController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.ordersView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputVBox);
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onProfileLabelClicked(MouseEvent event) {

        Pair<UserProfileController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.userProfileView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputVBox);
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
    }

    @FXML
    void onRulesLabelClicked(MouseEvent event) {

        Pair<RulesController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.rulesView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputVBox);
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }
}
