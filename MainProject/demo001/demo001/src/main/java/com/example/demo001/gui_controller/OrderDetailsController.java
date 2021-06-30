package com.example.demo001.gui_controller;

import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;
import com.example.demo001.service.OrderItemService;
import com.example.demo001.service.ProductService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("orderDetails.fxml")
public class OrderDetailsController {

    @Autowired
    private OrderItemService orderItemService;

    @FXML
    private DialogPane dialogPane;

    // Order details
    @FXML
    private TextField idOrderField, companyField, amountField;

    // Products details
    private ObservableList<OrderItem> products = null;
    @FXML
    private TableView <OrderItem> productsTable;
    @FXML
    private TableColumn <OrderItem, Number> idProductColumn;
    @FXML
    private TableColumn <OrderItem, String> productColumn;
    @FXML
    private TableColumn <OrderItem, String> amountOfProductColumn;

    public Optional<ButtonType> init(FXMLLoader fxmlLoader, ProductOrder currentOrder, String title, boolean type) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");

        OrderDetailsController orderController = fxmlLoader.getController();
        orderController.setStyling(type);
        orderController.setOrderDetails(currentOrder);

        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }

    public void setOrderDetails(ProductOrder currentOrder) {
        // Backend to do
        // Getting details about products in order
        idOrderField.setText(Integer.toString((int)currentOrder.getOrderId()));
        companyField.setText(currentOrder.getOrderStatus().toString());
        // TODO: 09.06.2021  chyba najlepiej dać tą wyliczoną cenę jako pole po stworzeniu oferty przez order managera - zgadzam się
        //amountField.setText(user.getSurname());

        //download list of orderitems from db
        products = (ObservableList<OrderItem>) orderItemService.FindOrderItemsByOrder(currentOrder.getOrderId());

        //TODO - pobranie produktów z bazy danych
        //products = FXCollections.observableArrayList(user.getProducts());
        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStyleClass().add("tableview");

        idProductColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int)cellData.getValue().getId()));
        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
        amountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getProductAmount())));
        productsTable.setItems(products);
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