package com.example.demo001.gui_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("loginAlertBox.fxml")
public class AlertBoxController {

    @FXML
    private Text alertText;
    @FXML
    private DialogPane dialogPane;

    public static Optional<ButtonType> createAlert(FXMLLoader fxmlLoader, String message) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("demo001/src/main/resources/com/example/demo001/gui_controller/styling/buttomView.css");
        setUpController(fxmlLoader,message);
        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        return dialog.showAndWait();
    }

    public void goToLoginPanel() throws IOException{
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(LoginController.class);
        Scene scene = new Scene(root);
        NavigationController.stage.setScene(scene);
        NavigationController.stage.show();
    }

    public static void setUpController(FXMLLoader fxmlLoader,String message){
        AlertBoxController alertBoxController = fxmlLoader.getController();
        alertBoxController.alertText.setText(message);
        alertBoxController.setStyling();
    }

    public void setStyling() {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");
    }
}