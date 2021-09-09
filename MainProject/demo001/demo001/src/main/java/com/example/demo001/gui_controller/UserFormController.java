package com.example.demo001.gui_controller;

import com.example.demo001.Cipher;
import com.example.demo001.domain.Actors.Administrator;
import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Actors.UserRole;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Factory.Factory;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.domain.OrderManagement.OrderManager;
import com.example.demo001.domain.Transport.City;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.repository.CityRepository;
import com.example.demo001.repository.FactoryRepository;
import com.example.demo001.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.management.relation.Role;
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
    private TextField usernameField, roleField, uniField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button confirmButton, rejectButton;
    @FXML
    private Label uniFieldLabel;
    @FXML
    private DialogPane dialogPane;


    @Autowired
    private BasicUserService basicUserService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransportProviderService transportProviderService;
    @Autowired
    private FactoryManagerService factoryManagerService;
    @Autowired
    private OrderManagerService orderManagerService;
    @Autowired
    private FactoryService factoryService;
    @Autowired
    private CityService cityService;

    private Cipher cipher = new Cipher();

    public void initialize(){
        NavigationController.lastScene2 = NavigationController.lastScene;
        NavigationController.lastSceneName2 = NavigationController.lastSceneName;
        if(NavigationController.updatingUser)
        {
            BasicUser bUser = basicUserService.findByUsername(NavigationController.operatedUser);
            usernameField.setText(bUser.getUserName());
            passwordField.setText("");
            passwordField.setPromptText("New Password");
            roleField.setText(bUser.getUserRole().toString());
            confirmButton.setText("Save");
            rejectButton.setText("Reject");
            uniField.setVisible(true);
            uniFieldLabel.setVisible(true);
            switch(bUser.getUserRole())
            {
                case CLIENT:
                    Client client = clientService.findByUsername(bUser.getUserName());
                    uniFieldLabel.setText("City");
                    uniField.setText(client.getClientCity().getCityName());
                    break;
                case TRANSPORT_PROVIDER:
                    TransportProvider transportProvider = transportProviderService.findByUsername(bUser.getUserName());
                    uniFieldLabel.setText("Price For KM");
                    uniField.setText(String.valueOf(transportProvider.getPriceForKilometer()));
                    break;
                case FACTORY_MANAGER:
                    FactoryManager factoryManager = factoryManagerService.findByUsername(bUser.getUserName());
                    uniFieldLabel.setText("Factory");
                    uniField.setText(factoryManager.getManagedFactory().getFactoryName());
                    break;
                default:
                    uniField.setVisible(false);
                    uniFieldLabel.setVisible(false);
                    break;
            }

        }
        else
        {
            uniField.setVisible(false);
            uniFieldLabel.setVisible(false);
            confirmButton.setText("Next");
            rejectButton.setText("Go back");
        }
    }

    public void confirmButton() throws IOException{
        switch(confirmButton.getText())
        {
            case "Next":
                confirmButtonNext();
                break;

            case "Add user":
                confirmButtonAddUser();
                break;

            case "Save":
                confirmButtonSave();
                break;
        }
    }

    public void confirmButtonNext(){
        UserRole userRole=null;
        try{
            userRole = UserRole.valueOf(roleField.getText());}
        catch(IllegalArgumentException e)
        {
            NavigationController.alertText = "Enter proper Role";
            callAlertBox();
            return;
        }
        switch (userRole)
        {
            case FACTORY_MANAGER:
                uniField.setVisible(true);
                uniFieldLabel.setVisible(true);
                uniFieldLabel.setText("Factory");
                uniField.setPromptText("Factory name");
                confirmButton.setText("Add user");
                break;
            case CLIENT:
                uniField.setVisible(true);
                uniFieldLabel.setVisible(true);
                uniFieldLabel.setText("City");
                uniField.setPromptText("City name");
                confirmButton.setText("Add user");
                break;
            case TRANSPORT_PROVIDER:
                uniField.setVisible(true);
                uniFieldLabel.setVisible(true);
                uniFieldLabel.setText("Price For KM");
                uniField.setPromptText("Price For KM");
                confirmButton.setText("Add user");
                break;
            default:
                confirmButton.setText("Add user");
                uniField.setText("x");
                break;
        }
    }

    public void confirmButtonAddUser(){
        UserRole userRole=null;
        boolean everythingOK = true;
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || roleField.getText().isEmpty() || uniField.getText().isEmpty())
        {
            NavigationController.alertText = "None of the fields can be empty";
            callAlertBox();
            return;
        }
        BasicUser bUser = basicUserService.findByUsername(usernameField.getText());
        if(bUser!=null)
        {
            NavigationController.alertText = "Username is taken, choose another username";
            callAlertBox();
            return;
        }
        try{
            userRole = UserRole.valueOf(roleField.getText());}
        catch(IllegalArgumentException e)
        {
            NavigationController.alertText = "Enter proper Role";
            callAlertBox();
            return;
        }
        double priceForKm=0.0;
        City city=null;
        Factory factory=null;
        switch (userRole)
        {
            case TRANSPORT_PROVIDER:
                try{
                    priceForKm = Double.valueOf(uniField.getText());}
                catch(IllegalArgumentException e)
                {
                    NavigationController.alertText = "Enter proper price for KM";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                if(priceForKm<0)
                {
                    NavigationController.alertText = "Enter proper price for KM";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                break;
            case FACTORY_MANAGER:
                factory = factoryService.findByName(uniField.getText());
                if(factory==null)
                {
                    NavigationController.alertText = "No factory with such name";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                break;
            case CLIENT:
                city = cityService.FindCityByName(uniField.getText());
                if(city==null)
                {
                    NavigationController.alertText="No city with such name";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                break;
        }
        if(everythingOK)
        {
            String enc = cipher.encrypt(passwordField.getText());
            switch (userRole)
            {
                case FACTORY_MANAGER:
                    FactoryManager factoryManager = new FactoryManager();
                    factoryManager.setManagedFactory(factory);
                    factoryManager.setUserName(usernameField.getText());
                    factoryManager.setUserPassword(enc);
                    factoryManager.setUserRole(UserRole.FACTORY_MANAGER);
                    factoryManager.setActive(true);
                    factoryManagerService.addUser(factoryManager);
                    break;
                case CLIENT:
                    Client client = new Client();
                    client.setClientCity(city);
                    client.setUserName(usernameField.getText());
                    client.setUserPassword(enc);
                    client.setUserRole(UserRole.CLIENT);
                    client.setActive(true);
                    clientService.addUser(client);
                    break;
                case TRANSPORT_PROVIDER:
                    TransportProvider transportProvider = new TransportProvider();
                    transportProvider.setPriceForKilometer(priceForKm);
                    transportProvider.setUserName(usernameField.getText());
                    transportProvider.setUserPassword(enc);
                    transportProvider.setUserRole(UserRole.TRANSPORT_PROVIDER);
                    transportProvider.setActive(true);
                    transportProviderService.addUser(transportProvider);
                    break;
                case ORDER_MANAGER:
                    OrderManager orderManager = new OrderManager();
                    orderManager.setUserName(usernameField.getText());
                    orderManager.setUserPassword(enc);
                    orderManager.setUserRole(UserRole.ORDER_MANAGER);
                    orderManager.setActive(true);
                    orderManagerService.addUser(orderManager);
                    break;
                case ADMINISTRATOR:
                    BasicUser administrator = new Administrator();
                    administrator.setUserName(usernameField.getText());
                    administrator.setUserPassword(enc);
                    administrator.setUserRole(UserRole.ADMINISTRATOR);
                    administrator.setActive(true);
                    basicUserService.addUser(administrator);
                    break;
            }
            NavigationController.alertText="User added successfully";
            NavigationController.lastSceneName = NavigationController.lastSceneName2;
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);

            Parent root = fxWeaver.loadView(AdministratorPanelController.class);
            Scene scene = new Scene(root);
            NavigationController.lastScene = scene;
            NavigationController.manageUsersToFront=true;

            root = fxWeaver.loadView(AlertBoxController.class);
            scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Alert!");
            NavigationController.stage.show();
        }
    }

    public void confirmButtonSave(){
        UserRole userRole=null;
        boolean everythingOK = true;
        BasicUser operated = basicUserService.findByUsername(NavigationController.operatedUser);
        if (operated.getUserRole().equals(UserRole.ADMINISTRATOR) || operated.getUserRole().equals(UserRole.ORDER_MANAGER))
            uniField.setText("1");
        if(usernameField.getText().isEmpty() || roleField.getText().isEmpty() || uniField.getText().isEmpty())
        {
            NavigationController.alertText = "Only new password field can be empty! Fill in the rest of the fields";
            callAlertBox();
            return;
        }
        BasicUser bUser = basicUserService.findByUsername(usernameField.getText());
        if(bUser!=null && !usernameField.getText().equals(NavigationController.operatedUser))
        {
            NavigationController.alertText = "Username is taken, choose another username";
            callAlertBox();
            return;
        }
        try{
            userRole = UserRole.valueOf(roleField.getText());}
        catch(IllegalArgumentException e)
        {
            NavigationController.alertText = "Enter proper Role";
            callAlertBox();
            return;
        }
        double priceForKm=0.0;
        City city=null;
        Factory factory=null;
        switch (userRole)
        {
            case TRANSPORT_PROVIDER:
                try{
                    priceForKm = Double.valueOf(uniField.getText());}
                catch(IllegalArgumentException e)
                {
                    NavigationController.alertText = "Enter proper price for KM";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                if(priceForKm<0)
                {
                    NavigationController.alertText = "Enter proper price for KM";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                break;
            case FACTORY_MANAGER:
                factory = factoryService.findByName(uniField.getText());
                if(factory==null)
                {
                    NavigationController.alertText = "No factory with such name";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                break;
            case CLIENT:
                city = cityService.FindCityByName(uniField.getText());
                if(city==null)
                {
                    NavigationController.alertText="No city with such name";
                    everythingOK=false;
                    callAlertBox();
                    break;
                }
                break;
        }
        if(everythingOK)
        {
            String enc="";
            if(!passwordField.getText().isEmpty())
                enc = cipher.encrypt(passwordField.getText());
            switch (userRole)
            {
                case FACTORY_MANAGER:
                    FactoryManager factoryManager = factoryManagerService.findByUsername(NavigationController.operatedUser);
                    factoryManager.setManagedFactory(factory);
                    factoryManager.setUserName(usernameField.getText());
                    if(!enc.isEmpty())
                        factoryManager.setUserPassword(enc);
                    factoryManager.setUserRole(UserRole.FACTORY_MANAGER);
                    factoryManagerService.saveChangedUser(factoryManager);
                    break;
                case CLIENT:
                    Client client = clientService.findByUsername(NavigationController.operatedUser);
                    client.setClientCity(city);
                    client.setUserName(usernameField.getText());
                    if(!enc.isEmpty())
                        client.setUserPassword(enc);
                    client.setUserRole(UserRole.CLIENT);
                    clientService.saveChangedUser(client);
                    break;
                case TRANSPORT_PROVIDER:
                    TransportProvider transportProvider = transportProviderService.findByUsername(NavigationController.operatedUser);
                    transportProvider.setPriceForKilometer(priceForKm);
                    transportProvider.setUserName(usernameField.getText());
                    if(!enc.isEmpty())
                        transportProvider.setUserPassword(enc);
                    transportProvider.setUserRole(UserRole.TRANSPORT_PROVIDER);
                    transportProviderService.saveChangedUser(transportProvider);
                    break;
                case ORDER_MANAGER:
                    OrderManager orderManager = orderManagerService.findByUsername(NavigationController.operatedUser);
                    orderManager.setUserName(usernameField.getText());
                    if(!enc.isEmpty())
                        orderManager.setUserPassword(enc);
                    orderManager.setUserRole(UserRole.ORDER_MANAGER);
                    orderManagerService.saveChangedUser(orderManager);
                    break;
                case ADMINISTRATOR:
                    BasicUser administrator = basicUserService.findByUsername(NavigationController.operatedUser);
                    administrator.setUserName(usernameField.getText());
                    if(!enc.isEmpty())
                        administrator.setUserPassword(enc);
                    administrator.setUserRole(UserRole.ADMINISTRATOR);
                    basicUserService.saveChangedUser(administrator);
                    break;
            }
            NavigationController.alertText="User updated successfully";


            NavigationController.lastSceneName = NavigationController.lastSceneName2;
            FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);

            Parent root = fxWeaver.loadView(AdministratorPanelController.class);
            Scene scene = new Scene(root);
            NavigationController.lastScene = scene;
            NavigationController.manageUsersToFront=true;

            root = fxWeaver.loadView(AlertBoxController.class);
            scene = new Scene(root);
            NavigationController.stage.setScene(scene);
            NavigationController.stage.setTitle("Alert!");
            NavigationController.stage.show();
        }
    }

    public void rejectButton() throws IOException{
        NavigationController.stage.setScene(NavigationController.lastScene2);
        NavigationController.stage.setTitle(NavigationController.lastSceneName2);
        NavigationController.stage.show();
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

}

