package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Transport.TransportProvider;
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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URL;
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
    private BorderPane manageBasicUserPage, createOfferPage;
    @FXML
    private Button myAccountButton, manageOffersButton, createOfferButton, historyButton;

    // My account tab
    @FXML
    private TextField accountNameField, accountBasicUserNameField, accountSurnameField, accountEmailField;
    @FXML
    private PasswordField accountPasswordField;
    @FXML
    private Button deleteAccountButton, changeDetailsButton, saveDetailsButton;

    @FXML
    private TableView <BasicUser> BasicUsersTable;
    @FXML
    private TableColumn <BasicUser, Number> idColumn;
    @FXML
    private TableColumn <BasicUser, String> nameColumn;
    @FXML
    private TableColumn <BasicUser, String> surnameColumn;
    @FXML
    private Button orderDetailsButton;
    @FXML
    private TextField searchField;

    @FXML
    private TableColumn <BasicUser, Number>  createOfferIdColumn;
    @FXML
    private TableColumn <BasicUser, String> createOfferNameColumn;
    @FXML
    private TableColumn <BasicUser, String> createOfferSurnameColumn;
    @FXML
    private TextField createOfferSearchField;
    @FXML
    private Button createOfferOrderDetailsButton;

    @FXML
    private TableView<Product>productsTable;
    @FXML
    private TableColumn<Product, Number> idProductColumn;
    @FXML
    private TableColumn<Product, String> productColumn;
    @FXML
    private TableColumn<Product, String> amountOfProductColumn;
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
    private TableView<Product> summaryProductsTable;
    @FXML
    private TableColumn<Product, Number>  summaryIdProductColumn;
    @FXML
    private TableColumn<Product, String> summaryProductColumn;
    @FXML
    private TableColumn<Product, String> summaryAmountOfProductColumn;
    @FXML
    private TableColumn<Product, String>summaryFactoryColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    // Create new offer tab
    // Main view
    @FXML
    private TableView<BasicUser> createOfferBasicUsersTable;

    //zmienna na klientów
    private ObservableList <Client> users = null;

    //Obiekty aktualnych zamówień
    private List<ProductOrder> allCurrentOrders;

    private ObservableList<Product> productsArray = null;

    // Offer for order
    private BasicUser currentUser = null;

    //Obiekt zamówienia przetwarzanego podczas sesji
    private ProductOrder sessionOrder;

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
        accountBasicUserNameField.setEditable(true);
        accountSurnameField.setEditable(true);
        accountEmailField.setEditable(true);
        accountPasswordField.setEditable(true);
        saveDetailsButton.toFront();
    }

    public void deleteAccountButtonOnAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../confirmationBox.fxml"));
        Optional<ButtonType> isConfirmed = new ConfirmationBoxController().createConfirmation(fxmlLoader, "Are you sure you would like to delete your account ?", "Deleting account", "Ok", "Cancel");
        if(isConfirmed.get() == ButtonType.OK) {
            // Backend
            // Deleting account to do
            // must have actually proceeded account
            // void deleteAccount(long BasicUserId)
        }
    }

    public void saveDetailsButtonOnAction() {
        // Backend
        // Saving changed details for a BasicUser
        accountNameField.getText(); //account name ?
        accountBasicUserNameField.getText();//concatenate BasicUser name
        accountSurnameField.getText();//and surname ??
        accountEmailField.getText();//ok
        accountPasswordField.getText();//ok
        saveDetailsButton.toFront();
        //create new BasicUser
        //boolean checkIfBasicUserExists(String accountName, String BasicUserName);
        //void addAccount(BasicBasicUser newBasicUser)

        //Set back field to uneditable
        accountNameField.setEditable(false);
        accountBasicUserNameField.setEditable(false);
        accountSurnameField.setEditable(false);
        accountEmailField.setEditable(false);
        accountPasswordField.setEditable(false);
        changeDetailsButton.toFront();
    }

    // Manager offers tab
    public void manageOffersButtonOnAction() throws IOException {
        // Set up the style
        BasicUsersTable.getStylesheets().add("sample/styling/tableView.css");
        BasicUsersTable.getStyleClass().add("tableview");
        tabText.setText("Manage offers");
        manageBasicUserPage.toFront();

        //idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getUserId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        //surnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

        //odwolanie do service zamiast do samego repozytorium
        //this.allCurrentOrders = List<ProductOrder>getAllOrders();
        //wymagane będzie wydobycie nazw użytkowników z zamówień
        //FXCollections users = FXCollections.observableArrayList(BasicUsersRepository.getBasicUsers());
        //propozycja wydobycia nazw użytkowników z aktualnie istniejących zamówień
        populateUsers();

        orderDetailsButton.disableProperty().bind(Bindings.isNull (
                BasicUsersTable.getSelectionModel().selectedItemProperty()));
        //  Setting the table
        BasicUsersTable.setItems(setupOffersList());
    }

    private void populateUsers(){
        users =( ObservableList<Client>)(this.allCurrentOrders.stream()
                .map(ProductOrder::getOrderClient).collect(Collectors.toList()));
    }

    private ObservableList<BasicUser> setupOffersList(){
        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        //zmiana typu na client
        FilteredList<Client> filteredData = new FilteredList<Client>(users, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(BasicUser -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(BasicUser.getUserId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (BasicUser.getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                /*else if (BasicUser.getSurname().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;*/
                else
                    return false;
            });
        });
        SortedList<BasicUser> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(BasicUsersTable.comparatorProperty());
        return sortedData;
    }

    public void orderDetailsButtonOnAction() throws IOException {
        BasicUser selectedOrder = BasicUsersTable.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../orderDetails.fxml"));
        // Two types of showing orders details - true without editing, false - editing
        new OrderDetailsController().init(fxmlLoader, selectedOrder, "Order Details",true);
    }

    public void createOfferOrderDetailsButtonOnAction() throws IOException {
        BasicUser selectedUser = createOfferBasicUsersTable.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../orderDetails.fxml"));
        Optional<ButtonType> isConfirmed = new OrderDetailsController().init(fxmlLoader, selectedUser, "Order Details",false);
        if(isConfirmed.get() == ButtonType.OK) {
            // Backend to do
            // Setting a order, which will changed now - can be done in different way
            // now an Order accessible during the whole offer creation seesion must be constructed
            // czyli chodzi o obiekt actualOrder
            this.currentUser = selectedUser;
            //tak na razie jest dokonywane połaczenie pomiędzy uzytkownikiem wybranym na frontendzie a odpowiadającym mu zamówieniu
            this.sessionOrder = this.allCurrentOrders.get(users.indexOf(currentUser));
            newOfferPage.toFront();
            setUpNewOfferPage();
        }
    }

    // Create offer tab
    public void createOfferButtonOnAction() throws IOException {
        // Set up the style
        createOfferBasicUsersTable.getStylesheets().add("sample/styling/tableView.css");
        createOfferBasicUsersTable.getStyleClass().add("tableview");
        tabText.setText("Create a offer");
        createOfferPage.toFront();

        createOfferIdColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getUserId()));
        createOfferNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        //createOfferSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

        //users = FXCollections.observableArrayList(BasicUsersRepository.getBasicUsers());
        //odwolanie do service zamiast do samego repozytorium
        //this.allCurrentOrders = List<ProductOrder>getAllOrders();
        populateUsers();

        createOfferOrderDetailsButton.disableProperty().bind(Bindings.isNull (
                createOfferBasicUsersTable.getSelectionModel().selectedItemProperty()));

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        // w tym miejscu powinna zostac pobrana lista wszystkich uzytkownikow ktorzy zlozyli oferte
       /* FilteredList<BasicUser> filteredData = new FilteredList<>(BasicUsers, b -> true);
        createOfferSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(BasicUser -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(BasicUser.getUserId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (BasicUser.getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                //else if (BasicUser.getUserSurname().toLowerCase().indexOf(lowerCaseFilter) != -1)
                 //   return true;
                else
                    return false;
            });
        });
        SortedList<BasicUser> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(createOfferBasicUsersTable.comparatorProperty());*/

        //  Setting the table
        createOfferBasicUsersTable.setItems(setupOffersList());
    }

    public void setUpNewOfferPage (){
        tabText.setText("Set up the offer");
        idOrderField.setText(Integer.toString((int) currentUser.getUserId()));
        companyField.setText(currentUser.getUserName());
        //amountField.setText(selectedOrder.getSurname());

        // Backend to do
        // Normally taken the list of product for selected order - sth similiary for check already done
        // pozyskanie produktow dla danego zamowienia
        // List<Product> getProductsForActualOrder(Order actualOrder);
        productsArray = FXCollections.observableArrayList(sessionOrder.getOrderedProducts().stream()
                .map(OrderItem::getProduct).collect(Collectors.toList()));
        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStyleClass().add("tableview");

        idProductColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getProductId()));
        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        //amountOfProductColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

        Callback<TableColumn <OrderItem, String>, TableCell <OrderItem, String>> cellFactory = (param) -> {
            final TableCell<OrderItem, String> cell = new TableCell<OrderItem, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        OrderItem orderItem = getTableView().getItems().get(getIndex());
                        ComboBox comboBox = new ComboBox();
                        comboBox.setMinWidth(170);
                        comboBox.setMinHeight(25);

                        //###### TA CZESC JEST WYWOLYWANA WIELOKROTNIE W CZASIE INICJALIZACJI OFERTY
                        // Backend to do
                        // It has to be for changes if someone is going back from summary !
                        // here order Factory must be obtained
                        comboBox.setValue( orderItem.getFactory().getFactoryName());

                        // Backend to do
                        // Depending from the product different list in combobox is given
                        // Now all products has the same one almost
                        // List<Factory> getPossibleFactories(Order actualOrder);
                        //if (product.getProductId() == 1){ comboBox.setItems(FXCollections.observableArrayList("Maniek", "Zgrana Spółka"));}
                        //else { comboBox.setItems(FXCollections.observableArrayList("Mrówka", "Zespół Pomocnik"));}

                        //###### TA CZESC JEST WYWOLYWANA WIELOKROTNIE W CZASIE INICJALIZACJI OFERTY

                        comboBox.setOnAction(event -> {
                            // Backend to do
                            // Changing the factory for selected one
                            // void setFactoryForOrder(OrderItem actualOrderItem, Factory chosenFactory);
                            //czy te order items należą do sessionOrder ?
                            //orderItem.setFactory((String) comboBox.getValue()));
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
        setupTransportProvider();
    }

    private void setupTransportProvider(){
        //List<TransportProvider> getTransportProvidersForOrder(Order actualOrder);

        transportProviderCombobox.getStylesheets().add("sample/styling/comboBoxView.css");
        // Careful all the time first setValue, then setItems - otherwise it not gonna be correct
        //transportProviderCombobox.setValue(currentUser.getTransport());
        // Backend to do
        // Getting available transport
        // pozyskanie TransportProviderów ktorzy obsluguja polaczenie klient - fabryka
        transportProviderCombobox.setItems(FXCollections.observableArrayList("DHL", "DPD"));

        transportProviderCombobox.setOnAction(event -> {
            // Backend to do
            // Setting chosen transport
            //void setTransportProviderForOrder(TransportProvider chosenProvider, Order actualOrder);
            //currentUser.setTransport(transportProviderCombobox.getValue());
        });
    }

    public void summaryOfferButtonOnAction(){
        // Backend to do
        // Order getActualOrder();
        tabText.setText("Summary offer");
        summaryNewOfferPage.toFront();
        summaryIdOrderField.setText(Long.toString(currentUser.getUserId()));
        summaryCompanyField.setText(currentUser.getUserName());
        //summaryAmountField.setText(selectedOrder.getSurname());
        //użycie gettera zamiast serwisu
        summaryTransportProviderField.setText(sessionOrder.getOrderTransportProvider().getUserName());

        summaryProductsTable.getStylesheets().add("sample/styling/tableView.css");
        summaryProductsTable.getStyleClass().add("tableview");

        summaryIdProductColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getProductId()));
        summaryProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        //summaryAmountOfProductColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        //summaryFactoryColumn.setCellValueFactory(cellData -> cellData.getValue().);
        productsArray = FXCollections.observableArrayList(sessionOrder.getOrderedProducts().stream().map(obj -> obj.getProduct()).collect(Collectors.toList()));
        summaryProductsTable.setItems(productsArray);
    }

    public void goBackButtonOnAction(){
        // Backend to do
        // Clearing out after changing the tab back
        // void clearActualOrder(Order actualOrder)
        //for (Product product : currentUser.getProducts())
            //product.setCompany("");
        //currentUser.setTransport("");
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
            // void sendOffer(Order proceededOrder)
            users.remove(currentUser);
            createOfferPage.toFront();
        }
    }

    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
        createOfferSearchField.setText("");
    }
}
/*Opcja 1: Konieczność utrzymywania obiektu actualOrder pomiędzy kliknieciem OrderDetails a kliknięciem Send the offer, lub powrotem do listy Orderow
* przy goBackButtonOnAction()*/
/*Opcja 2: Konieczność utrzymywania obiektu actualOrder pomiędzy kliknieciem Create offer a kliknięciem Send the offer, lub powrotem do listy Orderow
* przy goBackButtonOnAction()*/

/*Przy goBackButtonOnAction() koniecznosc usunięcia/wyzerowania obiektu actualOrder*/

/*KONTRAKT Z INNYMI KLASAMI
* 1. Konieczność pozyskiwania TransportProviderow w metodzie setUpNewOfferPage ()
* 2. Po wyslaniu Send the offer musi być wywoływana metoda do oznaczenia wybranego providera, poinformowania go,
*    doda się kolejny order ktory będzie pokazywany wybranemu tutaj TransportProvider, i on będzie miał możliwość wyboru:
*    potwierdza lub odrzuca;
*    po Send the offer zamówienie dostaje dopiero TransportProvider, a dopiero po jego akceptacji przekazane klientowi
* 3. Konieczność pozyskania wszystkich Orderów o statusie ISSUED
* 4. Konieczność ustawienia Factory dla każdego OrderItem aktualnego orderu, czyli każdego produktu
* 5. Konieczność zmiany statusu Order dla którego kliknięto Send the offer na jakiś stan opisujacy przetwarzanie*/