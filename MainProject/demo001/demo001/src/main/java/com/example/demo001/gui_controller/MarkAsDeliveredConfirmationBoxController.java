package com.example.demo001.gui_controller;

import com.example.demo001.domain.OrderManagement.OrderStatus;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.service.BasicUserService;
import com.example.demo001.service.ProductOrderService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@FxmlView("markAsDeliveredConfirmationBox.fxml")
public class MarkAsDeliveredConfirmationBoxController {

    @FXML
    private Text alertText;
    @FXML
    private DialogPane dialogPane;

    @Autowired
    private ProductOrderService productOrderService;

    public void initialize() {
        if (!NavigationController.alertText.isEmpty())
            alertText.setText(NavigationController.alertText);
    }

    public void confirmButton() throws IOException{
        ProductOrder order = productOrderService.findOrderByID(NavigationController.orderID);
        order.setOrderStatus(OrderStatus.DELIVERED);
        productOrderService.saveChangedOrder(order);
        NavigationController.alertText = "Order marked as delivered successfully";
        NavigationController.transportProviderScreenToFront=2;
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(TransportProviderPanelController.class);
        Scene scene = new Scene(root);
        NavigationController.lastScene = scene;

        root = fxWeaver.loadView(AlertBoxController.class);
        scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.setTitle("Alert!");
        NavigationController.stage.show();
    }

    public void rejectButton() throws IOException{
        NavigationController.stage.setScene(NavigationController.lastScene);
        NavigationController.stage.setTitle(NavigationController.lastSceneName);
        NavigationController.stage.show();
    }

}
