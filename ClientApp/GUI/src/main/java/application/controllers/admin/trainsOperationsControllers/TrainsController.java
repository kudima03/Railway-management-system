package application.controllers.admin.trainsOperationsControllers;

import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.AdminAccess;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class TrainsController {

    private AdminAccess access;
    @FXML
    private VBox trainsVBox;

    private VBox outputVBox;

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutputNode(VBox outputNode) {
        this.outputVBox = outputNode;
    }

    public void loadData(){

        trainsVBox.getChildren().clear();
        try {
            var trains = access.getAllTrains();
            for (var train: trains) {

                Pair<TrainItemController, VBox> pair =  Application.viewLoader.getItem(Application.viewLoader.trainItemView);
                pair.getKey().setAccess(access);
                pair.getKey().setTrain(train);
                pair.getKey().setOutput(outputVBox);
                pair.getKey().setParentMenu(new Pair<>(this, outputVBox.getChildren().get(0)));
                trainsVBox.getChildren().add(pair.getValue());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    public void onAddClick(MouseEvent mouseEvent) {

        Pair<AddOrEditTrainController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.addOrEditTrainView);
        pair.getKey().setOutputVBox(outputVBox);
        pair.getKey().setAccess(access);
        pair.getKey().loadData();
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
    }
}
