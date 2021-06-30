package com.example.demo001.gui_controller;

import com.example.demo001.domain.Products.Product;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@FxmlView("basket.fxml")
public class BasketController {

    private ObservableList<Product> orders = null;

    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField idOrderField, amountField;
    @FXML
    private TableView <Product> productsTable;
    @FXML
    private TableColumn <Product, Number> idProductColumn;
    @FXML
    private TableColumn <Product, String> productColumn;
    @FXML
    private TableColumn <Product, String> amountOfProductColumn; //czemu to jest string????
    @FXML
    private TableColumn deleteColumn;

    public Optional<ButtonType> init (FXMLLoader fxmlLoader, HashMap<Product, Integer> order, String title) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");

        BasketController basketController = fxmlLoader.getController();
        basketController.setStyling();
        //operacje na obiekcie sesjii
        //basketController.setBasketDetails(order);

        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }
    public List<Product> setBasketDetails(List<Product> order) {
        // Backend to do
        // Get details about the order - calculate them mostly
        // TODO: 07.06.2021  czy w koszyku jest już obliczana cena...? - przepływ programu
        // idOrderField.setText(Integer.toString(user.getId()));
        // amountField.setText(user.getName());
        idOrderField.setText("12");
        amountField.setText("250");

        // Set up the style
        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStylesheets().add("sample/styling/buttomView.css");
        productsTable.getStyleClass().add("tableview");
        deleteColumn.setStyle( "-fx-alignment: center;");

        orders = FXCollections.observableArrayList(order);

        idProductColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getProductId()));
        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        amountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString((int)cellData.getValue().getProductPrize()))); //??????

        Callback<TableColumn <Product, String>, TableCell <Product, String>> cellFactory = (param) -> {
            final TableCell<Product, String> cell = new TableCell<Product, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        final Button addButton = new Button("Delete");
                        addButton.setMinWidth(30);
                        addButton.setMinHeight(25);
                        addButton.getStyleClass().add("buttom");

                        addButton.setOnAction(event -> {
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("../confirmationBox.fxml"));
                            Optional<ButtonType> isConfirmed = null;
                            try {
                                isConfirmed = new ConfirmationBoxController().createConfirmation(fxmlLoader, "Are you sure you would like to delete that product ?", "Deleting product", "Ok", "Cancel");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(isConfirmed.get() == ButtonType.OK) {
                                // Backend to do
                                // Deleting the product from order
                                orders.remove(getIndex());
                                order.remove(getIndex());
                            }
                        });
                        setGraphic(addButton);
                    }
                    setText(null);
                };
            };
            return cell;
        };
        deleteColumn.setCellFactory(cellFactory);
        productsTable.setItems(orders);
        return orders;
    }

    public void setStyling() {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");
        okButton.setText("Finish order");
        Button cancelButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(1));
        cancelButton.getStyleClass().add("buttom");
        cancelButton.setText("Continue order");
    }

    private boolean validateFormData() {
        // Backend to do
        // Checking if the input is correct
        // TODO: 07.06.2021  co to robi, nawet używane nie jest aktualnie
        if (idOrderField.getText().isEmpty() || idOrderField.getText().equals("0")) {
            idOrderField.requestFocus();
            return false;
        }
        return true;
    }
}