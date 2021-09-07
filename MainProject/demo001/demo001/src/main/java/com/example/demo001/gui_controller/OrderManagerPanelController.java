package com.example.demo001.gui_controller;

import com.example.demo001.Cipher;
import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.gui_controller.ConfirmationBoxController;
import com.example.demo001.service.BasicUserService;
import com.example.demo001.service.ProductOrderService;
import com.example.demo001.service.ProductionAbilityService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
@FxmlView("orderManagerPanel.fxml")
public class OrderManagerPanelController implements Initializable {

    // Changing the tabs
    @FXML
    private Label tabText;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane myAccountPage, /*newOfferPage,*/ summaryNewOfferPage;
    @FXML
    public GridPane newOfferPage;
    @FXML
    private BorderPane manageUserPage, createOfferPage;
    @FXML
    private Button myAccountButton, manageOffersButton, createOfferButton, historyButton;

    // My account tab
    @FXML
    private TextField accountNameField;

    @FXML
    private PasswordField accountPasswordField;

    @FXML
    private Button changeDetailsButton, saveDetailsButton, myLoginDataButton;

    // Manage offers tab
    private ObservableList <ProductOrder> users = null;
    @FXML
    private TableView <ProductOrder> usersTable;
    @FXML
    private TableColumn <ProductOrder, Number> orderIDColumn;
    @FXML
    private TableColumn <ProductOrder, String> statusOrderColumn;
    @FXML
    private TableColumn <ProductOrder, String> clientOrderColumn;
    @FXML
    private TableColumn <ProductOrder, String> transportProviderOrderColumn;
    @FXML
    private Button orderDetailsButton;
    @FXML
    private TextField searchField;

    // Create new offer tab
    // Main view
    @FXML
    private TableView<ProductOrder> createOfferUsersTable;
    @FXML
    private TableColumn <ProductOrder, Number>  createOfferOrderIDColumn;
    @FXML
    private TableColumn <ProductOrder, String> createOfferStatusOrderColumn;
    @FXML
    private TableColumn <ProductOrder, String> createOfferClientOrderColumn;
    @FXML
    private TableColumn <ProductOrder, String> createOfferTransportProviderOrderColumn;
    @FXML
    private TextField createOfferSearchField;
    @FXML
    private Button createOfferOrderDetailsButton;

    // Offer for order
    private ProductOrder selectedOrder = null;
    private ObservableList<OrderItem> productsArray = null;

    @FXML
    private TableView<OrderItem>productsTable;
    @FXML
    private TableColumn<OrderItem, Number> idProductColumn;
    @FXML
    private TableColumn<OrderItem, String> productColumn;
    @FXML
    private TableColumn<OrderItem, Number> amountOfProductColumn;
    @FXML
    private TableColumn factoryColumn;
    @FXML
    private TextField idOrderField, statusOrderField, clientOrderField, transportProviderOrderField;
    @FXML
    private Button summaryOfferButton, goBackToCategoryButton;
    @FXML
    private ComboBox<String> transportProviderCombobox;

    // Summary offer
    @FXML
    private TextField summaryIdOrderField, summaryCompanyField, summaryAmountField, summaryTransportProviderField;
    @FXML
    private Button summarySendOfferButton, summaryGoBackToCategoryButton;
    @FXML
    private TableView<OrderItem> summaryProductsTable;
    @FXML
    private TableColumn<OrderItem, Number>  summaryIdProductColumn;
    @FXML
    private TableColumn<OrderItem, String> summaryProductColumn;
    @FXML
    private TableColumn<OrderItem, Integer> summaryAmountOfProductColumn;
    @FXML
    private TableColumn<OrderItem, String>summaryFactoryColumn;

    //transport providers for order
    private List<TransportProvider> transportProviders;


    //names of transport providers for order
    private ObservableList<String> transportProvidersNames;

    //chosen transport provider
    private TransportProvider selectedTransportProvider;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private ProductionAbilityService productionAbilityService;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(NavigationController.orderScreenToFront==1)
            myAccountButtonOnAction();
        else if(NavigationController.orderScreenToFront==2){

        }
        else
        {
            setUpNewOfferPage();


        }
    }

    private Cipher cipher = new Cipher();

    //NOWOSC
    public void setOrderManagerDetails(BasicUser user) {
        // Backend to do
        // Getting details about order manager
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
        setUserDetails();
        myAccountPage.toFront();
    }

    public void changeDetailsButtonOnAction() {
        accountNameField.setEditable(true);
        accountPasswordField.setEditable(true);
        accountPasswordField.setText("");
        accountPasswordField.setPromptText("New Password");
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

        String enc="";
        if(!newPassword.isEmpty())
        {
            enc=cipher.encrypt(newPassword);
        }

        if(changeDetails)
        {
            NavigationController.username=newUsername;
            user.setUserName(newUsername);
            if(!enc.equals(""))
            {
                user.setUserPassword(enc);
            }
            basicUserService.saveChangedUser(user);
            accountNameField.setEditable(false);
            accountPasswordField.setEditable(false);
            changeDetailsButton.toFront();
            NavigationController.alertText="Details changed successfully";
        }
        callAlertBox();
    }


    public void callAlertBox(){
        NavigationController.lastSceneName=NavigationController.stage.getTitle();
        NavigationController.lastScene = NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(AlertBoxController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Alert!");
        NavigationController.stage.show();
    }
    ////////////////////////////////////////////////////

    // Manager offers tab
    public void manageOffersButtonOnAction() throws IOException {
        // Set up the style
        usersTable.getStylesheets().add("sample/styling/tableView.css");
        usersTable.getStyleClass().add("tableview");
        tabText.setText("Manage offers");
        manageUserPage.toFront();

        orderIDColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderClient().getUserId()));
        statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));
        clientOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));
        transportProviderOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderTransportProvider().getUserName()));

        //get all orders that have been issued from base
        users = FXCollections.observableArrayList(productOrderService.findAllHistoricOrders());

         orderDetailsButton.disableProperty().bind(Bindings.isNull (
                usersTable.getSelectionModel().selectedItemProperty()));  // ODKOMENTOWAĆ

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<ProductOrder> filteredData = new FilteredList<>(users, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(user.getOrderClient().getUserId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getOrderClient().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
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

    /*
    private void getIssuedOrders()
    {
        if(users == null)
            users = FXCollections.observableArrayList(productOrderService.findOrdersForManagements());
    }*/

    public void orderDetailsButtonOnAction() throws IOException {

        NavigationController.orderDetailsType = true;
        NavigationController.selectedOrder = usersTable.getSelectionModel().getSelectedItem();
        NavigationController.orderID = NavigationController.selectedOrder.getOrderId();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(OrderDetailsController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Order details");
        stage.show();
    }

    public void createOfferOrderDetailsButtonOnAction() throws IOException {

        NavigationController.orderDetailsType = false;
        NavigationController.createOffer = false;
        //////////////////////TU ZMIANA BO NIE DZIAŁAŁO
        //NavigationController.selectedOrder = this.productOrderService.findOrderByID(createOfferUsersTable.getSelectionModel().getSelectedItem().getOrderId());
        NavigationController.selectedOrderId = createOfferUsersTable.getSelectionModel().getSelectedItem().getOrderId();
        NavigationController.selectedOrder = createOfferUsersTable.getSelectionModel().getSelectedItem();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(OrderDetailsController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create offer");
        stage.show();
    }

    // Create offer tab
    public void createOfferButtonOnAction() throws IOException {
        // Set up the style
        createOfferUsersTable.getStylesheets().add("sample/styling/tableView.css");
        createOfferUsersTable.getStyleClass().add("tableview");
        tabText.setText("Create a offer");
        createOfferPage.toFront();

        createOfferOrderIDColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderClient().getUserId()));
        createOfferStatusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));
        createOfferClientOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));
        //createOfferTransportProviderOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderTransportProvider().getUserName()));

        ////users = FXCollections.observableArrayList(UsersRepository.getUsers());
        users = FXCollections.observableArrayList(productOrderService.findOrdersForManagements());


        createOfferOrderDetailsButton.disableProperty().bind(Bindings.isNull (
                createOfferUsersTable.getSelectionModel().selectedItemProperty()));

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
         FilteredList<ProductOrder> filteredData = new FilteredList<>(users, b -> true);
        createOfferSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(user.getOrderClient().getUserId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getOrderClient().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<ProductOrder> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(createOfferUsersTable.comparatorProperty());

        //  Setting the table
        createOfferUsersTable.setItems(sortedData);

    }

    public void setUpNewOfferPage (){
        tabText.setText("Set up the offer");
        summaryGoBackToCategoryButton.setDisable(false);
        newOfferPage.toFront();


        NavigationController.selectedOrder = this.productOrderService.findOrderByID(NavigationController.selectedOrderId);


        idOrderField.setText(Long.toString(NavigationController.selectedOrder.getOrderId()));
        statusOrderField.setText(NavigationController.selectedOrder.getOrderStatus().toString());
        clientOrderField.setText(NavigationController.selectedOrder.getOrderClient().toString());
        transportProviderOrderField.setText(NavigationController.selectedOrder.getOrderTransportProvider().toString());

        // Backend to do
        // Normally taken the list of product for selected order - sth similiary for check already done
        productsArray = FXCollections.observableArrayList(NavigationController.selectedOrder.getOrderedProducts());
        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStyleClass().add("tableview");

        idProductColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getProduct().getProductId()));
        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        amountOfProductColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductAmount()));

        Callback<TableColumn <OrderItem, String>, TableCell <OrderItem, String>> cellFactory = (param) -> {
            final TableCell<OrderItem, String> cell = new TableCell<OrderItem, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        OrderItem product = getTableView().getItems().get(getIndex());
                        ComboBox comboBox = new ComboBox();
                        comboBox.setMinWidth(170);
                        comboBox.setMinHeight(25);
                        // Backend to do
                        // It has to be for changes if someone is going back from summary !
                        //ustawienie nazwy fabryki dla danego pola
                        comboBox.setValue(currentFactory(product));

                        // Backend to do
                        // Depending from the product different list in combobox is given
                        // Now all products has the same one almost

                        //pobranie fabryk dla danego produktu
                        comboBox.setItems(FXCollections.observableArrayList(productionAbilityService
                                .searchAvailableFactories(product).stream().map(Factory::getFactoryName).collect(Collectors.toList())));
                        /*if (product.getId() == 1){ comboBox.setItems(FXCollections.observableArrayList("Maniek", "Zgrana Spółka"));}
                        else { comboBox.setItems(FXCollections.observableArrayList("Mrówka", "Zespół Pomocnik"));}*/

                        comboBox.setOnAction(event -> {
                            // Backend to do
                            // Changing the company for selected one
                            product.setFactory((Factory) comboBox.getValue());
                        });
                        setGraphic(comboBox);
                        setText(null);
                    }
                };
            };
            return cell;
        };
        factoryColumn.setCellFactory(cellFactory);
        productsTable.setItems(productsArray);

        transportProviderCombobox.getStylesheets().add("sample/styling/comboBoxView.css");
        // Careful all the time first setValue, then setItems - otherwise it not gonna be correct
        transportProviderCombobox.setValue(currentTransport(NavigationController.selectedOrder));
        // Backend to do
        // Getting available transport

        //get transport providers for selected order
        transportProviders = possibleTransportProviders(NavigationController.selectedOrder);

        //extract the names of transport providers for selected order
        transportProvidersNames = FXCollections.observableArrayList(transportProviders.stream()
                .map(TransportProvider::getUserName).collect(Collectors.toList()));

        transportProviderCombobox.setItems(transportProvidersNames);

        transportProviderCombobox.setOnAction(event -> {
            // Backend to do
            // Setting chosen transport

            //set chosen TransportProvider object from session TransportProvider's list
            //for selected Order
            NavigationController.selectedOrder.setOrderTransportProvider(transportProviders.stream()
                    .filter(transportProvider -> transportProvider.getUserName().equals(transportProviderCombobox.getValue()))
                    .findFirst().get());
        });

        if(NavigationController.result){
            NavigationController.result = false;
            // Backend
            // Deleting order from preparing list to do
            //users.remove(NavigationController.selectedOrder);
            tabText.setText("Create a offer");
            createOfferPage.toFront();
            try{createOfferButtonOnAction();}
            catch(IOException e){}
        }
    }

    private String currentFactory(OrderItem orderItem)
    {
        if(orderItem.getFactory() == null)
        {
            return "No factory set";
        }
        return orderItem.getFactory().getFactoryName();
    }

    private String currentTransport(ProductOrder productOrder)
    {
        if(productOrder.getOrderTransportProvider() == null)
        {
            return "No transport provider set";
        }
        return productOrder.getOrderTransportProvider().getUserName();
    }

    private List<TransportProvider> possibleTransportProviders(ProductOrder productOrder){
        return new ArrayList<>();
    }

    public void summaryOfferButtonOnAction(){
        // Backend to do
        tabText.setText("Summary offer");
        summaryGoBackToCategoryButton.setDisable(false);
        summaryNewOfferPage.toFront();
        //summaryIdOrderField.setText(Long.toString(NavigationController.selectedOrder.getOrderId()));
        //summaryCompanyField.setText(NavigationController.selectedOrder.getOrderClient().getUserName());
        ////summaryAmountField.setText(NavigationController.selectedOrder.getSurname());
        //summaryTransportProviderField.setText(NavigationController.selectedOrder.getOrderTransportProvider().getUserName());

        summaryProductsTable.getStylesheets().add("sample/styling/tableView.css");
        summaryProductsTable.getStyleClass().add("tableview");

        //summaryIdProductColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getProduct().getProductId()));
        //summaryProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        ////summaryAmountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().surnameProperty());
        //summaryFactoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
        //productsArray = FXCollections.observableArrayList(NavigationController.selectedOrder.getOrderedProducts());
        //summaryProductsTable.setItems(productsArray);
    }

    /**
     * Method for resetting choices for Factories for ordered items
     * and for TransportProvider
     */
    public void goBackButtonOnAction(){
        // Backend to do
        // Clearing out after changing the tab back
        //for (OrderItem orderItem : NavigationController.selectedOrder.getOrderedProducts())
        //orderItem.setFactory(null);
        //NavigationController.selectedOrder.setOrderTransportProvider(null);
        tabText.setText("Create a offer");
        summaryGoBackToCategoryButton.setVisible(true);
        createOfferPage.toFront();//create na new?
    }

    public void summaryGoBackButtonOnAction() {
        newOfferPage.toFront();
        setUpNewOfferPage();
    }

    public void summarySendOfferButtonOnAction() throws IOException {
        NavigationController.summarySendOffer = true;
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(ConfirmationBoxController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }



    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
        createOfferSearchField.setText("");
    }

    //TODO - this shouldn't exist - only for complying with FXML
    public void deleteAccountButtonOnAction(ActionEvent actionEvent) {
    }

}