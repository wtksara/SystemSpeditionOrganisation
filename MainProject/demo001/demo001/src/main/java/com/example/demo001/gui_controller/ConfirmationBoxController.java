package com.example.demo001.gui_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Optional;

public class ConfirmationBoxController {

    @FXML
    private Text alertText;
    @FXML
    private DialogPane dialogPane;

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
