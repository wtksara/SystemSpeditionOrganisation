package com.example.demo001.gui_controller;

import com.example.demo001.domain.Products.Product;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FxmlView("basket.fxml")
public class BasketController {

    private ObservableList<CartWrapperController> orders = null;
    private List<CartWrapperController> tmpCart = new ArrayList<>();

    @FXML
    private DialogPane dialogPane;
    @FXML
    private TableView <CartWrapperController> productsTable;
    @FXML
    private TableColumn <CartWrapperController, Number> idProductColumn;
    @FXML
    private TableColumn <CartWrapperController, String> productColumn;
    @FXML
    private TableColumn <CartWrapperController, String> amountOfProductColumn;
    @FXML
    private TableColumn deleteColumn;
    /*
    Tu trzeba będzie się wyświetlić na nowo
    TAK JAK PRZY ADMINISTRATORZE !!!!!!!!!
     */
    public void initialize() {
        if(NavigationController.wantToDelete) {
            NavigationController.wantToDelete = false;
            // Backend to do
            // Deleting the product from order
            //orders.remove(NavigationController.ordersIndex);
            NavigationController.cart.remove(NavigationController.productToDelete.getProduct());
            setBasketDetails(NavigationController.cart);
        }
        else{
            setBasketDetails(NavigationController.cart);
        }
    }
    public void setBasketDetails(HashMap<Product, Integer> cart) {
        // Set up the style
        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStylesheets().add("sample/styling/buttomView.css");
        productsTable.getStyleClass().add("tableview");
        deleteColumn.setStyle( "-fx-alignment: center;");

        tmpCart.clear();

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
            tmpCart.add(new CartWrapperController(entry.getKey(), entry.getValue()));
        }

        orders = FXCollections.observableArrayList(tmpCart);

        idProductColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getProduct().getProductId()));
        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        amountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getProductAmount())));

        Callback<TableColumn <CartWrapperController, String>, TableCell <CartWrapperController, String>> cellFactory = (param) -> {
            final TableCell<CartWrapperController, String> cell = new TableCell<CartWrapperController, String>() {
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

                            NavigationController.ordersIndex = getIndex();
                            NavigationController.productToDelete = getTableView().getItems().get(getIndex());

                            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                            NavigationController.alertText ="Are you sure you would like to delete that product ?";
                            Parent root = fxWeaver.loadView(ConfirmationBoxController.class);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setTitle("Deleting product");
                            //NavigationController.lastScene = stage.getScene(); //zapis sceny otwieranego koszyka
                            NavigationController.wantToDelete = true;
                            stage.show();
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
}