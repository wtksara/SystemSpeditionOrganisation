package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("transportProviderPanel.fxml")
public class TransportProviderPanelController implements Initializable {

    // Changing the tabs
    @FXML
    private Label tabText;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane myAccountPage; //my account page (1)
    @FXML
    private BorderPane myRoutePage; //my route page (2)
    @FXML
    private BorderPane pendingOrdersPage; //pending orders page (3)
    @FXML
    private Button myAccountButton, myRouteButton, pendingOrdersButton; //buttons to change tab

    // My account tab (1) - user account
    @FXML
    private TextField accountNameField;
    @FXML
    private PasswordField accountPasswordField;
    @FXML
    private Button changeDetailsButton, saveDetailsButton;

    // My route tab (2) - all transport provider routes
    private ObservableList <ProductOrder> users = null;
    @FXML
    private TableView <ProductOrder> usersTable;
    @FXML
    private TableColumn <ProductOrder, Number> idColumn;
    @FXML
    private TableColumn <ProductOrder, String> clientNameColumn;
    @FXML
    private TableColumn <ProductOrder, String> fromColumn;
    @FXML
    private TableColumn <ProductOrder, String> toColumn;
    @FXML
    private TableColumn <ProductOrder, String> stateColumn;
    @FXML
    private TextField searchField;
    @FXML
    private TextField currentOrderSearchField;

    // Pending orders tab (3) - orders pending approval or rejection by transport provider
    @FXML
    private TableView <ProductOrder> pendingOrdersTable;
    @FXML
    private TableColumn <ProductOrder, CheckBox> checkBoxTransportColumn;
    @FXML
    private TableColumn <ProductOrder, Number> idPendingOrdersColumn;
    @FXML
    private TableColumn <ProductOrder, String> clientNamePendingOrdersColumn;
    @FXML
    private TableColumn <ProductOrder, String> fromPendingOrdersColumn;
    @FXML
    private TableColumn <ProductOrder, String> toPendingOrdersColumn;
    @FXML
    private TableColumn <ProductOrder, String> statePendingOrdersColumn;
    @FXML
    private Button acceptTransportButton, declineTransportButton;
    @FXML
    private TextField searchField2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }


    //NOWOSC
    public void setTransportProviderDetails(BasicUser user) {
        // Backend to do
        // Getting details about transport provider
        accountNameField.setText(user.getUserName());
        accountPasswordField.setText(user.getUserPassword());
    }

    // My account page (1)
    // That details are the same for all client Panel for now.
    // It can be taken out to the main class, but it depends if details are gonna be the same. It is up to you.
    public void myAccountButtonOnAction() { //change tab to "My account"
        // Setting the color of te top vBox for more dynamic view
        clearFilters();
        vBox.setBackground(new Background(new BackgroundFill(Color.web("#40c4ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        tabText.setText("My account");
        myAccountPage.toFront();
    }

    public void changeDetailsButtonOnAction() { //to allow the user change the data
        accountNameField.setEditable(true);
        accountPasswordField.setEditable(true);
        saveDetailsButton.toFront();
    }

    public void saveDetailsButtonOnAction() throws IOException { //saving the data changed by user
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

    // My route tab (2)
    public void myRouteButtonOnAction() throws IOException { //change tab to "My route"
        // Set up the style
        usersTable.getStylesheets().add("sample/styling/tableView.css");
        usersTable.getStyleClass().add("tableview");
        tabText.setText("My route");
        myRoutePage.toFront();

        // Backend to do
        // Displaying in table all orders of the transport provider
        // (order/route ID, client name, from (city), to = destination (city), order state)

        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()));
        clientNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));
        fromColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getClientCity().getCityName()));
        //Dwa razy miasto klienta - na razie, bo ciężko ogarnąć wszystkie fabryki zamówienia
        toColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getClientCity().getCityName()));
        stateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));

        users = FXCollections.observableArrayList();
        //users = FXCollections.observableArrayList(UsersRepository.getUsers());
        //users = FXCollections.observableArrayList(factoryForOrderItemSetup.getFactoryByName(sessionFactory.getFactoryName()).getProducedProducts());
        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<ProductOrder> filteredData = new FilteredList<>(users, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(user.getOrderId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getOrderClient().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getOrderStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<ProductOrder> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());

        //  Setting the table
        usersTable.setItems(sortedData);
    }

    public void acceptTransportButtonOnAction(){ //acceptance of the order by transport provider
        //TODO - ustawienie odpowiednich pól obiektu sesji
        /*ObservableList<User> selectedList = FXCollections.observableArrayList();
        for (User order : users){
            if(order.getSelect().isSelected()){
                // Backend to do
                // Changing the status of order
                order.changeStatus("In progress");
                order.getSelect().setSelected(false);
            } }*/
    }

    public void declineTransportButtonOnAction(){ //rejection of the order by transport provider
        //TODO - ustawienie odpowiednich pól obiektu sesji
       /* ObservableList<User> selectedList = FXCollections.observableArrayList();
        for (User order : users){
            if(order.getSelect().isSelected()){
                // Backend to do
                // Changing the status of order
                order.changeStatus("Declined");
                order.getSelect().setSelected(false);
            } }*/
    }

    // Pending orders tab (3)
    public void pendingOrdersButtonOnAction() throws IOException { //change tab to "Pending orders"
        // Set up the style
        pendingOrdersTable.getStylesheets().add("sample/styling/tableView.css");
        pendingOrdersTable.getStyleClass().add("tableview");
        checkBoxTransportColumn.setStyle("-fx-alignment: center;");

        tabText.setText("Pending orders");
        pendingOrdersPage.toFront();

        // Backend to do
        // Displaying in table only orders with state to accept by transport provider
        // (order/route ID, client name, from (city), to = destination (city), order state = to accept)

        checkBoxTransportColumn.setCellValueFactory(new PropertyValueFactory<ProductOrder, CheckBox>("select"));
        /*idPendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        clientNamePendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fromPendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        toPendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        statePendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());*/


        idPendingOrdersColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()));
        clientNamePendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));
        fromPendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getClientCity().getCityName()));
        //Dwa razy miasto klienta - na razie, bo ciężko ogarnąć wszystkie fabryki zamówienia
        toPendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getClientCity().getCityName()));
        statePendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));

        //users = FXCollections.observableArrayList(UsersRepository.getUsers());
        users = FXCollections.observableArrayList();

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<ProductOrder> filteredData = new FilteredList<>(users, b -> true);
        currentOrderSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(user.getOrderId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getOrderClient().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getOrderStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<ProductOrder> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());

        //  Setting the table
        pendingOrdersTable.setItems(sortedData);
    }

    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
        currentOrderSearchField.setText("");
    }

}
