package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.PageLoader;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField username_txt;

    @FXML
    private PasswordField password_txt;

    @FXML
    private TextField showpass_txt;

    @FXML
    void enterworkplace() {

    }

    @FXML
    void hidePass() {
        password_txt.setVisible(true);
        password_txt.setText(showpass_txt.getText());
        showpass_txt.setVisible(false);
    }

    @FXML
    void loadsignuppage() throws IOException {
        new PageLoader().load("/view/signup_page.fxml");
    }

    @FXML
    void showPass() {
        showpass_txt.setVisible(true);
        showpass_txt.setText(password_txt.getText());
        password_txt.setVisible(false);
    }
}
