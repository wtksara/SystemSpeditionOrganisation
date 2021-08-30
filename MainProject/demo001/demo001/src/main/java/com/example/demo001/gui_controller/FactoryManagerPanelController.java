package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.service.BasicUserService;
import com.example.demo001.service.FactoryForOrderItemSetup;
import com.example.demo001.service.FactoryManagerService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@FxmlView("factoryManagerPanel.fxml")
public class FactoryManagerPanelController implements Initializable {

    // Changing the tabs
    @FXML
    private Label tabText; //tab my account
    @FXML
    private Label tabMyFactory; //tab my factory
    @FXML
    private VBox vBox; //vBox my account
    @FXML
    private VBox vBox2; //vBox my factory
    @FXML
    private GridPane myAccountPage, myFactoryPage; //my account page, my factory page
    @FXML
    private BorderPane myProductsPage; //my products page
    @FXML
    private Button myAccountButton, myFactoryButton, myProductsButton;

    // My account tab (1) - user account
    @FXML
    private TextField accountNameField;
    @FXML
    private PasswordField accountPasswordField;
    @FXML
    private Button changeDetailsButton, saveDetailsButton;

    // My factory tab (2) - information about factory
    @FXML
    private TextField factoryNameField, factoryIdField,  localizationField;
    @FXML
    private Button changeFactoryDetailsButton, saveFactoryDetailsButton;

    // My products tab (3) - all products in factory
    private ObservableList <ProductionAbility> users = null;
    @FXML
    private TableView <ProductionAbility> usersTable;
    @FXML
    private TableColumn <ProductionAbility, Number> productIdColumn;
    @FXML
    private TableColumn <ProductionAbility, String> productNameColumn;
    @FXML
    private TableColumn <ProductionAbility, String> categoryColumn;
    @FXML
    private TableColumn <ProductionAbility, String> amountColumn;
    @FXML
    private Button addProductsButton, updateProductsButton, deleteProductsButton;
    @FXML
    private TextField searchField;


    @Autowired
    private FactoryForOrderItemSetup factoryForOrderItemSetup;

    @Autowired
    private FactoryManagerService factoryManagerService;

    @Autowired
    private BasicUserService basicUserService;

    private Factory sessionFactory;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    //NOWOSC
    public void setFactoryManagerDetails(BasicUser user) {
        // Backend to do
        // Getting details about factory manager
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
    public void myAccountButtonOnAction() { //change tab to "My account"
        // Setting the color of te top vBox for more dynamic view
        clearFilters();
        vBox.setBackground(new Background(new BackgroundFill(Color.web("#40c4ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        tabText.setText("My account");
        setUserDetails();
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
/*
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

    // My factory tab (2)
    public void myFactoryButtonOnAction() { //change tab to "My factory"
        // Setting the color of te top vBox for more dynamic view
        clearFilters();
        vBox.setBackground(new Background(new BackgroundFill(Color.web("#40c4ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        tabText.setText("My factory");
        myFactoryPage.toFront();
    }
    public void changeFactoryDetailsButtonOnAction() { //to allow the user change information about factory
        factoryNameField.setEditable(true);
        factoryIdField.setEditable(true);
        localizationField.setEditable(true);
        saveFactoryDetailsButton.toFront();
    }

    public void saveFactoryDetailsButtonOnAction() throws IOException { //saving information about factory changed by user
        // Backend
        // Saving changed details for a factory
/*
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../AlertBox.fxml"));
        String textInfo;
        if (factoryNameField.getText().isEmpty()) {
            textInfo = "This name is already taken";
            new AlertBoxController().createAlert(fxmlLoader, textInfo);
        }
        else if (factoryIdField.getText().isEmpty()){
            textInfo ="Your factory does not exist in database, check the correctness or contact system administrator";
            new AlertBoxController().createAlert(fxmlLoader, textInfo);
        }
        else {
            savingFactoryDetails();
        }
*/
    }

    public void savingFactoryDetails(){

        factoryNameField.getText();
        factoryIdField.getText();
        localizationField.getText();
        saveFactoryDetailsButton.toFront();

        //Set back field to uneditable
        factoryNameField.setEditable(false);
        factoryIdField.setEditable(false);
        localizationField.setEditable(false);
        changeFactoryDetailsButton.toFront();
    }

    // My products tab (3)
    public void myProductsButtonOnAction() throws IOException { //change tab to "My products"
        // Set up the style
        usersTable.getStylesheets().add("sample/styling/tableView.css");
        usersTable.getStyleClass().add("tableview");
        tabText.setText("My products");
        myProductsPage.toFront();

        // Backend to do
        // Displaying in table all products from the factory
        // (product ID, product Name, amount, category)

        productIdColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()));
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMyProduct().getProductName()));
        amountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Integer)cellData.getValue().getProductAmount()).toString()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getMyProduct().getProductType().toString()));


        //TODO - POTEŻNY ONELINER
        //users = FXCollections.observableArrayList(factoryForOrderItemSetup.getFactoryByName(sessionFactory.getFactoryName()).getProducedProducts());
        users = FXCollections.observableArrayList();

        /*FactoryManager fm = factoryManagerService.findByUsername(NavigationController.username);

        Factory fc = fm.getManagedFactory();
        //FXCollections.observableArrayList
        List<ProductionAbility> ls = fc.getProducedProducts();
        users = FXCollections.observableArrayList();
        users.addAll(ls);*/


        deleteProductsButton.disableProperty().bind(Bindings.isNull (
                usersTable.getSelectionModel().selectedItemProperty()));
        updateProductsButton.disableProperty().bind(Bindings.isNull(
                usersTable.getSelectionModel().selectedItemProperty()));

        // This could also be taken out but it depends if there will be different data in table view.
        // Searching in table view
        FilteredList<ProductionAbility> filteredData = new FilteredList<>(users, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(user.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getMyProduct().getProductName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (user.getMyProduct().getProductType().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<ProductionAbility> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());

        //  Setting the table
        usersTable.setItems(sortedData);
    }

    public void deleteProductsButtonOnAction() throws IOException { //to delete products from factory
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../confirmationBox.fxml"));
        /*Optional<ButtonType> isConfirmed = new ConfirmationBoxController().createConfirmation(fxmlLoader, "Are you sure you would like to delete these products?", "Deleting products", "Ok", "Cancel");
        if(isConfirmed.get() == ButtonType.OK) {
            // Backend to do
            // Deleting chosen products from table
            int selectedId = usersTable.getSelectionModel().getSelectedIndex();
            users.remove(selectedId);
        }*/
    }

    public void updateProductsButtonOnAction() throws IOException { //to update only amount of products in factory
        ProductionAbility selectedUser = usersTable.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../productForm.fxml"));
        new ProductFormController().init(fxmlLoader, selectedUser.getMyProduct(), "Update products");
        // Backend to do
        // Update data (only amount) for selectedProducts - inside of the ProductFormController
    }

    public void addProductsButtonOnAction() throws IOException { //to add products to the factory
        ProductionAbility newUser = new ProductionAbility();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../factoryForm.fxml"));
        Optional<ButtonType> isConfirmed  = new FactoryFormController().init(fxmlLoader, newUser, "Add products");
        if (isConfirmed.get() == ButtonType.OK){
            // Backend to do
            // Adding products to table
            users.add(newUser);
        }
    }

    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
    }

}
