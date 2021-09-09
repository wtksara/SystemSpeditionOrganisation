package com.example.demo001.gui_controller;

import com.example.demo001.Cipher;
import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.OrderStatus;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;
import com.example.demo001.service.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@SuppressWarnings("rawtypes")
@Component
@FxmlView("clientPanel.fxml")
public class ClientPanelController implements Initializable {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private OrderItemService orderItemService;

    private List<Product> products;
    private List<ProductOrder> orders = new ArrayList<>();
    private HashMap<Product, Integer> cart = new HashMap();
    private List<OrderWrapperController> pricedOrders = new ArrayList<>();
    List<OrderItem> orderedItems = new ArrayList<>();

    // All tableview has to change depending from the data which will be in table

    // Changing the tabs
    @FXML
    private Label tabText;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane myAccountPage ,categoryPage , productsListPage;
    @FXML
    private BorderPane currentOrdersPage, historyPage;

    // My account tab
    @FXML
    private TextField accountNameField;

    @FXML
    private PasswordField accountPasswordField;

    @FXML
    private Button changeDetailsButton, saveDetailsButton;

    // Create order tab
    @FXML
    private Button categoryButton1,categoryButton2,categoryButton3,categoryButton4,categoryButton5,categoryButton6,categoryButton7,categoryButton8;
    @FXML
    private TableView <Product> createOrderTable;
    @FXML
    private TableColumn <Product, String> createOrderItemColumn;
    @FXML
    private TableColumn addToBasketButtonColumn;

    // Current orders tab
    @FXML
    private TableView <OrderWrapperController> currentOrdersTable;
    @FXML
    private TableColumn <OrderWrapperController, Number> currentOrderIdColumn;
    @FXML
    private TableColumn <OrderWrapperController, String> currentOrderAmountColumn;
    @FXML
    private TableColumn <OrderWrapperController, String> currentOrderStatusColumn;
    @FXML
    private TextField currentOrderSearchField;
    @FXML
    private Button currentOrderDetailsButton;

    // History tab
    @FXML
    private TableView <OrderWrapperController> historyTable;
    @FXML
    private TableColumn <OrderWrapperController, Number> idOrderColumn;
    @FXML
    private TableColumn <OrderWrapperController, String> costOrderColumn;
    @FXML
    private TableColumn <OrderWrapperController, String> statusOrderColumn;
    @FXML
    private TextField historySearchField;
    @FXML
    private Button orderDetailsButton;

    @Autowired
    private BasicUserService basicUserService;

    private final Cipher cipher = new Cipher();

    @Autowired
    public ClientPanelController(ProductService productService, ProductOrderService productOrderService, OrderItemService orderItemService){
        this.productService = productService;
        this.productOrderService = productOrderService;
        this.orderItemService = orderItemService;
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(NavigationController.basketResult){
            NavigationController.basketResult = false;
            //creating and saving order to db
            ////place order
            Client client = clientService.findByUsername(NavigationController.username);
            ProductOrder productOrder = new ProductOrder(client);
            this.productOrderService.createOrder(productOrder);
            ////create order item list
            List<OrderItem> orderedItems = new ArrayList();
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                OrderItem orderItem = new OrderItem(productOrder, entry.getKey(), entry.getValue());
                this.orderItemService.CreateOrderItem(orderItem);
                orderedItems.add(orderItem);
            }
            ////save ordered items to order
            productOrder.setOrderedProducts(orderedItems);
            this.productOrderService.modifyOrder(productOrder);

            // Cleaning the order
            cart.clear();
            myAccountButtonOnAction();
        } else {
            if(NavigationController.emptyBasket){
                NavigationController.emptyBasket = false;
                cart.clear();
            }
            if(NavigationController.orderScreenToFrontClient==1)
                myAccountButtonOnAction();
            else if(NavigationController.orderScreenToFrontClient==3){
                currentOrdersButtonOnAction();
            }
            else if(NavigationController.orderScreenToFrontClient==4){
                historyButtonOnAction();
            }
        }
    }

    public void setUserDetails() {
        BasicUser user = basicUserService.findByUsername(NavigationController.username);
        accountNameField.setText(user.getUserName());
        accountPasswordField.setText(user.getUserPassword());
    }

    // My account page
    // That details are the same for all client Panel for now.
    // It can be taken out to the main class, but it depends if details are gonna be the same. It is up to you.
    public void myAccountButtonOnAction() throws IOException {
        // Setting the color of te top vBox for more dynamic view
        if (!cart.isEmpty()) {
            NavigationController.notFinishedOrder = true;
            NavigationController.orderScreenToFrontClient = 1;
            notFinishedOrder();
        }
        else { setUpMyAccountPage();}
    }

    private void setUpMyAccountPage(){
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

    public void saveDetailsButtonOnAction() throws IOException {

        String newUsername = accountNameField.getText();
        String newPassword = accountPasswordField.getText();
        String oldUsername = NavigationController.username;
        BasicUser user = basicUserService.findByUsername(oldUsername);
        boolean changeDetails = true;

        if(!newUsername.equals(oldUsername)) {
            BasicUser user2 = basicUserService.findByUsername(newUsername);
            if (user2!=null) {
                NavigationController.alertText="Username taken, choose another username";
                changeDetails=false;
            }
            else if(newUsername.isEmpty()) {
                NavigationController.alertText="Username can not be empty";
                changeDetails=false;
            }
        }

        String enc="";
        if(!newPassword.isEmpty()) {
            enc=cipher.encrypt(newPassword);
        }

        if(changeDetails) {
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
        changeDetailsButton.toFront();
        callAlertBox();
    }

    public void callAlertBox(){
        NavigationController.lastSceneName="Spedition Organisation System - Client";
        NavigationController.lastScene = NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(AlertBoxController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Alert!");
        NavigationController.stage.show();
    }

    // Create order tab
    public void createOrderButtonOnAction() {
        // Clear
        clearFilters();
        tabText.setText("Category");
        categoryPage.toFront();
    }

    @FXML
    public void categoryButtonOnAction(ActionEvent event)
    {
        if (event.getSource() instanceof Button) {
            //when category is chosen a list of products of this type is downloaded from database
            Button btnClicked = (Button) event.getSource();
            if (btnClicked == categoryButton1) {
                tabText.setText(ProductType.HARDWARE.toString());
                products = productService.FindProductsByProductType(ProductType.HARDWARE);
            }
            else if (btnClicked == categoryButton2) {
                tabText.setText(ProductType.TOOLS.toString());
                products = productService.FindProductsByProductType(ProductType.TOOLS);
            }
            else if (btnClicked == categoryButton3) {
                tabText.setText(ProductType.WOOD.toString());
                products = productService.FindProductsByProductType(ProductType.WOOD);
            }
            else if (btnClicked == categoryButton4) {
                tabText.setText(ProductType.FLOORING.toString());
                products = productService.FindProductsByProductType(ProductType.FLOORING);
            }
            else if (btnClicked == categoryButton5) {
                tabText.setText(ProductType.TILES.toString());
                products = productService.FindProductsByProductType(ProductType.TILES);
            }
            else if (btnClicked == categoryButton6) {
                tabText.setText(ProductType.PAINT.toString());
                products = productService.FindProductsByProductType(ProductType.PAINT);
            }
            else if (btnClicked == categoryButton7) {
                tabText.setText(ProductType.WALLPAPERS.toString());
                products = productService.FindProductsByProductType(ProductType.WALLPAPERS);
            }
            else if (btnClicked == categoryButton8) {
                tabText.setText(ProductType.LIGHTING.toString());
                products = productService.FindProductsByProductType(ProductType.LIGHTING);
            }
        }
        // Set up the style
        createOrderTable.getStylesheets().add("sample/styling/tableView.css");
        createOrderTable.getStylesheets().add("sample/styling/buttomView.css");
        createOrderTable.getStyleClass().add("tableview");
        addToBasketButtonColumn.setStyle( "-fx-alignment: center;");

        createOrderItemColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));

        Callback<TableColumn <Product, String>, TableCell <Product, String>> cellFactory = (param) -> {
            final TableCell<Product, String> cell = new TableCell<Product, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        // Set view of the button
                        final Button addButton = new Button("Add to cart");
                        addButton.setMinWidth(100);
                        addButton.setMinHeight(25);
                        addButton.getStyleClass().add("buttom");

                        addButton.setOnAction(event -> {
                            NavigationController.addProduct = true;
                            NavigationController.cart = cart;
                            NavigationController.product = getTableView().getItems().get(getIndex());

                            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                            Parent root = fxWeaver.loadView(ProductFormController.class);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setTitle("Add a product");
                            stage.setScene(scene);
                            stage.show();
                        });
                        setGraphic(addButton);
                    }
                    setText(null);
                }
            };
            return cell;
        };
        addToBasketButtonColumn.setCellFactory(cellFactory);
        createOrderTable.setItems(FXCollections.observableArrayList(products));
        productsListPage.toFront();
    }

    public void basketButtonOnAction() {

        if(!NavigationController.result) {
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(BasketController.class);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Basket");
            NavigationController.stage = stage; //zapis sceny otwieranego koszyka
            stage.show();
        }
    }

    // Current orders tab
    public void currentOrdersButtonOnAction() {
        // Clear
        if (!cart.isEmpty()) {
            NavigationController.notFinishedOrder = true;
            NavigationController.orderScreenToFrontClient = 3;
            notFinishedOrder();
        }
        else {
            pricedOrders.clear();
            setUpCurrentOrdersPage();
        }
    }

    public void setUpCurrentOrdersPage(){
        clearFilters();
        // Set up the style
        currentOrdersTable.getStylesheets().add("sample/styling/tableView.css");
        currentOrdersTable.getStyleClass().add("tableview");

        Client client = clientService.findByUsername(NavigationController.username);
        orders = productOrderService.findCurrentOrders(client);

        for (ProductOrder order : orders) {
            double price = 0;
            orderedItems = orderItemService.FindOrderItemsByOrder(order.getOrderId());
            for (OrderItem item : orderedItems) {
                price += item.getProductAmount() * item.getProduct().getProductPrize();
            }
            pricedOrders.add(new OrderWrapperController(order, price));
        }

        currentOrderIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getProductOrder().getOrderId()));

        currentOrderAmountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getOrderCost())));
        currentOrderStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductOrder().getOrderStatus().toString()));

        currentOrderDetailsButton.disableProperty().bind(Bindings.isNull(
                currentOrdersTable.getSelectionModel().selectedItemProperty()));

        if(!orders.isEmpty()) {
            // This could also be taken out but it depends if there will be different data in table view.
            // Searching in table view
            FilteredList<OrderWrapperController> filteredData = new FilteredList<OrderWrapperController>(FXCollections.observableArrayList(pricedOrders), b -> true);

            currentOrderSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(order -> {
                    if (newValue == null || newValue.isEmpty()) return true;

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (String.valueOf(order.getProductOrder().getOrderId()).indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (order.getProductOrder().getOrderStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (order.getProductOrder().getOrderTransportProvider().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else
                        return false;
                });
            });
            SortedList<OrderWrapperController> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(currentOrdersTable.comparatorProperty());

            currentOrdersTable.setItems(sortedData);
        }
        tabText.setText("Current orders");
        currentOrdersPage.toFront();
    }


    public void acceptButtonOnAction(){
        OrderWrapperController selectedOrder = currentOrdersTable.getSelectionModel().getSelectedItem();
        if(selectedOrder.getProductOrder().getOrderStatus() == OrderStatus.OFFER_SENT) { //tylko jesli do zaakceptowania
            selectedOrder.getProductOrder().setOrderStatus(OrderStatus.ACCEPTED);
            productOrderService.modifyOrder(selectedOrder.getProductOrder());
        }
    }

    public void declineButtonOnAction(){
        OrderWrapperController selectedOrder = currentOrdersTable.getSelectionModel().getSelectedItem();
        if(selectedOrder.getProductOrder().getOrderStatus() == OrderStatus.OFFER_SENT) { //tylko jesli do zaakceptowania
            selectedOrder.getProductOrder().setOrderStatus(OrderStatus.REJECTED);
            productOrderService.modifyOrder(selectedOrder.getProductOrder());
        }
    }

    // History page
    public void historyButtonOnAction() {
        if (!cart.isEmpty()) {
            NavigationController.notFinishedOrder = true;
            NavigationController.orderScreenToFrontClient = 4;
            notFinishedOrder();
        }
        else {
            pricedOrders.clear();
            setUpHistoryPage();
        }
    }

    private void setUpHistoryPage(){
        clearFilters();

        Client client = clientService.findByUsername(NavigationController.username);
        orders = productOrderService.findHistoricOrders(client);

        for (ProductOrder order : orders) {
            double price = 0;
            orderedItems = orderItemService.FindOrderItemsByOrder(order.getOrderId());
            for (OrderItem item : orderedItems) {
                price += item.getProductAmount() * item.getProduct().getProductPrize();
            }
            pricedOrders.add(new OrderWrapperController(order, price));
        }

        historyTable.getStylesheets().add("sample/styling/tableView.css");
        historyTable.getStyleClass().add("tableview");

        idOrderColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getProductOrder().getOrderId()));

       // costOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getOrderCost())));

        statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductOrder().getOrderStatus().toString()));
        orderDetailsButton.disableProperty().bind(Bindings.isNull(
                historyTable.getSelectionModel().selectedItemProperty()));

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<OrderWrapperController> filteredData = new FilteredList<OrderWrapperController>(FXCollections.observableArrayList(pricedOrders), b -> true);

        historySearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(order.getProductOrder().getOrderId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (order.getProductOrder().getOrderStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (order.getProductOrder().getOrderTransportProvider().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<OrderWrapperController> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(historyTable.comparatorProperty());
        historyTable.setItems(sortedData);

        tabText.setText("History");
        historyPage.toFront();
    }


    public void orderDetailsButtonOnAction() {
        OrderWrapperController selectedOrder;
        if (tabText.getText().equals("Current orders"))
            selectedOrder = currentOrdersTable.getSelectionModel().getSelectedItem();
        else
            selectedOrder = historyTable.getSelectionModel().getSelectedItem();

        NavigationController.selectedOrder=selectedOrder.getProductOrder();
        NavigationController.orderDetailsType = true;
        NavigationController.lastSceneName="Spedition Organisation System - Client";
        NavigationController.lastScene = NavigationController.stage.getScene();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(OrderDetailsController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Order details");
        stage.show();
    }

    // Additional functions
    public void clearFilters(){
        currentOrderSearchField.setText("");
        historySearchField.setText("");
    }

    public void notFinishedOrder() {
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(ConfirmationBoxController.class);
        NavigationController.alertText="";
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Confirmation");
        NavigationController.stage.show();
    }
}