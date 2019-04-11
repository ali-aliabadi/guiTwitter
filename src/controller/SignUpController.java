package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.PageLoader;

import java.io.IOException;

public class SignUpController {

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmpassword;

    @FXML
    private TextField show_confirmpass;

    @FXML
    private TextField show_pass;

    @FXML
    void hideConfirmPass() {
        confirmpassword.setVisible(true);
        show_pass.setVisible(false);
    }

    @FXML
    void hidePass() {
        password.setVisible(true);
        show_confirmpass.setVisible(false);
    }

    @FXML
    void loadloginpage() throws IOException {
        new PageLoader().load("/view/Login.fxml");
    }

    @FXML
    void registeruser() {
        System.out.println(password.getText() + '\t' + confirmpassword.getText());
    }

    @FXML
    void showConfirmPass() {
        show_pass.setVisible(true);
        show_pass.setText(confirmpassword.getText());
        confirmpassword.setVisible(false);
    }

    @FXML
    void showPass() {
        show_confirmpass.setVisible(true);
        show_confirmpass.setText(password.getText());
        password.setVisible(false);
    }

}
