package com.example.demo001.gui_controller;

import com.example.demo001.domain.Products.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("productForm.fxml")
public class ProductFormController extends ConfirmationBoxController {

    @FXML
    private TextField idField, productField, amountField;
    @FXML
    private DialogPane dialogPane;

    public Optional<ButtonType> init (FXMLLoader fxmlLoader, Product product, String title) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");

        ProductFormController productFormController = fxmlLoader.getController();
        productFormController.setStyling();
        productFormController.setProductDetails(product);

        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }

    public void setProductDetails (Product product){
        // Backend to do
        // Getting details about product
        idField.setText(Integer.toString((int)product.getProductId()));
        productField.setText(product.getProductName());

        Button okButton = (Button)dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION, event -> {
                    // Backend to do
                    // Checking if the input is correct
                    //TODO - TA FUNKCJA MUSI SPRAWDZAĆ CZY ILOŚĆ JEST ODPOWIEDNIEGO TYPU I ODPOWIEDNIEJ WARTOŚCI
                    if (!validateFormData()) {
                        event.consume();
                    }
                    else {
                        // Backend to do
                        // Setting product details

                        //TODO - POBRANIE ILOŚCI ZAMAWIANYCH PRODUKTÓW
                        amountField.getText();
                    }
                });
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
        //TODO - AMOUNT FIELD GET TEXT - sprawdzenie poprawności ilości produktu - NIE idField - to jest wypełniacz
        if (idField.getText().isEmpty() || idField.getText().equals("0")) {
            idField.requestFocus();
            return false;
        }
        return true;
    }
}