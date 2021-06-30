package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.service.BasicUserService;
import com.example.demo001.service.TransportProviderService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@FxmlView("administratorPanel.fxml")
public class AdministratorPanelController implements Initializable {

    // Changing the tabs
    @FXML
    private Label tabText;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane myAccountPage;
    @FXML
    private BorderPane manageUserPage;
    @FXML
    private Button myAccountButton, manageUsersButton, currentOrdersButton, historyButton;

    // My account tab
    @FXML
    private TextField accountNameField;

    @FXML
    private PasswordField accountPasswordField;

    @FXML
    private Button changeDetailsButton, saveDetailsButton;

    // Manage users tab - KOLEKCJA AKTUALNEJ SESJII
    private ObservableList <BasicUser> users = null;
    @FXML
    private TableView <BasicUser> usersTable;
    @FXML
    private TableColumn <BasicUser, Number> idColumn;
    @FXML
    private TableColumn <BasicUser, String> nameColumn;
    @FXML
    private TableColumn <BasicUser, String> surnameColumn;
    @FXML
    private Button addButton, updateButton, deleteButton;
    @FXML
    private TextField searchField;

    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private TransportProviderService transportProviderService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //NOWOSC
    public void setAdministratorDetails(BasicUser user) {
        // Backend to do
        // Getting details about administrator
        accountNameField.setText(user.getUserName());
        accountPasswordField.setText(user.getUserPassword());
    }

    public void setUserDetails()
    {
        BasicUser user = basicUserService.findByUsername(NavigationController.username);
        accountNameField.setText(user.getUserName());
        accountPasswordField.setText(user.getUserPassword());
    }

    // My account page
    // That details are the same for all client Panel for now.
    // It can be taken out to the main class, but it depends if details are gonna be the same. It is up to you.
    public void myAccountButtonOnAction() {
        // Setting the color of te top vBox for more dynamic view
        clearFilters();
        vBox.setBackground(new Background(new BackgroundFill(Color.web("#40c4ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        tabText.setText("My account");
        myAccountPage.toFront();
    }

    public void changeDetailsButtonOnAction() {
        accountNameField.setEditable(true);
        accountPasswordField.setEditable(true);
        saveDetailsButton.toFront();
    }

    // CHANGES HAS BEEN HERE
    // Copy the code and just paste in
    ////////////////////////////////////////////////////
    public void saveDetailsButtonOnAction() throws IOException {
        // Backend
        // Saving changed details for a user

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../AlertBox.fxml"));
        String textInfo;
        if (accountNameField.getText().isEmpty()) {
            textInfo = "This username is already taken";
            new AlertBoxController().createAlert(fxmlLoader, textInfo);
        }
        else {
            savingDetails();
        }

    }

    public void savingDetails(){

        accountNameField.getText();
        accountPasswordField.getText();
        saveDetailsButton.toFront();

        //Set back field to uneditable
        accountNameField.setEditable(false);
        accountPasswordField.setEditable(false);
        changeDetailsButton.toFront();
    }
    ////////////////////////////////////////////////////
    // Manager users tab
    public void manageUsersButtonOnAction() throws IOException {
        // Set up the style
        usersTable.getStylesheets().add("sample/styling/tableView.css");
        usersTable.getStyleClass().add("tableview");
        tabText.setText("Manage users");
        manageUserPage.toFront();

        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getUserId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        surnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));

        //pobieranie danych
        users = FXCollections.observableArrayList(basicUserService.findAll());

        deleteButton.disableProperty().bind(Bindings.isNull (
                usersTable.getSelectionModel().selectedItemProperty()));
        updateButton.disableProperty().bind(Bindings.isNull(
                usersTable.getSelectionModel().selectedItemProperty()));

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<BasicUser> filteredData = new FilteredList<BasicUser>(users, b -> true);

        //filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(user.getUserId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<BasicUser> sortedData = new SortedList<BasicUser>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());

        //  Setting the table
        usersTable.setItems(sortedData);
    }

    public void deleteButtonOnAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../confirmationBox.fxml"));
        Optional<ButtonType> isConfirmed = new ConfirmationBoxController().createConfirmation(fxmlLoader, "Are you sure you would like to delete that user ?", "Deleting User", "Ok", "Cancel");
        if(isConfirmed.get() == ButtonType.OK) {
            // Backend to do
            // Deleting chosen user from table
            int selectedId = usersTable.getSelectionModel().getSelectedIndex();
            //operowanie na kolekcji frontendowej
            users.remove(selectedId);
        }
    }

    public void updateButtonOnAction() throws IOException {
        BasicUser selectedUser = usersTable.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../userForm.fxml"));
        new UserFormController().init(fxmlLoader, selectedUser, "Update User");
        // Backend to do
        // Update data for selectedUser - inside of the UserFormController
    }

    public void addButtonOnAction() throws IOException {
        BasicUser newUser = new Client();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../userForm.fxml"));
        Optional<ButtonType> isConfirmed  = new UserFormController().init(fxmlLoader, newUser, "Add User");
        if (isConfirmed.get() == ButtonType.OK){
            // Backend to do
            // Adding new user to table
            //users.add(newUser);
        }
    }

    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
    }

}
