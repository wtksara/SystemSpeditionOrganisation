package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Products.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;


import java.io.IOException;
import java.util.Optional;

public class OrderDetailsController {

    @FXML
    private DialogPane dialogPane;

    // Order details
    @FXML
    private TextField idOrderField, companyField, amountField;

    // Products details
    private ObservableList<Product> products = null;
    @FXML
    private TableView <Product> productsTable;
    @FXML
    private TableColumn <Product, Number> idProductColumn;
    @FXML
    private TableColumn <Product, String> productColumn;
    @FXML
    private TableColumn <Product, String> amountOfProductColumn;

    public Optional<ButtonType> init(FXMLLoader fxmlLoader, BasicUser BasicUser, String title, boolean type) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");

        OrderDetailsController orderController = fxmlLoader.getController();
        orderController.setStyling(type);
        orderController.setOrderDetails(BasicUser);

        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }

    public void setOrderDetails(BasicUser BasicUser) {
        // Backend to do
        // Getting details about products in order
        //idOrderField.setText(Integer.toString(BasicUser.getId()));
        //companyField.setText(BasicUser.getName());
        //amountField.setText(BasicUser.getSurname());

        //products = FXCollections.observableArrayList(BasicUser.getProducts());
        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStyleClass().add("tableview");

       // idProductColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
       // productColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        //amountOfProductColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        //productsTable.setItems(products);
    }

    public void setStyling(boolean type) {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");
        okButton.setText("Create a offer");
        if (type) okButton.setVisible(false);
        else okButton.setVisible(true);

        Button cancelButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(1));
        cancelButton.getStyleClass().add("buttom");
        cancelButton.setText("Close");
    }
}

