package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.domain.OrderManagement.OrderItem;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.service.OrderItemService;
import com.example.demo001.service.ProductOrderService;
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
import net.rgielen.fxweaver.core.FxWeaver;
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

    @Autowired
    private ProductOrderService productOrderService;

    @FXML
    private DialogPane dialogPane;

    // Order details
    @FXML
    private TextField idOrderField, statusField, clientField, transportField;
    @FXML
    private Label transportProviderLabel;
    @FXML
    private Button confirmButton;

    // Products details
    private ObservableList<OrderItem> products = null;
    @FXML
    private TableView<OrderItem> productsTable;
    @FXML
    private TableColumn<OrderItem, Number> idProductColumn;
    @FXML
    private TableColumn<OrderItem, String> productColumn;
    @FXML
    private TableColumn<OrderItem, String> amountOfProductColumn;
    @FXML
    private TableColumn<OrderItem, String> factoryColumn;

    public void initialize() {

        NavigationController.lastScene2 = NavigationController.lastScene;
        NavigationController.lastSceneName2 = NavigationController.lastSceneName;
        ProductOrder currentOrder = this.productOrderService.findOrderByID(NavigationController.selectedOrderId);
        NavigationController.selectedOrder = currentOrder;
        NavigationController.selectedOrderId = currentOrder.getOrderId();
        NavigationController.selectedOrderStatusName = currentOrder.getOrderStatus().name();
        NavigationController.selectedOrderClientName = currentOrder.getOrderClient().getUserName();
       // NavigationController.selectedOrderTransportProviderName = currentOrder.getOrderTransportProvider().getUserName();
        if (NavigationController.orderDetailsType == true) {
            //ProductOrder currentOrder = NavigationController.selectedOrder;

            // Backend to do
            // Getting details about products in order
        idOrderField.setText(Integer.toString((int) currentOrder.getOrderId()));
        statusField.setText(currentOrder.getOrderStatus().toString());
            clientField.setText(currentOrder.getOrderClient().getUserName());
            transportField.setText(currentOrder.getOrderTransportProvider().getUserName());
        // TODO: 28.06.2021  cena zamówienia

        //download list of orderitems from db
        products = FXCollections.observableArrayList(orderItemService.FindOrderItemsByOrder(NavigationController.selectedOrderId));

        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStyleClass().add("tableview");

        idProductColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int) cellData.getValue().getId()));
        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        amountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getProductAmount())));
            factoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
        productsTable.setItems(products);
        }
        else{
            ////////////////////////////////TU ZOBACZYĆ
            transportField.setVisible(false);
            transportProviderLabel.setVisible(false);
            confirmButton.setText("Create offer");
//
//             Backend to do
//             Getting details about products in order
            idOrderField.setText(Integer.toString((int) currentOrder.getOrderId()));
            statusField.setText(currentOrder.getOrderStatus().toString());
            clientField.setText(currentOrder.getOrderClient().getUserName());
            transportField.setText(currentOrder.getOrderTransportProvider().getUserName());
            /*idOrderField.setText(Long.toString(NavigationController.selectedOrder.getOrderId()));
            statusField.setText(NavigationController.selectedOrder.getOrderStatus().toString());
            clientField.setText(NavigationController.selectedOrder.getOrderClient().getUserName());
            transportField.setText(NavigationController.selectedOrder.getOrderTransportProvider().getUserName());*/
        // TODO: 28.06.2021  cena zamówienia

        //download list of orderitems from db
        products = FXCollections.observableArrayList(orderItemService.FindOrderItemsByOrder(NavigationController.selectedOrderId));

        productsTable.getStylesheets().add("sample/styling/tableView.css");
        productsTable.getStyleClass().add("tableview");

        idProductColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int) cellData.getValue().getId()));
        //productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
            productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        amountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getProductAmount())));
        factoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
        factoryColumn.setVisible(false);
        productsTable.setItems(products);

        }
        /*else{
            transportField.setVisible(false);
            transportProviderLabel.setVisible(false);
            confirmButton.setText("Create offer");
            //ProductOrder currentOrder = NavigationController.selectedOrder;

            // Backend to do
            // Getting details about products in order
//        idOrderField.setText(Integer.toString((int) currentOrder.getOrderId()));
//        statusField.setText(currentOrder.getOrderStatus().toString());
            //clientField.setText(currentOrder.getOrderClient().toString());
            //transportField.setText(currentOrder.getOrderTransportProvider().toString());
//        // TODO: 28.06.2021  cena zamówienia
//
//        //download list of orderitems from db
//        products = (ObservableList<OrderItem>) orderItemService.FindOrderItemsByOrder(currentOrder.getOrderId());
//
//        productsTable.getStylesheets().add("sample/styling/tableView.css");
//        productsTable.getStyleClass().add("tableview");
//
//        idProductColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int) cellData.getValue().getId()));
//        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
//        amountOfProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getProductAmount())));
////        factoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFactory().getFactoryName()));
            factoryColumn.setVisible(false);
//        productsTable.setItems(products);

        }*/
    }

    public void confirmButtonOnAction(ActionEvent event) throws IOException {
        if (NavigationController.orderDetailsType == true) {
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
        else{
            NavigationController.orderScreenToFront=3;
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(OrderManagerPanelController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Create offer");
            NavigationController.stage.show();

        }
    }

}