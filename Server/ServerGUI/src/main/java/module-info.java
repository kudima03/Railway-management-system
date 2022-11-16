module com.example.servergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires BusinessLayer;
    requires TransferLayer;

    opens ServerGui to javafx.fxml;
    exports ServerGui;
}