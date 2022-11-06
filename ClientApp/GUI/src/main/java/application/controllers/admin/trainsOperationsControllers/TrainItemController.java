package application.controllers.admin.trainsOperationsControllers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.AdminAccess;
import databaseEntities.Classes.Train;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class TrainItemController {

    private AdminAccess access;
    private Train train;

    private Pair<TrainsController, Node> parentMenu;
    private VBox output;
    @FXML
    private Label trainNumberLabel;

    @FXML
    private Label trainTypeNameLabel;

    @FXML
    void onDeleteClick(MouseEvent event) {

        try {
           var response = access.deleteTrain(train.getId());
           switch (response){
               case ERROR -> {
                   AlertManager.showWarningAlert("Ошибка", "Поезд участвует в маршруте");
                   return;
               }
           }
           parentMenu.getKey().loadData();
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }

    }
    @FXML
    void onEditClick(MouseEvent event) {

        Pair<AddOrEditTrainController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditTrainView);
        pair.getKey().setTrain(train);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputVBox(output);
        pair.getKey().loadData();
        output.getChildren().clear();
        output.getChildren().add(pair.getValue());
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setTrain(Train train) {
        this.train = train;
        trainNumberLabel.setText(String.valueOf(train.getNumber()));
        trainTypeNameLabel.setText(train.getType().getName());
    }

    public void setOutput(VBox output) {
        this.output = output;
    }

    public void setParentMenu(Pair<TrainsController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }
}
