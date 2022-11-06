package application.controllers.admin.trainsOperationsControllers;

import application.AlertManager;
import application.Application;
import application.AutoCompleteComboBoxListener;
import clientConnectionModule.interfaces.AdminAccess;
import databaseEntities.Classes.Train;
import databaseEntities.Classes.TrainType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.List;

public class AddOrEditTrainController {

    private AdminAccess access;

    private Train train;

    private VBox outputVBox;

    private List<TrainType> trainTypes;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField trainNumberInput;

    @FXML
    private ComboBox<TrainType> trainTypeComboBox;

    private AutoCompleteComboBoxListener<TrainType> cmbListener;

    @FXML
    void onBackClicked(MouseEvent event) {

        Pair<TrainsController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.trainsView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputNode(outputVBox);
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onConfirmClicked(MouseEvent event) {

        int number = 0;
        try{
            number = Integer.parseInt(trainNumberInput.getText());
        } catch (NumberFormatException e) {
            AlertManager.showWarningAlert("Неверный формат номера", "");
            return;
        }
        var trainType = trainTypeComboBox.getSelectionModel().getSelectedItem();

        if (trainType == null){
            AlertManager.showWarningAlert("Выберите тип поезда", "");
            return;
        }

        try {
            if (train == null){
                access.createTrain(new Train(0, trainType, number));
            } else {
                train.setType(trainType);
                train.setNumber(number);
                access.editTrain(train);
            }

        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
            return;
        }

        Pair<TrainsController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.trainsView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputNode(outputVBox);
        outputVBox.getChildren().clear();
        outputVBox.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void initialize() {
        var comboBoxConverter = new StringConverter<TrainType>() {
            @Override
            public String toString(TrainType trainType) {
                if (trainType == null) return "";
                return trainType.getName();
            }

            @Override
            public TrainType fromString(String s) {
                var matches =
                        trainTypes.stream().filter((var trainType) -> trainType.getName().equals(s)).toList();
                if (matches.size() != 1) return null;
                return matches.get(0);
            }
        };
        trainTypeComboBox.setConverter(comboBoxConverter);
        cmbListener = new AutoCompleteComboBoxListener<>(trainTypeComboBox);
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setTrain(Train train) {
        this.train = train;
        trainNumberInput.setText(String.valueOf(train.getNumber()));
        trainTypeComboBox.getSelectionModel().select(train.getType());
    }

    public void setOutputVBox(VBox outputVBox) {
        this.outputVBox = outputVBox;
    }

    public void loadData() {

        try {
            trainTypes = access.getAllTrainTypes();
            trainTypeComboBox.setItems(FXCollections.observableList(trainTypes));
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }
}
