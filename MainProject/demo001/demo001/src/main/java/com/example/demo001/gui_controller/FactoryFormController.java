package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.Administrator;
import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Actors.UserRole;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.OrderManager;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;
import com.example.demo001.domain.Transport.City;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.service.FactoryManagerService;
import com.example.demo001.service.ProductService;
import com.example.demo001.service.ProductionAbilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@FxmlView("factoryForm.fxml")
public class FactoryFormController {

    // For changing the field in time of writing - If needed to use.
    // idField.textProperty().bindBidirectional(user.idProperty(), NumberFormat.getNumberInstance());
    // nameField.textProperty().bindBidirectional(user.nameProperty());
    // surnameField.textProperty().bindBidirectional(user.surnameProperty());

    @FXML
    private TextField productNameField, categoryField, amountField;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Button okButton, cancelButton;

    @Autowired
    private ProductService productService;

    @Autowired
    private FactoryManagerService factoryManagerService;

    @Autowired
    private ProductionAbilityService productionAbilityService;

    private Factory sessionFactory;

    private void initializeFactory(){
        if(this.sessionFactory == null) {
            this.sessionFactory = this.factoryManagerService.findByUsername(NavigationController.username).getManagedFactory();
        }
    }



    public void okButtonOnAction(ActionEvent event) throws IOException {
        initializeFactory();
        ProductType productType=null;
        if (productNameField.getText().isEmpty() || amountField.getText().isEmpty() || categoryField.getText().isEmpty()){
            // Backend
            // Adding products to table
                NavigationController.alertText = "None of the fields can be empty";
                callAlertBox();
                return;
            }
            Product product = this.productService.FindProductByName(productNameField.getText());
            System.out.println("product.getProductName()");
            System.out.println(product.getProductName());
            if(product==null)
            {
                NavigationController.alertText = "Enter proper product name";
                callAlertBox();
                return;
            }
            if(this.sessionFactory.getProducedProducts().stream().anyMatch(prodAb -> prodAb.getMyProduct().getProductName().equals(product.getProductName()))){
                NavigationController.alertText = "This product is already produced";
                callAlertBox();
                return;
            }
            try{
                productType = ProductType.valueOf(this.categoryField.getText());}
            catch(IllegalArgumentException e)
            {
                NavigationController.alertText = "Enter proper product type";
                callAlertBox();
                return;
            }

            if(product.getProductType() != productType)
            {
                NavigationController.alertText = "This product does not belong to entered category";
                callAlertBox();
                return;
            }
            ProductionAbility productionAbility = new ProductionAbility();
            //productionAbility.setId(Long.parseLong(productIdField.getText()));
            productionAbility.setMyProduct(product);
            productionAbility.setProductAmount(Integer.parseInt(amountField.getText()));

            //może to dać do serwisu
            this.productionAbilityService.newProductionAbilityForFactory(this.sessionFactory, productionAbility);
            NavigationController.addProduct = true;
            //users.add(newUser);
            System.out.println("Dodano produkty");
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(FactoryManagerPanelController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle(NavigationController.lastSceneName);
            NavigationController.stage.show();
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


    public void cancelButtonOnAction(ActionEvent event) throws IOException {
        // do nothing and close the window
        /*NavigationController.factoryManagerScreenToFront = 3;
        NavigationController.stage.setScene(NavigationController.lastScene);
        NavigationController.stage.setTitle(NavigationController.lastSceneName);
        NavigationController.stage.show();*/
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    public Optional<ButtonType> init (FXMLLoader fxmlLoader, ProductionAbility
            productionAbility, String title) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");

        FactoryFormController factoryformController = fxmlLoader.getController();
        factoryformController.setStyling();
        factoryformController.setUserDetails(productionAbility);

        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);

        return dialog.showAndWait();

    }

    public void setUserDetails(ProductionAbility productionAbility){
        // Backend to do
        // Getting details about users
        productNameField.setText(productionAbility.getMyProduct().getProductName());
        amountField.setText((String.valueOf(productionAbility.getProductAmount())));
        //TODO - możliwe dodatkowe pola
        //categoryField.setText(user.getStatus());
        /*
        Button okButton = (Button)dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION, event -> {
                    // Backend to do
                    // Checking if the input is correct
                    if (!validateFormData()) {
                        event.consume();
                    }
                    else {
                        // Backend to do
                        // Setting users details
                        //Tworzenie ProductionAbility - praca na obiekcie przekazanym do tej klasy,
                        //ustawienie jego pól, sprawdzenie czy istnieje taki produkt jaki byśmy chcieli
                        //produkować w fabryce; jeżeli istnieje to dodajemy ProductionAbility do fabryki;
                        //jeżeli nie istnieje to informujemy o podaniu niewłaściwego produktu

                        //Funkcja wyszukująca produkt o określonej nazwie w bazie
                        Product selectedProduct = getProductIfExists(productNameField.getText());
                        if(selectedProduct == null)
                        {
                            productionAbility.setMyProduct(selectedProduct);
                            productionAbility.setProductAmount(Integer.parseInt(amountField.getText()));
                        }
                        productionAbility.setUserName(productNameField.getText());
                        user.setStatus(categoryField.getText());

                    }
                });*/
    }

    private Product getProductIfExists(String productName)
    {
        return productService.FindProductByName(productName);
    }

    public void setStyling() {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");

        Button cancelButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(1));
        cancelButton.getStyleClass().add("buttom");
    }



}

