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
@FxmlView("alertBox.fxml")
public class AlertBoxController {

    @FXML
    private Text alertText;
    @FXML
    private DialogPane dialogPane;


    public void initialize() {
        if (!NavigationController.alertText.isEmpty())
            alertText.setText(NavigationController.alertText);
    }

    public void confirmButton() throws IOException{
        NavigationController.alertText = "";
        NavigationController.stage.setScene(NavigationController.lastScene);
        NavigationController.stage.setTitle(NavigationController.lastSceneName);
        NavigationController.stage.show();
    }
}