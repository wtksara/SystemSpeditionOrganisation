package com.example.demo001.gui_controller;

import com.example.demo001.service.SomeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@FxmlView("main-form.fxml")
public class MainController {

    @Autowired
    private SomeService someService;


    // private SomeService someService;


    // @Autowired
    // public MainController(SomeService someService) {
    //     this.someService = someService;
    // }


    @FXML
    private Button btnGetNumber ;

    @FXML
    private Button btnGetElement ;

    @FXML
    private TextField txtNumber;

    @FXML
    private TextField txtElementName;

    @FXML public void onBtnGetNumber (ActionEvent actionEvent) {
        txtNumber.setText(this.someService.Service1(5));
    }

    @FXML public void onBtnGetElement (ActionEvent actionEvent){} /*{
        Element el = this.someService.Service2(1L);
        if (el  != null)
            txtElementName.setText(el.getNameEl()) ;
    }*/


}