package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.gui_controller.ConfirmationBoxController;
import com.example.demo001.service.BasicUserService;
import com.example.demo001.service.FactoryForOrderItemSetup;
import com.example.demo001.service.ProductOrderService;
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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
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
    private GridPane myAccountPage, newOfferPage, summaryNewOfferPage;
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
    private Button changeDetailsButton, saveDetailsButton;

    // Manage offers tab
    private ObservableList <ProductOrder> users = null;
    @FXML
    private TableView <ProductOrder> usersTable;
    @FXML
    private TableColumn <ProductOrder, Number> idColumn;
    @FXML
    private TableColumn <ProductOrder, String> nameColumn;
    @FXML
    private TableColumn <ProductOrder, String> surnameColumn;
    @FXML
    private Button orderDetailsButton;
    @FXML
    private TextField searchField;

    // Create new offer tab
    // Main view
    @FXML
    private TableView<ProductOrder> createOfferUsersTable;
    @FXML
    private TableColumn <ProductOrder, Number>  createOfferIdColumn;
    @FXML
    private TableColumn <ProductOrder, String> createOfferNameColumn;
    @FXML
    private TableColumn <ProductOrder, String> createOfferSurnameColumn;
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
    private TextField idOrderField, companyField, amountField;
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
    private FactoryForOrderItemSetup factoryForOrderItemSetup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

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

    // Manager offers tab
    public void manageOffersButtonOnAction() throws IOException {
        // Set up the style
        usersTable.getStylesheets().add("sample/styling/tableView.css");
        usersTable.getStyleClass().add("tableview");
        tabText.setText("Manage offers");
        manageUserPage.toFront();

        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderClient().getUserId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));

        //get all orders that have been issued from base
        users = FXCollections.observableArrayList(productOrderService.findOrdersForManagements());

        orderDetailsButton.disableProperty().bind(Bindings.isNull (
                usersTable.getSelectionModel().selectedItemProperty()));

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
        ProductOrder selectedOrder = usersTable.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../orderDetails.fxml"));
        // Two types of showing orders details - true without editing, false - editing
        new OrderDetailsController().init(fxmlLoader, selectedOrder, "Order Details",true);
    }

    public void createOfferOrderDetailsButtonOnAction() throws IOException {
        ProductOrder selectedOrder = createOfferUsersTable.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../orderDetails.fxml"));
        Optional<ButtonType> isConfirmed = new OrderDetailsController().init(fxmlLoader, selectedOrder, "Order Details",false);
        if(isConfirmed.get() == ButtonType.OK) {
            // Backend to do
            // Setting a order, which will changed now - can be done in different way
            this.selectedOrder = selectedOrder;
            newOfferPage.toFront();
            setUpNewOfferPage();
        }
    }

    // Create offer tab
    public void createOfferButtonOnAction() throws IOException {
        // Set up the style
        createOfferUsersTable.getStylesheets().add("sample/styling/tableView.css");
        createOfferUsersTable.getStyleClass().add("tableview");
        tabText.setText("Create a offer");
        createOfferPage.toFront();

        createOfferIdColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderClient().getUserId()));
        createOfferNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderClient().getUserName()));

        //users = FXCollections.observableArrayList(UsersRepository.getUsers());
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
        idOrderField.setText(Long.toString(selectedOrder.getOrderId()));
        companyField.setText(selectedOrder.getOrderClient().getUserName());
        amountField.setText(selectedOrder.getOrderStatus().toString());

        // Backend to do
        // Normally taken the list of product for selected order - sth similiary for check already done
        productsArray = FXCollections.observableArrayList(selectedOrder.getOrderedProducts());
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
                        comboBox.setItems(FXCollections.observableArrayList(factoryForOrderItemSetup
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
        transportProviderCombobox.setValue(currentTransport(selectedOrder));
        // Backend to do
        // Getting available transport

        //get transport providers for selected order
        transportProviders = possibleTransportProviders(selectedOrder);

        //extract the names of transport providers for selected order
        transportProvidersNames = FXCollections.observableArrayList(transportProviders.stream()
                .map(TransportProvider::getUserName).collect(Collectors.toList()));

        transportProviderCombobox.setItems(transportProvidersNames);

        transportProviderCombobox.setOnAction(event -> {
            // Backend to do
            // Setting chosen transport

            //set chosen TransportProvider object from session TransportProvider's list
            //for selected Order
            selectedOrder.setOrderTransportProvider(transportProviders.stream()
                    .filter(transportProvider -> transportProvider.getUserName().equals(transportProviderCombobox.getValue()))
                    .findFirst().get());
        });
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
        summaryNewOfferPage.toFront();
        summaryIdOrderField.setText(Long.toString(selectedOrder.getOrderId()));
        summaryCompanyField.setText(selectedOrder.getOrderClient().getUserName());
        //summaryAmountField.setText(selectedOrder.getSurname());
        summaryTransportProviderField.setText(selectedOrder.getOrderTransportProvider().getUserName());

        summaryProductsTable.getStylesheets().add("sample/styling/tableView.css");
        summaryProductsTable.getStyleClass().add("tableview");

        summaryIdProductColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getProduct().getProductId()));
        summaryProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        //summaryAmountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().surnameProperty());
        summaryFactoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
        productsArray = FXCollections.observableArrayList(selectedOrder.getOrderedProducts());
        summaryProductsTable.setItems(productsArray);
    }

    /**
     * Method for resetting choices for Factories for ordered items
     * and for TransportProvider
     */
    public void goBackButtonOnAction(){
        // Backend to do
        // Clearing out after changing the tab back
        for (OrderItem orderItem : selectedOrder.getOrderedProducts())
            orderItem.setFactory(null);
        selectedOrder.setOrderTransportProvider(null);
        tabText.setText("Create a offer");
        createOfferPage.toFront();
    }

    public void summaryGoBackButtonOnAction() {
        newOfferPage.toFront();
        setUpNewOfferPage();
    }

    public void summarySendOfferButtonOnAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../confirmationBox.fxml"));
        Optional<ButtonType> isConfirmed = new ConfirmationBoxController().createConfirmation(fxmlLoader, "Are you sure you would like to send that offer?", "Send offer","Ok", "Cancel");
        if(isConfirmed.get() == ButtonType.OK) {
            // Backend
            // Deleting order from preparing list to do
            users.remove(selectedOrder);
            createOfferPage.toFront();
        }
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