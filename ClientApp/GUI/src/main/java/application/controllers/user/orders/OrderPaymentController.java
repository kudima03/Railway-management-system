package application.controllers.user.orders;

import application.AlertManager;
import application.Application;
import application.InputValidation;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.Ticket;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class OrderPaymentController {

    private Pair<OrdersController, Node> parentMenu;

    private UserAccess access;

    private Ticket ticket;

    private VBox outputPane;

    @FXML
    private TextField cardNumberInput;

    @FXML
    private TextField cvvInput;

    @FXML
    private TextField dateOfIssueInput;

    @FXML
    private TextField ownerInput;

    @FXML
    void onPayButtonClick(MouseEvent event) {

        var cardNumber = cardNumberInput.getText();
        var cvv = cvvInput.getText();
        var dateOfIssue = dateOfIssueInput.getText();
        var owner = ownerInput.getText();
        if (!InputValidation.isCardNumberCorrect(cardNumber)){
            AlertManager.showWarningAlert("Неверный формат номера карты", "");
            return;
        }
        if (!InputValidation.isCVVCorrect(cvv)){
            AlertManager.showWarningAlert("Неверный формат cvv", "");
            return;
        }
        if (!InputValidation.isDateOfIssueCorrect(dateOfIssue)){
            AlertManager.showWarningAlert("Неверный формат даты выдачи", "");
            return;
        }
        if (!InputValidation.isCardOwnerCorrect(owner)){
            AlertManager.showWarningAlert("Неверный формат имени держателя", "");
            return;
        }
        try {
            access.payTicket(ticket.getId());
        } catch (Exception e) {
           AlertManager.showErrorAlert("Ошибка соединения", "");
           return;
        }
        AlertManager.showInformationAlert("Успешно оплачено!", "");
        Pair<OrdersController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.ordersView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputPane(outputPane);
        outputPane.getChildren().clear();
        outputPane.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setParentMenu(Pair<OrdersController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @FXML
    public void onBackLabelClick(MouseEvent mouseEvent) {

        outputPane.getChildren().clear();
        outputPane.getChildren().add(parentMenu.getValue());
    }
}
