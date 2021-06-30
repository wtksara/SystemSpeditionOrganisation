package com.example.demo001.gui_controller;

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

    //NOWOSC
    public void loginButtonOnAction() throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();

        BasicUser user = basicUserService.findByUsername(username);
        boolean passwordOK=false;

        // Backend to do
        // Checking if details are alright
        if(user!=null)
        {
            if(user.getUserPassword().equals(password))
                passwordOK=true;
        }
        if(passwordOK){

            // Backend to do
            // by user name search for user and its role
            // if (basicUser.userRole.equals("ADMINISTRATOR")){
            if (user.getUserRole().toString().equals("ADMINISTRATOR")){
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(AdministratorPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Administrator");
                NavigationController.stage.show();
                NavigationController.username=username;
                //AdministratorPanelController aa = fxWeaver.loadController(AdministratorPanelController.class);
                //aa.setAdministratorDetails(user);
               /* FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../administratorPanel.fxml"));
                fxmlLoader.setLocation(getClass().getResource("../administratorPanel.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                AdministratorPanelController administratorPanelController = fxmlLoader.getController();
                stage.setTitle("Spedition Organisation System - Administrator");
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                administratorPanelController.setAdministratorDetails(user);*/
            }
            else if(user.getUserRole().toString().equals("CLIENT")){ //client
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(ClientPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Client");
                NavigationController.stage.show();
                NavigationController.username=username;

                /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../clientPanel.fxml"));
                fxmlLoader.setLocation(getClass().getResource("../clientPanel.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                ClientPanelController clientPanelController = fxmlLoader.getController();
                stage.setTitle("Spedition Organisation System - Client");
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                clientPanelController.setClientDetails(user);*/
            }
            else if(user.getUserRole().toString().equals("ORDER_MANAGER")){ //order manager
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(OrderManagerPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Order Manager");
                NavigationController.stage.show();
                NavigationController.username=username;

                /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../orderManagerPanel.fxml"));
                fxmlLoader.setLocation(getClass().getResource("../orderManagerPanel.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                OrderManagerPanelController orderManagerPanelController = fxmlLoader.getController();
                stage.setTitle("Spedition Organisation System - Order Manager");
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                orderManagerPanelController.setOrderManagerDetails(user);*/
            }
            else if(user.getUserRole().toString().equals("FACTORY_MANAGER")){ //factory manager
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(FactoryManagerPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Factory Manager");
                NavigationController.stage.show();
                NavigationController.username=username;

                /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../factoryManagerPanel.fxml"));
                fxmlLoader.setLocation(getClass().getResource("../factoryManagerPanel.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                FactoryManagerPanelController factoryManagerPanelController = fxmlLoader.getController();
                stage.setTitle("Spedition Organisation System - Factory Manager");
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                factoryManagerPanelController.setFactoryManagerDetails(user);*/
            }
            else if(user.getUserRole().toString().equals("TRANSPORT_PROVIDER")){ //transport provider
                FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
                Parent root = fxWeaver.loadView(TransportProviderPanelController.class);
                Scene scene = new Scene(root);
                NavigationController.stage.setScene(scene);
                NavigationController.stage.setTitle("Spedition Organisation System - Transport Provider");
                NavigationController.stage.show();
                NavigationController.username=username;

                /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../transportProviderPanel.fxml"));
                fxmlLoader.setLocation(getClass().getResource("../transportProviderPanel.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                TransportProviderPanelController transportProviderPanelController = fxmlLoader.getController();
                stage.setTitle("Spedition Organisation System - Transport Provider");
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                transportProviderPanelController.setTransportProviderDetails(user);*/
            }
        }
        else {
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(AlertBoxController.class);
            Scene scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Warning!");
            NavigationController.stage.show();
            //AlertBoxController.createAlert()


            /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../alertBox.fxml"));
            fxmlLoader.setLocation(getClass().getResource("../alertBox.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            AlertBoxController alertBoxController = fxmlLoader.getController();
            stage.setTitle("Alert");
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            alertBoxController.createAlert(fxmlLoader, "Incorrect password or login. Please try again.");*/
        }
    }

}





