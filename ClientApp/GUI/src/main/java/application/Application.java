package application;

import application.controllers.common.AuthorizationController;
import clientConnectionModule.implementations.ClientConnectionModule;
import com.itextpdf.text.DocumentException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Application extends javafx.application.Application {

    public static ViewLoader viewLoader;

    public static Properties getPropertiesFromConfig() throws IOException {

        var properties = new Properties();
        String propFileName = "ClientApp/GUI/src/main/resources/config.properties";
        var inputStream = new FileInputStream(propFileName);
        if (inputStream == null)
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        properties.load(inputStream);
        return properties;
    }

    @Override
    public void start(Stage stage) throws IOException, DocumentException {

        var properties = getPropertiesFromConfig();



        ClientConnectionModule clientConnectionModule = new ClientConnectionModule(
                properties.getProperty("serverIp"),
                Integer.parseInt(properties.getProperty("serverPort")));

        var state = clientConnectionModule.connectToServer();
        if (!state){
            AlertManager.showWarningAlert("Cannot connect to server", "");
            return;
        }
        viewLoader = new ViewLoader(stage);
        Pair<AuthorizationController, Scene> pair = viewLoader.getScene(viewLoader.authorizationView, 480, 600);
        pair.getKey().setAccess(clientConnectionModule);
        stage.setResizable(false);
        viewLoader.setSceneToStage(pair.getValue(), "Авторизация");
    }

    public static void main(String[] args) {
        launch();
    }
}