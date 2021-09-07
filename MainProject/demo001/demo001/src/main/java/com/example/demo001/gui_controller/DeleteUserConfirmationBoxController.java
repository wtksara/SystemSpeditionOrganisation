package com.example.demo001.gui_controller;

import com.example.demo001.service.BasicUserService;
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
@FxmlView("deleteUserConfirmationBox.fxml")
public class DeleteUserConfirmationBoxController {

    @FXML
    private Text alertText;
    @FXML
    private DialogPane dialogPane;

    @Autowired
    private BasicUserService basicUserService;

    public void initialize() {
        if (!NavigationController.alertText.isEmpty())
            alertText.setText(NavigationController.alertText);
    }

    public void confirmButton() throws IOException{
        basicUserService.deleteByUsername(NavigationController.operatedUser);
        NavigationController.alertText = "User deleted successfully";
        NavigationController.manageUsersToFront=true;
        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(AdministratorPanelController.class);
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