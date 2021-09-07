
package com.example.demo001.gui_controller;

        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.control.Button;
        import javafx.scene.control.ButtonType;
        import javafx.scene.control.Dialog;
        import javafx.scene.control.DialogPane;
        import javafx.scene.text.Text;
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

    public Optional<ButtonType> createAlert(FXMLLoader fxmlLoader, String message) throws IOException {
        DialogPane dialogPane = fxmlLoader.load();
        dialogPane.getStylesheets().add("demo001/src/main/resources/com/example/demo001/gui_controller/styling/buttomView.css");
        setUpController(fxmlLoader,message);
        Dialog<ButtonType> dialog = new Dialog <>();
        dialog.setDialogPane(dialogPane);
        return dialog.showAndWait();
    }

    public void setUpController(FXMLLoader fxmlLoader,String message){
        AlertBoxController alertBoxController = fxmlLoader.getController();
        alertBoxController.alertText.setText(message);
        alertBoxController.setStyling();
    }

    public void setStyling() {
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().get(0));
        okButton.getStyleClass().add("buttom");
    }

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