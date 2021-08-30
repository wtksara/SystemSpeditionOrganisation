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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.hibernate.sql.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
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

        if(NavigationController.manageUsersToFront)
        {
            try{manageUsersButtonOnAction();}
            catch(IOException e){}
            NavigationController.manageUsersToFront=false;
        }
        else
            setUserDetails();
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
        setUserDetails();
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

        String newUsername = accountNameField.getText();
        String newPassword = accountPasswordField.getText();
        String oldUsername = NavigationController.username;
        BasicUser user = basicUserService.findByUsername(oldUsername);
        String oldPassword=user.getUserPassword();

        boolean changeDetails = true;

        if(!newUsername.equals(oldUsername))
        {
            BasicUser user2 = basicUserService.findByUsername(newUsername);
            if (user2!=null)
            {
                NavigationController.alertText="Username taken, choose another username";
                changeDetails=false;
            }
            else if(newUsername.isEmpty())
            {
                NavigationController.alertText="Username can not be empty";
                changeDetails=false;
            }
        }

        if(!newPassword.equals(oldPassword))
        {
            if(newPassword.isEmpty())
            {
                NavigationController.alertText="Password can not be empty";
                changeDetails=false;
            }
        }

        if(changeDetails)
        {
            NavigationController.username=newUsername;
            user.setUserName(newUsername);
            user.setUserPassword(newPassword);
            basicUserService.saveChangedUser(user);
            accountNameField.setEditable(false);
            accountPasswordField.setEditable(false);
            changeDetailsButton.toFront();
            NavigationController.alertText="Details changed successfully";
        }
        callAlertBox();
    }


    public void callAlertBox(){
        NavigationController.lastSceneName="Spedition Organisation System - Administrator";
        NavigationController.lastScene = NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(AlertBoxController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Alert!");
        NavigationController.stage.show();
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
        BasicUser selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if(!NavigationController.username.equals(selectedUser.getUserName()))
        {
            NavigationController.alertText="Are you sure you want to delete this user?";
            NavigationController.operatedUser=selectedUser.getUserName();
            NavigationController.lastSceneName=NavigationController.stage.getTitle();
            NavigationController.lastScene = NavigationController.stage.getScene();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(DeleteUserConfirmationBoxController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Confirm Delete");
            NavigationController.stage.show();
        }
        else
        {
            NavigationController.alertText="You can't delete your account!";
            NavigationController.lastSceneName=NavigationController.stage.getTitle();
            NavigationController.lastScene = NavigationController.stage.getScene();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(AlertBoxController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Alert!");
            NavigationController.stage.show();
        }
    }

    public void updateButtonOnAction() throws IOException {
        NavigationController.updatingUser = true;
        BasicUser selectedUser = usersTable.getSelectionModel().getSelectedItem();
        NavigationController.operatedUser=selectedUser.getUserName();

        NavigationController.lastSceneName="Spedition Organisation System - Administrator";
        NavigationController.lastScene = NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(UserFormController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Change User Details");
        NavigationController.stage.show();
    }

    public void addButtonOnAction() throws IOException {

        NavigationController.updatingUser = false;
        NavigationController.lastSceneName="Spedition Organisation System - Administrator";
        NavigationController.lastScene = NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(UserFormController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("New User Details");
        NavigationController.stage.show();
    }

    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
    }

}
