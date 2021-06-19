package com.example.demo001.gui_controller;

import com.example.demo001.controller.SimpleCtrl;
import com.example.demo001.domain.Actors.UserRole;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Products.ProductType;
import com.example.demo001.domain.Transport.Connection;
import com.example.demo001.domain.Transport.TransportProvider;
import com.example.demo001.repository.*;
import com.example.demo001.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
@FxmlView("main-form.fxml")
public class MainController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private TransportProviderRepository transportProviderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransportProviderService transportProviderService;


    @Autowired
    private BasicUserRepository basicUserRepository;

    @Autowired
    private ProductRepository productRepository;

    // private SomeService someService;


     @Autowired
     public MainController() {
     }


    @FXML
    private Button btnGetNumber ;

   /* @FXML
    private Button btnGetElement ;*/

    @FXML
    private TextField txtNumber;

    @FXML
    private TextField txtElementName;

    @FXML public void onBtnGetNumber (ActionEvent actionEvent) {






    }

    /*@FXML public void onBtnGetElement (ActionEvent actionEvent) {
        Element el = this.someService.Service2(1L);
        if (el  != null)

    }*/


}