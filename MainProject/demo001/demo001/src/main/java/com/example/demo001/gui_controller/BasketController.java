package com.example.demo001.gui_controller;

import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

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
    private TableColumn <Product, String> amountOfProductColumn;
    @FXML
    private TableColumn deleteColumn;

    public void initialize() {
        setBasketDetails(NavigationController.listOfProducts);
    }

    public void setBasketDetails(List<Product> order) {
        // Backend to do
        // Get details about the order - calculate them mostly
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

                            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                            NavigationController.alertText ="Are you sure you would like to delete that product ?";
                            Parent root = fxWeaver.loadView(BasketController.class);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setTitle("Deleting product");
                            stage.show();

                            // SPRAWDZIC CZY TO GOWNO DZIALA <3

                            if (NavigationController.result){
                                NavigationController.result=false;
                                // Backend to do
                                // Deleting the product from order
                                orders.remove(getIndex());
                                order.remove(getIndex());
                            }
                            else{}
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
    }

    public void okButtonOnAction(ActionEvent event) throws IOException {
        // Backend
        NavigationController.basketResult = true;
        // close the window
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(ConfirmationBoxController.class);
        NavigationController.alertText="";
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Confirmation");
        NavigationController.stage.show();
    }


    public void cancelButtonOnAction(ActionEvent event) throws IOException {
        // do nothing and close the window
        NavigationController.basketResult = false;
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }


    private boolean validateFormData() {
        // Backend to do
        // Checking if the input is correct
        if (idOrderField.getText().isEmpty() || idOrderField.getText().equals("0")) {
            idOrderField.requestFocus();
            return false;
        }
        return true;
    }
}