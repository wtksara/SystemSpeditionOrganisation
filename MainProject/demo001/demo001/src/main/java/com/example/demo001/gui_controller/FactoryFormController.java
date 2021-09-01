package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.service.ProductService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("factoryForm.fxml")
public class FactoryFormController {

    // For changing the field in time of writing - If needed to use.
    // idField.textProperty().bindBidirectional(user.idProperty(), NumberFormat.getNumberInstance());
    // nameField.textProperty().bindBidirectional(user.nameProperty());
    // surnameField.textProperty().bindBidirectional(user.surnameProperty());

    @FXML
    private TextField productIdField, productNameField, categoryField, amountField;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Button okButton, cancelButton;

    @Autowired
    private ProductService productService;

    public void okButtonOnAction(ActionEvent event) throws IOException {

        if (!(productIdField.getText().isEmpty() | productNameField.getText().isEmpty() | amountField.getText().isEmpty())) {
            // Backend
            // Adding products to table
            //users.add(newUser);
            System.out.println("Dodano produkty");
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
    }

    public void cancelButtonOnAction(ActionEvent event) throws IOException {
        // do nothing and close the window
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
        productIdField.setText(Long.toString(productionAbility.getId()));
        productNameField.setText(productionAbility.getMyProduct().getProductName());
        amountField.setText((String.valueOf(productionAbility.getProductAmount())));
        //TODO - możliwe dodatkowe pola
        //categoryField.setText(user.getStatus());

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

                        productionAbility.setId(Integer.parseInt(productIdField.getText()));

                        //Funkcja wyszukująca produkt o określonej nazwie w bazie
                        Product selectedProduct = getProductIfExists(productNameField.getText());
                        if(selectedProduct == null)
                        {
                            productionAbility.setMyProduct(selectedProduct);
                            productionAbility.setProductAmount(Integer.parseInt(amountField.getText()));
                        }
                        /*productionAbility.setUserName(productNameField.getText());
                        user.setStatus(categoryField.getText());*/

                    }
                });
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

    private boolean validateFormData() {
        // Backend to do
        // Checking if the input is correct
        if (productIdField.getText().isEmpty() || productIdField.getText().equals("0")) {
            productIdField.requestFocus();
            return false;
        }
        return true;
    }
}

