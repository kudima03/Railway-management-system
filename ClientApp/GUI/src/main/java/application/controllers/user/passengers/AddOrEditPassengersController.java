package application.controllers.user.passengers;

import application.AlertManager;
import application.AutoCompleteComboBoxListener;
import application.InputValidation;
import clientConnectionModule.interfaces.UserAccess;
import databaseEntities.Classes.DocumentType;
import databaseEntities.Classes.Passenger;
import databaseEntities.Enums.Sex;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AddOrEditPassengersController {


    private Passenger passenger;

    private Pair<PassengerChoiceController, Node> prevMenu;

    private Pair<PassengerChoiceController, Node> prevMenu1;

    private VBox outputAnchorPane;

    private UserAccess access;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    public TextField emailInput;
    @FXML
    private TextField documentNumberInput;

    @FXML
    private ComboBox<DocumentType> documentTypesComboBox;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField patronymicInput;

    @FXML
    private TextField phoneNumberInput;

    @FXML
    private TextField surnameInput;

    @FXML
    private Label titleLabel;

    private boolean isEdit = false;

    private StringConverter<DocumentType> comboBoxConverter;

    private AutoCompleteComboBoxListener<DocumentType> listener;

    private List<DocumentType> documentTypes;

    public void initialize() {

        comboBoxConverter = new StringConverter<DocumentType>() {
            @Override
            public String toString(DocumentType documentType) {
                return documentType.getTypeName();
            }

            @Override
            public DocumentType fromString(String s) {
                var matches =
                        documentTypes.stream().filter((var type) -> type.getTypeName().equals(s)).toList();
                if (matches.size() != 1) return null;
                return matches.get(0);
            }
        };
    }

    @FXML
    void onBackLabelClicked(MouseEvent event) {

        outputAnchorPane.getChildren().clear();
        if (prevMenu != null) {
            outputAnchorPane.getChildren().add(prevMenu.getValue());
        } else if (prevMenu1 != null) {
            outputAnchorPane.getChildren().add(prevMenu1.getValue());
        }
    }

    @FXML
    void onConfirmButtonClicked(MouseEvent event) {

        var surname = surnameInput.getText();
        var name = nameInput.getText();
        var patronymic = patronymicInput.getText();
        Sex sex = Sex.Man;
        if (maleRadioButton.isSelected())
            sex = Sex.Man;
        if (femaleRadioButton.isSelected())
            sex = Sex.Woman;
        var date = dateOfBirthPicker.getValue();
        if (date == null){
            AlertManager.showWarningAlert("Введите дату рождения!", "");
            return;
        }
        var dateOfBirth = Date.from(Instant.from(date.atStartOfDay(ZoneId.systemDefault())));
        var documentType = documentTypesComboBox.getSelectionModel().getSelectedItem();
        var documentNumber = documentNumberInput.getText();
        var phoneNumber = phoneNumberInput.getText();
        var email = emailInput.getText();
        if (!InputValidation.isStringContainsOnlyLetters(surname)) {
            AlertManager.showWarningAlert("Ошибка ввода", "Неверный формат ввода фамилии");
            return;
        }
        if (!InputValidation.isStringContainsOnlyLetters(name)) {
            AlertManager.showWarningAlert("Ошибка ввода", "Неверный формат ввода имени");
            return;
        }
        if (!InputValidation.isStringContainsOnlyLetters(patronymic)) {
            AlertManager.showWarningAlert("Ошибка ввода", "Неверный формат ввода отчества");
            return;
        }
        if (!InputValidation.isPhoneNumber(phoneNumber)) {
            AlertManager.showWarningAlert("Ошибка ввода", "Неверный формат ввода телефона");
            return;
        }
        if (!InputValidation.isEmailCorrect(email.toCharArray())){
            AlertManager.showWarningAlert("Ошибка ввода", "Неверный формат ввода электронного адреса");
            return;
        }
        try {
            if (!isEdit) {
                var response = access.createPassenger(new Passenger(name, surname, patronymic,
                        phoneNumber, documentType, documentNumber, email, dateOfBirth, sex));
                switch (response){

                    case ERROR -> {
                        AlertManager.showWarningAlert("Ошибка", "Ошибка добавления");
                        return;
                    }
                }
            } else {
                passenger.setName(name);
                passenger.setSurname(surname);
                passenger.setPatronymic(patronymic);
                passenger.setPhoneNumber(phoneNumber);
                passenger.setDocumentType(documentType);
                passenger.setDocumentNumber(documentNumber);
                passenger.setDateOfBirth(dateOfBirth);
                passenger.setEmail(email);
                passenger.setSex(sex);
                var response = access.updatePassenger(passenger);
                switch (response){

                    case ERROR -> {
                        AlertManager.showWarningAlert("Ошибка", "Ошибка обновления");
                        return;
                    }
                }
            }
            outputAnchorPane.getChildren().clear();
            if (prevMenu != null) {
                outputAnchorPane.getChildren().add(prevMenu.getValue());
                prevMenu.getKey().loadData();
            } else if (prevMenu1 != null) {
                outputAnchorPane.getChildren().add(prevMenu1.getValue());
                prevMenu1.getKey().loadData();
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
            return;
        }
    }

    public void loadData() {

        try {
            documentTypes = access.getAllDocumentTypes();
            documentTypesComboBox.setItems(FXCollections.observableArrayList(documentTypes));
            documentTypesComboBox.setConverter(comboBoxConverter);
            documentTypesComboBox.getSelectionModel().selectFirst();
            listener = new AutoCompleteComboBoxListener<>(documentTypesComboBox);
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка подключения", "");
        }
    }

    public void setPrevMenu(Pair<PassengerChoiceController, Node> prevMenu) {

        prevMenu1 = null;
        this.prevMenu = prevMenu;
    }

    public void setAccess(UserAccess access) {
        this.access = access;
    }

    public void setOutputAnchorPane(VBox outputAnchorPane) {
        this.outputAnchorPane = outputAnchorPane;
    }

    public void setData(Passenger passengerToEdit) {

        isEdit = true;
        titleLabel.setText("Редактирование пассажира");
        passenger = passengerToEdit;
        surnameInput.setText(passengerToEdit.getSurname());
        nameInput.setText(passengerToEdit.getName());
        patronymicInput.setText(passengerToEdit.getPatronymic());
        emailInput.setText(passengerToEdit.getEmail());
        var date = passengerToEdit.getDateOfBirth();
        dateOfBirthPicker.setValue(LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate()));
        switch (passengerToEdit.getSex()) {
            case Man -> {
                maleRadioButton.setSelected(true);
            }
            case Woman -> {
                femaleRadioButton.setSelected(true);
            }
        }
        documentTypesComboBox.getSelectionModel().select(passengerToEdit.getDocumentType());
        documentNumberInput.setText(passengerToEdit.getDocumentNumber());
        phoneNumberInput.setText(passengerToEdit.getPhoneNumber());

    }

    public void setPrevMenu1(Pair<PassengerChoiceController, Node> prevMenu) {

        this.prevMenu = null;
        this.prevMenu1 = prevMenu;
    }
}
