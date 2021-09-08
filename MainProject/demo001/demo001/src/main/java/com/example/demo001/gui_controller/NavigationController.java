package com.example.demo001.gui_controller;

import com.example.demo001.domain.Factory.ProductionAbility;
import com.example.demo001.domain.OrderManagement.ProductOrder;
import com.example.demo001.domain.Products.Product;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.List;

public class NavigationController {

    public static Stage stage;

    public static Scene lastScene;

    public static String lastSceneName;

    public static Scene lastScene2;

    public static String lastSceneName2;

    public static String username;

    public static String password;

    public static boolean deleteProducts = false;

    public static boolean updatingUser;

    public static String operatedUser;

    public static boolean manageUsersToFront; //info for administrator panel

    public static int transportProviderScreenToFront; //info for transport provider panel - 1 - Account, 2 - Route, 3 - Pending

    public static int factoryManagerScreenToFront; //info 1 - Account, 2 - My Factory, 3 - MyFactoryProducts

    public static Long orderID;

    public static ConfigurableApplicationContext applicationContext;

    public static String alertText;

    public static boolean deleteUser = false;

    public static boolean createOffer = false;

    public static boolean incorrectLogin = false;

    public static boolean duplicate = false;

    //public static boolean changeScreen;

    //public static ControllerTypes actualScreen;

    public static ProductOrder selectedOrder;

    public static String selectedOrderClientId;

    public static String selectedOrderClientName;

    public static String selectedOrderStatusName;

    public static String selectedOrderTransportProviderName;

    //public static ProductOrder newOrder;

    ////////////////////////////////Factory manager variables
    public static ProductionAbility productionAbilityToUpdate;

    //////////////////////////////Confirmation box variables (Order)
    public static boolean result;
    public static boolean summarySendOffer; //order manager -> confirmation box
    public static boolean goBack = false;

    ////////////////////////////// Client Panel + OrderManager Panel
    public static boolean orderDetailsType; // True - all details, False - without some details
    public static int orderScreenToFront; //1 - Account, 2 - Offer, 3 - Create the offer
    ////TEST SOLUTION
    public static long selectedOrderId;

    ////////////////////////Client Panel Reszta
    public static List<Product> listOfProducts;


    public static boolean basketResult = false;
    public static boolean notFinishedOrder = false;
    public static boolean emptyBasket = false;


    public static HashMap<Product, Integer> cart = new HashMap();
    public static int productAmount;
    public static Product product;
    public static boolean addProduct;
    public static int orderScreenToFrontClient;

}