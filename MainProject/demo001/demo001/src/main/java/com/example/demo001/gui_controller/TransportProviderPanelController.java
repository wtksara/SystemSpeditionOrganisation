package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.OrderManagement.OrderStatus;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.service.BasicUserService;
import com.example.demo001.service.ProductOrderService;
import javafx.beans.binding.Bindings;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ObservableList <ProductOrder> ordersTable = null;
    @FXML
    private TableView <ProductOrder> actualOrdersView;
    @FXML
    private TableColumn <ProductOrder, Number> idColumn;
    @FXML
    private TableColumn <ProductOrder, String> clientNameColumn;
    @FXML
    private TableColumn <ProductOrder, String> toColumn;
    @FXML
    private TableColumn <ProductOrder, String> stateColumn;
    @FXML
    private TextField searchField;
    @FXML
    private TextField currentOrderSearchField;
    @FXML
    private Button orderDeliveredButton;

    // Pending orders tab (3) - orders pending approval or rejection by transport provider
    @FXML
    private TableView <ProductOrder> pendingOrdersView;
    @FXML
    private TableColumn <ProductOrder, Number> idPendingOrdersColumn;
    @FXML
    private TableColumn <ProductOrder, String> clientNamePendingOrdersColumn;
    @FXML
    private TableColumn <ProductOrder, String> toPendingOrdersColumn;
    @FXML
    private TableColumn <ProductOrder, String> statePendingOrdersColumn;
    @FXML
    private Button acceptTransportButton, declineTransportButton;
    @FXML
    private TextField searchField2;

    @Autowired
    private BasicUserService basicUserService;
    @Autowired
    private ProductOrderService productOrderService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //To be (maybe) changed later
        changeDetailsButton.setVisible(false);
        saveDetailsButton.setVisible(false);

        setUserDetails();
        switch(NavigationController.transportProviderScreenToFront)
        {
            case 2:
                try{NavigationController.transportProviderScreenToFront=1;myRouteButtonOnAction();}
                catch(IOException e){}
                break;
            case 3:
                try{NavigationController.transportProviderScreenToFront=1;pendingOrdersButtonOnAction();}
                catch(IOException e){}
                break;
        }
    }


    public void setUserDetails()
    {
        BasicUser user = basicUserService.findByUsername(NavigationController.username);
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
        setUserDetails();
        myAccountPage.toFront();
        NavigationController.transportProviderScreenToFront=1;
    }

    public void changeDetailsButtonOnAction() { //to allow the user change the data
        accountNameField.setEditable(true);
        accountPasswordField.setEditable(true);
        saveDetailsButton.toFront();
    }

    public void saveDetailsButtonOnAction() throws IOException { //saving the data changed by user
        accountNameField.setEditable(false);
        accountPasswordField.setEditable(false);
        changeDetailsButton.toFront();
    }

    // My route tab (2)
    public void myRouteButtonOnAction() throws IOException { //change tab to "My route"
        // Set up the style
        actualOrdersView.getStylesheets().add("sample/styling/tableView.css");
        actualOrdersView.getStyleClass().add("tableview");
        tabText.setText("My route");
        myRoutePage.toFront();

        orderDeliveredButton.disableProperty().bind(Bindings.isNull (
                actualOrdersView.getSelectionModel().selectedItemProperty()));

        // Backend to do
        // Displaying in table all orders of the transport provider
        // (order/route ID, client name, from (city), to = destination (city), order state)

        ordersTable = FXCollections.observableArrayList(productOrderService.findActualOrdersByTransportProvider(NavigationController.username));

        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()));
        clientNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));
        toColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getClientCity().getCityName()));
        stateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<ProductOrder> filteredData = new FilteredList<>(ordersTable, b -> true);
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
        SortedList<ProductOrder> sortedData = new SortedList<ProductOrder>(filteredData);
        sortedData.comparatorProperty().bind(actualOrdersView.comparatorProperty());

        //  Setting the table
        actualOrdersView.setItems(sortedData);
    }

    public void acceptTransportButtonOnAction(){ //acceptance of the order by transport provider
        ProductOrder selectedOrder = pendingOrdersView.getSelectionModel().getSelectedItem();
        selectedOrder.setOrderStatus(OrderStatus.OFFER_SENT);
        productOrderService.saveChangedOrder(selectedOrder);
        NavigationController.transportProviderScreenToFront=3;
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(TransportProviderPanelController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Spedition Organisation System - Transport Provider");
        NavigationController.stage.show();
    }

    public void declineTransportButtonOnAction(){ //rejection of the order by transport provider
        ProductOrder selectedOrder = pendingOrdersView.getSelectionModel().getSelectedItem();
        selectedOrder.setOrderStatus(OrderStatus.LKN_4_TP);
        productOrderService.saveChangedOrder(selectedOrder);
        NavigationController.transportProviderScreenToFront=3;
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(TransportProviderPanelController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Spedition Organisation System - Transport Provider");
        NavigationController.stage.show();
    }

    // Pending orders tab (3)
    public void pendingOrdersButtonOnAction() throws IOException { //change tab to "Pending orders"
        // Set up the style
        pendingOrdersView.getStylesheets().add("sample/styling/tableView.css");
        pendingOrdersView.getStyleClass().add("tableview");

        tabText.setText("Pending orders");
        pendingOrdersPage.toFront();

        //pobieranie danych
        ordersTable = FXCollections.observableArrayList(productOrderService.findPendingOrdersForTransportProviderByTransportProvider(NavigationController.username));
        //ordersTable = FXCollections.observableArrayList();
        acceptTransportButton.disableProperty().bind(Bindings.isNull (
                pendingOrdersView.getSelectionModel().selectedItemProperty()));
        declineTransportButton.disableProperty().bind(Bindings.isNull(
                pendingOrdersView.getSelectionModel().selectedItemProperty()));

        // Backend to do
        // Displaying in table only orders with state to accept by transport provider
        // (order/route ID, client name, from (city), to = destination (city), order state = to accept)

        /*idPendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        clientNamePendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fromPendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        toPendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        statePendingOrdersColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());*/


        idPendingOrdersColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()));
        clientNamePendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));
        //fromPendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getClientCity().getCityName()));
        //Dwa razy miasto klienta - na razie, bo ciężko ogarnąć wszystkie fabryki zamówienia
        toPendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getClientCity().getCityName()));
        statePendingOrdersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<ProductOrder> filteredData = new FilteredList<>(ordersTable, b -> true);
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
        sortedData.comparatorProperty().bind(pendingOrdersView.comparatorProperty());

        //  Setting the table
        pendingOrdersView.setItems(sortedData);
    }

    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
        currentOrderSearchField.setText("");
    }

    public void callAlertBox(){
        NavigationController.lastSceneName="Spedition Organisation System - Transport Provider";
        NavigationController.lastScene = NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(AlertBoxController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Alert!");
        NavigationController.stage.show();
    }

    public void markAsDelivered(){ //mark order as delivered
        ProductOrder selectedOrder = actualOrdersView.getSelectionModel().getSelectedItem();
        NavigationController.alertText="Are you sure you want to mark this order as delivered?";
        NavigationController.orderID = selectedOrder.getOrderId();
        NavigationController.lastSceneName=NavigationController.stage.getTitle();
        NavigationController.lastScene=NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(MarkAsDeliveredConfirmationBoxController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Confirm marking as delivered");
        NavigationController.stage.show();
    }
}
