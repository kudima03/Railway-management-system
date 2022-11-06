package application.controllers.user.rules;

import databaseEntities.Classes.Rule;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class RuleItemController {

    private Pair<RulesController, Node> parentMenu;

    private VBox outputPane;

    private Rule rule;
    @FXML
    private Label textLabel;

    public void setRule(Rule rule) {
        textLabel.setWrapText(true);
        textLabel.setText(rule.getText());
        this.rule = rule;
    }

    public void setParentMenu(Pair<RulesController, Node> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setOutputPane(VBox outputPane) {
        this.outputPane = outputPane;
    }
}
