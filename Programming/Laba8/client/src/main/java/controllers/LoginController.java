package controllers;

import client.WindowManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import models.AuthModel;

import javax.swing.event.ChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXTextField login;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Hyperlink registerLink;
    @FXML
    private JFXButton yandex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            AuthModel.getInstance().login(login.getText(), password.getText());
        });

        registerLink.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            AuthModel.getInstance().switchToRegister();
        });

        yandex.setOnAction(event -> WindowManager.yandex());
    }
}
