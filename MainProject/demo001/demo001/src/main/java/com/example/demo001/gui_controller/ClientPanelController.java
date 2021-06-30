package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.OrderStatus;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;
import com.example.demo001.domain.Transport.City;
import com.example.demo001.gui_controller.OrderDetailsController;
import com.example.demo001.service.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.*;


@Component
@FxmlView("clientPanel.fxml")
public class ClientPanelController implements Initializable {

    @Autowired
    private BasicUserService clientService;
    @Autowired
    private CityService cityService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private OrderItemService orderItemService;

    private Client client;
    private List<Product> products;
    private List<ProductOrder> orders;
    private HashMap<Product, Integer> cart = new HashMap();

    // All tableview has to change depending from the data which will be in table

    // Changing the tabs
    @FXML
    private Label tabText;
    @FXML
    private VBox vBox;
    @FXML
    private Button myAccountButton, createOrderButton, currentOrdersButton, historyButton;
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
    private ImageView categoryImage1,categoryImage2,categoryImage3,categoryImage4,categoryImage5,categoryImage6,categoryImage7,categoryImage8;
    @FXML
    private Label categoryLabel1, categoryLabel2, categoryLabel3, categoryLabel4, categoryLabel5, categoryLabel6, categoryLabel7, categoryLabel8;
    @FXML
    private Button basketButton , productsListBasketButton, goBackToCategoryButton;
    @FXML
    private TableView <Product> createOrderTable;
    @FXML
    private TableColumn <Product, Number> createOrderIDColumn;
    @FXML
    private TableColumn <Product, String> createOrderItemColumn;
    @FXML
    private TableColumn <Product, String> createOrderCompanyColumn;
    @FXML
    private TableColumn <Product, String> createOrderStatusColumn;
    @FXML
    private TableColumn addToBasketButtonColumn;

    // Current orders tab
    @FXML
    private TableView <ProductOrder> currentOrdersTable;
    @FXML
    private TableColumn <ProductOrder, CheckBox> checkBoxColumn;
    @FXML
    private TableColumn <ProductOrder, Number> currentOrderIdColumn;
    @FXML
    private TableColumn <ProductOrder, String> currentOrderCompanyColumn;
    @FXML
    private TableColumn <ProductOrder, String> currentOrderAmountColumn;
    @FXML
    private TableColumn <ProductOrder, String> currentOrderStatusColumn;
    @FXML
    private TextField currentOrderSearchField;
    @FXML
    private Button currentOrderDetailsButton;

    // History tab
    @FXML
    private TableView <ProductOrder> historyTable;
    @FXML
    private TableColumn <ProductOrder, Number> idOrderColumn;
    @FXML
    private TableColumn <ProductOrder, String> companyOrderColumn;
    @FXML
    private TableColumn <ProductOrder, String> amountOrderColumn;
    @FXML
    private TableColumn <ProductOrder, String> statusOrderColumn;
    @FXML
    private TextField historySearchField;
    @FXML
    private Button orderDetailsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }


    //NOWOSC
    public void setClientDetails(BasicUser user) {
        // Backend to do
        // Getting details about client
        accountNameField.setText(user.getUserName());
        accountPasswordField.setText(user.getUserPassword());
    }

    //ilość produktu wybrana przez klienta
    private Integer productAmount = 0;

    // My account page
    public void myAccountButtonOnAction() throws IOException {
        // Checking if the order has finished
        if (!cart.isEmpty()) {
            if (!notFinishedOrder()) {}
            else {
                vBox.setBackground(new Background(new BackgroundFill(Color.web("#40c4ff"), CornerRadii.EMPTY, Insets.EMPTY)));
                clearFilters();
                tabText.setText("My account");
                myAccountPage.toFront();
            }
        }
    }
    // This three methods can be taken out for all Panel if the details will be the same
    public void changeDetailsButtonOnAction() {
        accountNameField.setEditable(true);
        accountPasswordField.setEditable(true);
        saveDetailsButton.toFront();
    }

    public void saveDetailsButtonOnAction() throws IOException {
    /*    FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../AlertBox.fxml"));
        String textInfo;
        //search for user with inputted name - should return null if name is valid for use
        BasicUser newName = clientService.FindClientByName(accountUserNameField.getText());
        //search for given city in database - should exist to change it
        City newCity = cityService.FindCityByName(accountNameField.getText());
        if (accountUserNameField.getText().isEmpty() || newName != null) {
            textInfo = "This username is already taken";
            new AlertBoxController().createAlert(fxmlLoader, textInfo);
        }
        else if (accountNameField.getText().isEmpty() || newCity == null){
            textInfo ="Your city does not exist in database, check your spelling or contact system administrator";
            new AlertBoxController().createAlert(fxmlLoader, textInfo);
        }
        else {
            client.setClientCity(newCity);
            client.setUserName(accountUserNameField.getText());
            client.setUserPassword(accountPasswordField.getText());
            clientService.ModifyClient(client);
            savingDetails(); //is it necessary?
        }
*/
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


    // Create order tab
    public void createOrderButtonOnAction() throws IOException {
        // Clear
        clearFilters();
        tabText.setText("Category");
        categoryPage.toFront();
    }

    @FXML
    public void categoryButtonOnAction(ActionEvent event)
    {
        if (event.getSource() instanceof Button)
        {
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
                            Product product = getTableView().getItems().get(getIndex());
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("../productForm.fxml"));
                            Optional<ButtonType> isConfirmed = null;
                            try {
                                isConfirmed = new ProductFormController().init(fxmlLoader, product , "Add a product");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(isConfirmed.get() == ButtonType.OK) {
                                cart.put(product, productAmount); //product amount should be set by client! and is
                            }
                        });
                        setGraphic(addButton);
                    }
                    setText(null);
                };
            };
            return cell;
        };
        addToBasketButtonColumn.setCellFactory(cellFactory);
        createOrderTable.setItems((ObservableList<Product>) products);
        productsListPage.toFront();
    }

    public void basketButtonOnAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../basket.fxml"));
        Optional<ButtonType> isConfirmed = new BasketController().init(fxmlLoader, cart, "Basket");
        if(isConfirmed.get() == ButtonType.OK) {
            // Order completed
            //creating and saving order to db

            //place order
            ProductOrder productOrder = new ProductOrder(client);
            this.productOrderService.createOrder(productOrder);
            //create order item list
            List<OrderItem> orderedItems = new ArrayList();
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                OrderItem orderItem = new OrderItem(productOrder, entry.getKey(), entry.getValue());
                this.orderItemService.CreateOrderItem(orderItem);
                orderedItems.add(orderItem);
            }
            //save ordered items to order
            productOrder.setOrderedProducts(orderedItems);
            this.productOrderService.modifyOrder(productOrder);


            FXMLLoader fxmlLoaders = new FXMLLoader();
            fxmlLoaders.setLocation(getClass().getResource("../alertBox.fxml"));
            new AlertBoxController().createAlert(fxmlLoaders, "You successfully placed the order");
            // Cleaning the order
            cart.clear();

        }
    }

    // Current orders tab
    public void currentOrdersButtonOnAction() throws IOException {
        // Clear
        if (!cart.isEmpty()) {
            if (!notFinishedOrder()) {}
            else {
                clearFilters();
                // Set up the style
                currentOrdersTable.getStylesheets().add("sample/styling/tableView.css");
                currentOrdersTable.getStyleClass().add("tableview");
                checkBoxColumn.setStyle("-fx-alignment: center;");

                orders = productOrderService.findCurrentOrders(client.getUserName());

                checkBoxColumn.setCellValueFactory(new PropertyValueFactory<ProductOrder, CheckBox>("select"));
                currentOrderIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getOrderId()));
                // TODO: 07.06.2021  czy cenę przechowujemy w zamówieniu, czy on ma być za każdym razem na nowo obliczana (trochę to zasobożerne)
                //currentOrderAmountColumn.setCellValueFactory(cellData -> cellData.getValue());
                currentOrderStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));

                currentOrderDetailsButton.disableProperty().bind(Bindings.isNull(
                        currentOrdersTable.getSelectionModel().selectedItemProperty()));

                // This could also be taken out but it depends if there will be different data in table view.
                // Searching in table view
                FilteredList<ProductOrder> filteredData = new FilteredList<ProductOrder>((ObservableList<ProductOrder>) orders, b -> true);

                currentOrderSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(order -> {
                        if (newValue == null || newValue.isEmpty()) return true;

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (String.valueOf(order.getOrderId()).indexOf(lowerCaseFilter) != -1)
                            return true;
                        else if (order.getOrderStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                            return true;
                        else if (order.getOrderTransportProvider().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                            return true;
                        else
                            return false;
                    });
                });
                SortedList<ProductOrder> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(currentOrdersTable.comparatorProperty());

                currentOrdersTable.setItems(sortedData);

                // You can change the color of Vbox if needed
                //vBox.setBackground(new Background(new BackgroundFill(Color.web("#e1f5fe"), CornerRadii.EMPTY, Insets.EMPTY)));
                tabText.setText("Current orders");
                currentOrdersPage.toFront();
            }
        }
    }

    public void acceptButtonOnAction(){
        //TODO - ZAMIENIĆ NA CALLBACK
        /*ObservableList<ProductOrder> selectedList = FXCollections.observableArrayList();
        for (ProductOrder order : orders){
            if(order.getSelect().isSelected()){
                // Backend to do
                // Changing the status of order
                order.setOrderStatus(OrderStatus.ACCEPTED);
                productOrderService.modifyOrder(order);
                order.getSelect().setSelected(false);
            } }*/
    }

    public void declineButtonOnAction(){
        //TODO - ZAMIENIĆ NA CALLBACK
    /*    ObservableList<ProductOrder> selectedList = FXCollections.observableArrayList();
        for (ProductOrder order : orders){
            if(order.getSelect().isSelected()){
                // Backend to do
                // Changing the status of order
                order.setOrderStatus(OrderStatus.REJECTED);
                productOrderService.modifyOrder(order);
                order.getSelect().setSelected(false);//?????????????
            } }*/
    }

    // History page
    public void historyButtonOnAction() throws IOException {
        if (!cart.isEmpty()) {
            if (!notFinishedOrder()) {
            } else {
                clearFilters();
                orders = productOrderService.findHistoricOrders(client.getUserName());

                historyTable.getStylesheets().add("sample/styling/tableView.css");
                historyTable.getStyleClass().add("tableview");

                idOrderColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getOrderId()));
                // TODO: 07.06.2021  cenę przechowujemy w zamówieniu
                //amountOrderColumn.setCellValueFactory(cellData -> cellData.getValue());
                statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));
                orderDetailsButton.disableProperty().bind(Bindings.isNull(
                        historyTable.getSelectionModel().selectedItemProperty()));

                // This could also be taken out but it depends if there will be different data in table view.
                // Searching in table view
                FilteredList<ProductOrder> filteredData = new FilteredList<ProductOrder>((ObservableList<ProductOrder>) orders, b -> true);

                historySearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(order -> {
                        if (newValue == null || newValue.isEmpty()) return true;

                        String lowerCaseFilter = newValue.toLowerCase();

                        if (String.valueOf(order.getOrderId()).indexOf(lowerCaseFilter) != -1)
                            return true;
                        else if (order.getOrderStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                            return true;
                        else if (order.getOrderTransportProvider().getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                            return true;
                        else
                            return false;
                    });
                });
                SortedList<ProductOrder> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(historyTable.comparatorProperty());
                historyTable.setItems(sortedData);

                //vBox.setBackground(new Background(new BackgroundFill(Color.web("#e1f5fe"), CornerRadii.EMPTY, Insets.EMPTY)));
                tabText.setText("History");
                historyPage.toFront();
            }
        }
    }

    public void orderDetailsButtonOnAction() throws IOException {
        ProductOrder selectedOrder;
        if (tabText.getText() == "Current orders")
            selectedOrder = currentOrdersTable.getSelectionModel().getSelectedItem();
        else
            selectedOrder = historyTable.getSelectionModel() .getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../orderDetails.fxml"));
        new OrderDetailsController().init(fxmlLoader, selectedOrder, "Order Details",true);
    }

    // Additional functions
    public void clearFilters(){
        currentOrderSearchField.setText("");
        historySearchField.setText("");
    }

    public boolean notFinishedOrder() throws IOException {
        FXMLLoader fxmlLoaders = new FXMLLoader();
        fxmlLoaders.setLocation(getClass().getResource("../confirmationBox.fxml"));
        Optional<ButtonType> isConfirmed = new ConfirmationBoxController().createConfirmation(fxmlLoaders, "You haven't finished the order", "Order not finished", "Empty basket", "Cancel");
        if (isConfirmed.get() == ButtonType.OK) {
            cart.clear();
            return true;
        }
        return false;
    }
}