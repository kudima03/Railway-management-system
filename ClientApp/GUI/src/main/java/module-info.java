module GUI {

    requires javafx.controls;
    requires javafx.fxml;
    requires CommonEntities;
    requires ClientConnectionModule;
    requires itextpdf;
    requires icu4j;
    requires java.desktop;

    opens application to javafx.fxml;
    opens application.controllers.common to javafx.fxml;
    opens application.controllers.admin to javafx.fxml;
    opens application.controllers.admin.driversOperationsControllers to javafx.fxml;
    opens application.controllers.admin.trainsOperationsControllers to javafx.fxml;
    opens application.controllers.admin.stationsOperationsControllers to javafx.fxml;
    opens application.controllers.admin.routesOperationsControllers to javafx.fxml;
    opens application.controllers.driver to javafx.fxml;
    opens application.controllers.driver.timeTable to javafx.fxml;
    opens application.controllers.driver.profile to javafx.fxml;
    opens application.controllers.user to javafx.fxml;
    opens application.controllers.user.orders to javafx.fxml;
    opens application.controllers.user.passengers to javafx.fxml;
    opens application.controllers.user.profile to javafx.fxml;
    opens application.controllers.user.routes to javafx.fxml;
    opens application.controllers.user.rules to javafx.fxml;
    opens application.controllers.user.ticketSearch to javafx.fxml;
    exports application;
    exports application.controllers.common;
    exports application.controllers.user;
    exports application.controllers.user.ticketSearch;
    exports application.controllers.user.rules;
    exports application.controllers.user.routes;
    exports application.controllers.user.profile;
    exports application.controllers.user.passengers;
    exports application.controllers.user.orders;
    exports application.controllers.admin;
    exports application.controllers.admin.driversOperationsControllers;
    exports application.controllers.admin.trainsOperationsControllers;
    exports application.controllers.admin.stationsOperationsControllers;
    exports application.controllers.admin.routesOperationsControllers;
    exports application.controllers.driver;
    exports application.controllers.driver.profile;
    exports application.controllers.driver.timeTable;

}