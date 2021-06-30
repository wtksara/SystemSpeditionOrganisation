package com.example.demo001.gui_controller;

import com.example.demo001.domain.Actors.BasicUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("userForm.fxml")
public class UserFormController {

    // For changing the field in time of writing - If needed to use.
    // idField.textProperty().bindBidirectional(user.idProperty(), NumberFormat.getNumberInstance());
    // nameField.textProperty().bindBidirectional(user.nameProperty());
    // surnameField.textProperty().bindBidirectional(user.surnameProperty());

    @FXML
    private TextField idField, nameField, surnameField;
    @FXML
    private DialogPane dialogPane;

    public Optional<ButtonType> init (FXMLLoader fxmlLoader, BasicUser user, String title) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("sample/styling/buttomView.css");

        UserFormController userController = fxmlLoader.getController();
        userController.setStyling();
        userController.setUserDetails(user);

        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }
    public void setUserDetails(BasicUser user){
        // Backend to do
        // Getting details about users
        idField.setText(Long.toString(user.getUserId()));
        nameField.setText(user.getUserName());

        Button okButton = (Button)dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(
                ActionEvent.ACTION, event -> {
                    // Backend to do
                    // Checking if the input is correct
                    if (!validateFormData()) {
                        event.consume();
                    }
                    else {
                        // Backend to do
                        // Setting users details
                        user.setUserId(Integer.parseInt(idField.getText()));
                        user.setUserName(nameField.getText());
                    }
                });
    }

    public void setStyling() {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");

        Button cancelButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(1));
        cancelButton.getStyleClass().add("buttom");
    }

    private boolean validateFormData() {
        // Backend to do
        // Checking if the input is correct
        if (idField.getText().isEmpty() || idField.getText().equals("0")) {
            idField.requestFocus();
            return false;
        }
        return true;
    }
}

