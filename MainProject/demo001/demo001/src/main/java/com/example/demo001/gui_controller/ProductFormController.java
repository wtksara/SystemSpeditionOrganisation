package com.example.demo001.gui_controller;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.service.FactoryManagerService;
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

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("productForm.fxml")
public class ProductFormController{ // extends ConfirmationBoxController

    @FXML
    private TextField idField, productField, amountField;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Button okButton, cancelButton;

    @Autowired
    private FactoryManagerService factoryManagerService;

    @Autowired
    private ProductionAbilityService productionAbilityService;

    private Factory sessionFactory;

    private void initializeFactory(){
        this.sessionFactory = this.factoryManagerService.findByUsername(NavigationController.username).getManagedFactory();
        System.out.println("this.sessionFactory: ");
        System.out.println(this.sessionFactory.getFactoryName());
    }

    public void okButtonOnAction(ActionEvent event) throws IOException {
        if(NavigationController.addProduct){
            NavigationController.productAmount = Integer.parseInt(amountField.getText());
            NavigationController.cart.put(NavigationController.product, NavigationController.productAmount);
            NavigationController.addProduct = false;
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }else {
            if (NavigationController.productionAbilityToUpdate == null) {
                NavigationController.alertText = "You didn't choose any product to update!";
                callAlertBox();
                return;
            }
            if ((amountField.getText().isEmpty()) || !isInteger(amountField.getText()) || Integer.parseInt(amountField.getText()) < 0) {
                // Backend
                // Update products
                //users.add(newUser);
                NavigationController.alertText = "Amount is not a valid number";
                callAlertBox();
                return;
            }
            NavigationController.productionAbilityToUpdate.setProductAmount(Integer.parseInt(amountField.getText()));
            this.productionAbilityService.updateProductionAbilityAmount(NavigationController.productionAbilityToUpdate);
            System.out.println("Zaktualizowano produkty");
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
    }

    public void cancelButtonOnAction(ActionEvent event) throws IOException {
        // do nothing and close the window
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    public Optional<ButtonType> init (FXMLLoader fxmlLoader, Integer amount, String title) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");

        ProductFormController productFormController = fxmlLoader.getController();
        productFormController.setStyling();
        productFormController.setProductDetails(amount);

        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }

    public void setProductDetails (Integer amount){
        //Variable used in lambda expression should be final or effectively final
        //TODO solution 1
        final Integer[] a = new Integer[1];
        Button okButton = (Button)dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION, event -> {
                    if (!validateFormData()) {
                        event.consume();
                        //TODO solution 2: can we return here? seems less weird
                        //return;
                    }
                    else {
                        //solution 1
                        a[0] = Integer.parseInt(amountField.getText());
                    }
                });
        //Solution 2
        //amount = a[0];
    }

    public void setStyling() {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");
        Button cancelButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(1));
        cancelButton.getStyleClass().add("buttom");
    }

    private boolean validateFormData() {
            if (idField.getText().isEmpty() || productField.getText().isEmpty()
                    || amountField.getText().isEmpty()){
                // Backend
                // Adding products to table
                NavigationController.alertText = "None of the fields can be empty";
                callAlertBox();
                return false;
            }
            if(isInteger(amountField.getText()) && Integer.parseInt(amountField.getText()) > 0){
                NavigationController.alertText = "Amount is not a valid number";
                callAlertBox();
                return false;
            }
            if(this.sessionFactory.getProducedProducts().stream().noneMatch(prodAb -> prodAb.getId() == Long.parseLong(idField.getText()))){
                NavigationController.alertText = "There is no product with such ID";
                callAlertBox();
                return false;
            }
            if(this.sessionFactory.getProducedProducts().stream().noneMatch(prodAb -> prodAb.getMyProduct().getProductName().equals(productField.getText())))
            {
                NavigationController.alertText = "There is no product with such name";
                callAlertBox();
                return false;
            }
            if (idField.getText().isEmpty() || idField.getText().equals("0")) {
                idField.requestFocus();
                return false;
            }
         else {
            amountField.requestFocus();
            return false;
        }
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

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}