package com.example.demo001.gui_controller;

import com.example.demo001.service.ProductionAbilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("confirmationBox.fxml")
public class ConfirmationBoxController {

    @FXML
    private Text alertText;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Button okButton, cancelButton;
    @FXML
    private Button declineButton;

    @Autowired
    private ProductionAbilityService productionAbilityService;

    public void initialize() {
        if (!NavigationController.alertText.isEmpty()) {
            alertText.setText(NavigationController.alertText);
        }
        if(NavigationController.summarySendOffer){
            alertText.setText("Are you sure you would like to send that offer?");
        }
        if(NavigationController.basketResult){
            alertText.setText("You successfully placed the order!");
            declineButton.setDisable(true);
        }
        if(NavigationController.notFinishedOrder){
            alertText.setText("You haven't finished the order!");
        }
    }

    public void okButtonOnAction(ActionEvent event) throws IOException{

        if(NavigationController.duplicate){
            NavigationController.duplicate = false;
        }
        else if(NavigationController.incorrectLogin){
            NavigationController.incorrectLogin = false;
        }
        if(NavigationController.deleteUser) {
            // Backend
            // Deleting chosen user from table - przeniesione z AdministratorPanel
            /* int selectedId = usersTable.getSelectionModel().getSelectedIndex();
            //operowanie na kolekcji frontendowej
            users.remove(selectedId); */
            NavigationController.deleteUser = false;
            System.out.println("Usunieto usera");
        }
        else if(NavigationController.deleteProducts){
            // Backend
            // Deleting chosen products from table - przeniesione z FactoryManagerPanel
            /* int selectedId = usersTable.getSelectionModel().getSelectedIndex();
            users.remove(selectedId); */
            NavigationController.deleteProducts = false;
            System.out.println("Usunieto produkty");
        }
        System.out.println("Ok");
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    public void confirmButton(ActionEvent event) throws IOException{
        if (NavigationController.summarySendOffer){
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            NavigationController.result = true;
            NavigationController.alertText = "";
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(OrderManagerPanelController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.show();

            NavigationController.summarySendOffer = false;
        }
        else if(NavigationController.basketResult){
            ((Stage)NavigationController.oldClient.getWindow()).close();
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(ClientPanelController.class);
            NavigationController.alertText="";
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Spedition Organisation System - Client");
            NavigationController.stage.show();

            NavigationController.basketResult = false;
        }
        else if(NavigationController.notFinishedOrder){
            NavigationController.emptyBasket = true;
            NavigationController.notFinishedOrder = false;
            NavigationController.stage.close();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(ClientPanelController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Spedition Organisation System - Client");
            NavigationController.stage.show();
        }
        else if(NavigationController.wantToDelete){

            NavigationController.stage.close(); //zamkniecie koszyka

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(BasketController.class);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Basket");
            stage.show();
        }
        else if(NavigationController.deleteProducts){
            // Backend
            // Deleting chosen products from table - przeniesione z FactoryManagerPanel
            /*int selectedId = usersTable.getSelectionModel().getSelectedIndex();
            users.remove(selectedId);*/
            NavigationController.factoryManagerScreenToFront =3;
            this.productionAbilityService.deleteProductionAbility(NavigationController.productionAbilityToDelete);
            NavigationController.deleteProducts = false;
            NavigationController.alertText = "";
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(FactoryManagerPanelController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle(NavigationController.lastSceneName);
            NavigationController.stage.show();
            System.out.println("Usunieto produkty");
        }
        else{
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            NavigationController.result = true;
            NavigationController.alertText = "";
            NavigationController.stage.setScene(NavigationController.lastScene);
            NavigationController.stage.setTitle(NavigationController.lastSceneName);
            NavigationController.stage.show();
        }
    }

    public void declineButton(ActionEvent event)  throws IOException{
        if (NavigationController.summarySendOffer){
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            NavigationController.result = false;
            NavigationController.alertText = "";

            NavigationController.summarySendOffer = false;
            NavigationController.goBack = true;
        }
        else if(NavigationController.notFinishedOrder){
            NavigationController.emptyBasket = false;
            NavigationController.notFinishedOrder = false;
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
        else if(NavigationController.wantToDelete){
            NavigationController.wantToDelete = false;
        }
        else{
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            NavigationController.result = false;
            NavigationController.alertText = "";
            NavigationController.stage.setScene(NavigationController.lastScene);
            NavigationController.stage.setTitle(NavigationController.lastSceneName);
            NavigationController.stage.show();
        }
    }

    /*
    public void cancelButtonOnAction(ActionEvent event) throws IOException{
        // Do nothing and close stage
        System.out.println("Cancel");
        NavigationController.duplicate = false;
        NavigationController.incorrectLogin = false;
        NavigationController.deleteUser = false;
        NavigationController.deleteProducts = false;
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }*/

    public Optional<ButtonType> createConfirmation(FXMLLoader fxmlLoader, String message, String title, String textButton1, String textButton2) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");
        setUpController(fxmlLoader,message,textButton1,textButton2);
        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }

    public void setUpController(FXMLLoader fxmlLoader,String message, String textButton1, String textButton2){
        ConfirmationBoxController confirmationBoxController = fxmlLoader.getController();
        confirmationBoxController.alertText.setText(message);
        confirmationBoxController.setStyling(textButton1,textButton2);
    }

    public void setStyling(String textButton1, String textButton2) {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");
        okButton.setText(textButton1);
        Button cancelButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(1));
        cancelButton.getStyleClass().add("buttom");
        cancelButton.setText(textButton2);
    }

}