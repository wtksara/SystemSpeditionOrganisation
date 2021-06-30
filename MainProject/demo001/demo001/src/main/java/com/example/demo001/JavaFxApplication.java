package com.example.demo001;


import com.example.demo001.gui_controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class JavaFxApplication extends Application {



    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        NavigationController.applicationContext = new SpringApplicationBuilder()
                .sources(Demo001SprinBootApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        FxWeaver fxWeaver = NavigationController.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(LoginController.class);
        Scene scene = new Scene(root);
        NavigationController.stage=stage;
        NavigationController.stage.setScene(scene);
        NavigationController.stage.show();
    }

    @Override
    public void stop() {
        NavigationController.applicationContext.close();
        Platform.exit();
    }

}