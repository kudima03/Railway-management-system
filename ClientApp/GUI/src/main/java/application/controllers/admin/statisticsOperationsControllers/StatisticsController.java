package application.controllers.admin.statisticsOperationsControllers;

import application.AlertManager;
import application.Application;
import application.PDFManager;
import clientConnectionModule.interfaces.AdminAccess;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class StatisticsController {

    private AdminAccess access;

    private VBox outputNode;

    @FXML
    void onAmountOfSoldTicketsReportClicked(MouseEvent event) {
        try {
            PDFManager.createRoutesPopularityReport(access);
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }

    @FXML
    void onForeignPassengersNumericalRatioPieChartClicked(MouseEvent event) {

        Pair<ForeignPassengersNumericalRatioPieChartController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.foreignPassengersNumericalRatioPieChartView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputNode(outputNode);
        outputNode.getChildren().clear();
        outputNode.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    @FXML
    void onStationsWorkloadReportClicked(MouseEvent event) {

        try {
            PDFManager.createStationsWorkloadReport(access);
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }

    @FXML
    void onTrainTypesNumericalRatioPieChartClicked(MouseEvent event) {

        Pair<TrainTypesNumericalRatioReportController, VBox> pair = Application.viewLoader.getItem(Application.viewLoader.trainTypesNumericalRatioReportView);
        pair.getKey().setAccess(access);
        pair.getKey().setOutputNode(outputNode);
        outputNode.getChildren().clear();
        outputNode.getChildren().add(pair.getValue());
        pair.getKey().loadData();
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutputNode(VBox output) {
        this.outputNode = output;
    }
}
