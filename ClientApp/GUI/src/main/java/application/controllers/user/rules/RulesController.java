package application.controllers.user.rules;
import application.AlertManager;
import application.Application;
import clientConnectionModule.interfaces.UserAccess;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class RulesController {

    private UserAccess access;

    private VBox outputPane;

    @FXML
    private VBox outputVbox;

    public void loadData() {
        outputVbox.getChildren().clear();
        try {
            var rules = access.getRules();
            for (var rule : rules) {
                Pair<RuleItemController, Node> pair = Application.viewLoader.getItem(Application.viewLoader.rulesItemView);
                pair.getKey().setRule(rule);
                pair.getKey().setParentMenu(new Pair<>(this, outputPane.getChildren().get(0)));
                pair.getKey().setOutputPane(outputPane);
                outputVbox.getChildren().add(pair.getValue());
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
}