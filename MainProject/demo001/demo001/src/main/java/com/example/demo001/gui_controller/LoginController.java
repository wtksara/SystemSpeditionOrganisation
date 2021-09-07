package com.example.demo001.gui_controller;

import com.example.demo001.Cipher;
import com.example.demo001.service.BasicUserService;
import com.example.demo001.service.TransportProviderService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.gui_controller.AlertBoxController;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;


@Component
@FxmlView("login.fxml")
public class LoginController {

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @Autowired
    private BasicUserService basicUserService;

    private Cipher cipher = new Cipher();

    public void loginButtonOnAction() throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String enc = cipher.encrypt(password);

        BasicUser user = basicUserService.findByUsername(username);
        boolean passwordOK=false;

        if(user!=null)
        {
            if(user.getUserPassword().equals(enc))
                passwordOK=true;
        }
        if(passwordOK){
            if (user.getUserRole().toString().equals("ADMINISTRATOR")){
                NavigationController.manageUsersToFront=false;
                NavigationController.username=username;
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(AdministratorPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Administrator");
                NavigationController.stage.show();
            }
            else if(user.getUserRole().toString().equals("CLIENT")){ //client
                NavigationController.username=username;
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(ClientPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Client");
                NavigationController.stage.show();
            }
            else if(user.getUserRole().toString().equals("ORDER_MANAGER")){ //order manager
                NavigationController.username=username;
                NavigationController.orderScreenToFront = 1;
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(OrderManagerPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Order Manager");
                NavigationController.stage.show();
            }
            else if(user.getUserRole().toString().equals("FACTORY_MANAGER")){ //factory manager
                NavigationController.username=username;
                NavigationController.factoryManagerScreenToFront=1;
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(FactoryManagerPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Factory Manager");
                NavigationController.stage.show();
            }
            else if(user.getUserRole().toString().equals("TRANSPORT_PROVIDER")){ //transport provider
                NavigationController.username=username;
                NavigationController.transportProviderScreenToFront = 1;
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(TransportProviderPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Transport Provider");
                NavigationController.stage.show();
            }
        }
        else {
            NavigationController.alertText="Wrong username or password";
            NavigationController.lastSceneName="Login";
            NavigationController.lastScene = NavigationController.stage.getScene();
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(AlertBoxController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Warning!");
            NavigationController.stage.show();
        }
    }

}