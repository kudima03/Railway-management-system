package application.controllers.admin.driversOperationsControllers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.AdminAccess;
import databaseEntities.Classes.Driver;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class DriverItemController {

    private AdminAccess access;

    private Pair<DriversController, Node> parentMenu;

    private VBox output;

    private Driver driver;

    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label experienceLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private ImageView photoView;

    @FXML
    void onDeleteClick(MouseEvent event) {

        try {
            switch (access.deleteDriver(driver.getId())){
                case ERROR -> {
                    AlertManager.showWarningAlert("Ошибка удаления", "Водитель участвует в маршруте");
                    return;
                }
            }
            parentMenu.getKey().loadData();
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    @FXML
    void onEditClick(MouseEvent event) {

        Pair<AddOrEditDriverController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditDriverView);
        pair.getKey().setAccess(access);
        pair.getKey().setDriver(driver);
        pair.getKey().setOutput(output);
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setParentMenu(Pair<DriversController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        var dateOfBirth = driver.getDateOfBirth();
        dateOfBirthLabel.setText(dateOfBirth.getDate() + "." + (dateOfBirth.getMonth() + 1) + "." + (dateOfBirth.getYear() + 1900));
        experienceLabel.setText(driver.getExperience() + ".лет");
        fullNameLabel.setText(driver.getSurname() + " " + driver.getName() + " " + driver.getPatronymic());
    }

    public void setOutput(VBox output) {
        this.output = output;
    }
}
