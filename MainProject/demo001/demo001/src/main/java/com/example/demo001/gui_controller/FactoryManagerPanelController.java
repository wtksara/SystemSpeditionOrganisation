package com.example.demo001.gui_controller;

import com.example.demo001.Cipher;
import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.Transport.City;
import com.example.demo001.service.*;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URL;
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
    private Button changeDetailsButton, saveDetailsButton, myLoginDataButton;

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
    private ProductionAbilityService productionAbilityService;

    @Autowired
    private FactoryManagerService factoryManagerService;

    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private CityService cityService;

    private Factory sessionFactory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(NavigationController.factoryManagerScreenToFront==1)
            setUserDetails();
        else if(NavigationController.factoryManagerScreenToFront==2)
            myFactoryButtonOnAction();
        else
        {
            try{
                myProductsButtonOnAction();
            }
            catch (IOException e)
            {

            }
        }


    }

    private Cipher cipher = new Cipher();

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
        NavigationController.factoryManagerScreenToFront = 1;
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
        NavigationController.factoryManagerScreenToFront = 2;
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


    }

    private void initializeFactory(){
        this.sessionFactory = this.factoryManagerService.findByUsername(NavigationController.username).getManagedFactory();
        System.out.println("this.sessionFactory: ");
        System.out.println(this.sessionFactory.getFactoryName());
    }

    public void savingFactoryDetails(){
        if(this.sessionFactory == null){
            initializeFactory();
        }
        /*
        Saving factory info
         */
        System.out.println("factoryNameField.getText(): " + factoryNameField.getText());
        System.out.println("factoryIdField.getText(): " + factoryIdField.getText());
        System.out.println("localizationField.getText(): " + localizationField.getText());
        this.sessionFactory.setFactoryName(factoryNameField.getText());
        this.sessionFactory.setFactoryId(Long.parseLong(factoryIdField.getText()));
        City factoryLocation = this.cityService.FindCityByName(localizationField.getText());
        System.out.println("factoryLocation");
        System.out.println(factoryLocation.getCityName());
        this.sessionFactory.setFactoryLocation(factoryLocation);


        saveFactoryDetailsButton.toFront();

        //Set back field to uneditable
        factoryNameField.setEditable(false);
        factoryIdField.setEditable(false);
        localizationField.setEditable(false);
        changeFactoryDetailsButton.toFront();


    }

    // My products tab (3)
    public void myProductsButtonOnAction() throws IOException { //change tab to "My products"
        if(this.sessionFactory == null){
            initializeFactory();
        }
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
        List<ProductionAbility> producedProd = productionAbilityService.getFactoryByName(sessionFactory.getFactoryName()).getProducedProducts();

        users = FXCollections.observableArrayList(productionAbilityService.getFactoryByName(sessionFactory.getFactoryName()).getProducedProducts());
        //users = FXCollections.observableArrayList();

        /*FactoryManager fm = factoryManagerService.findByUsername(NavigationController.username);
        Factory fc = fm.getManagedFactory();
        //FXCollections.observableArrayList
        List<ProductionAbility> ls = fc.getProducedProducts();
        users = FXCollections.observableArrayList();
        users.addAll(ls);*/

        /*
        deleteProductsButton.disableProperty().bind(Bindings.isNull (
                usersTable.getSelectionModel().selectedItemProperty()));
        updateProductsButton.disableProperty().bind(Bindings.isNull(
                usersTable.getSelectionModel().selectedItemProperty()));
        */

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
        NavigationController.factoryManagerScreenToFront = 3;
        NavigationController.deleteProducts = true;
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(ConfirmationBoxController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Do you want to delete these products?");
        stage.show();

        /* Optional<ButtonType> isConfirmed = new ConfirmationBoxController().createConfirmation(fxmlLoader, "Are you sure you would like to delete these products?", "Deleting products", "Ok", "Cancel");
        if(isConfirmed.get() == ButtonType.OK) {
            // Backend
            // Deleting chosen products from table - ConfirmationBoxController
            int selectedId = usersTable.getSelectionModel().getSelectedIndex();
            users.remove(selectedId);
        } */
    }

    public void updateProductsButtonOnAction() throws IOException { //to update only amount of products in factory
        NavigationController.factoryManagerScreenToFront = 3;
        NavigationController.productionAbilityToUpdate = usersTable.getSelectionModel().getSelectedItem();
        NavigationController.lastScene = NavigationController.stage.getScene();
        NavigationController.lastSceneName=NavigationController.stage.getTitle();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(ProductFormController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Update amount of products");
        stage.show();
    }


    public void addProductsButtonOnAction() throws IOException { //to add products to the factory
        ProductionAbility newUser = new ProductionAbility();
        NavigationController.factoryManagerScreenToFront = 3;
        NavigationController.lastScene = NavigationController.stage.getScene();
        NavigationController.lastSceneName=NavigationController.stage.getTitle();
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(FactoryFormController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add products");
        stage.show();
        // Backend
        // Adding products to table - inside FactoryFormController
    }

    // Cleaning text in searching field
    public void clearFilters(){
        searchField.setText("");
    }

}