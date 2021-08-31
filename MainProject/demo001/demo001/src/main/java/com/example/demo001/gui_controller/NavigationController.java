package com.example.demo001.gui_controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

public class NavigationController {

    public static Stage stage;

    public static Scene lastScene;

    public static String lastSceneName;

    public static Scene lastScene2;

    public static String lastSceneName2;

    public static String username;

    public static boolean updatingUser;

    public static String operatedUser;

    public static boolean manageUsersToFront; //info for administrator panel

    public static int transportProviderScreenToFront; //info for transport provider panel - 1 - Account, 2 - Route, 3 - Pending

    public static Long orderID;

    public static ConfigurableApplicationContext applicationContext;

    public static String alertText;

}