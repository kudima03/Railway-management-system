package application.controllers.admin.statisticsOperationsControllers;

import application.AlertManager;
import clientConnectionModule.interfaces.AdminAccess;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForeignPassengersNumericalRatioPieChartController {
    private AdminAccess access;

    private VBox outputNode;

    @FXML
    private PieChart pieChart;

    public void loadData() {
        try {
            Map<Integer, Integer> map = new HashMap<>();
            var documentTypes = access.getAllDocumentTypesAdmin();
            var passengers = access.getAllPassengersAdmin();
            if (documentTypes.size() == 0 || passengers.size() == 0) return;
            var pieChartData = new ArrayList<PieChart.Data>();
            for (var documentType: documentTypes) {
                pieChartData.add(new PieChart.Data(documentType.getTypeName(),
                        passengers.stream().filter((var obj)-> obj.getDocumentType().getId() == documentType.getId()).count()));
            }
            var obsColl = FXCollections.observableList(pieChartData);
            pieChart.setData(obsColl);
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    public void setAccess(AdminAccess access) {
        this.access = access;
    }

    public void setOutputNode(VBox outputNode) {
        this.outputNode = outputNode;
    }

}
