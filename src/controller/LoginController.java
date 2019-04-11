package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.PageLoader;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button signup_btn;

    @FXML
    private Button login_btn;

    @FXML
    private TextField username_txt;

    @FXML
    private PasswordField password_txt;

    @FXML
    private Button showpass_btn;

    @FXML
    private Button enter_btn;

    @FXML
    private TextField showpass_txt;

    @FXML
    void enterworkplace(MouseEvent event) {

    }

    @FXML
    void hidePass(MouseEvent event) {

    }

    @FXML
    void loadsignuppage(MouseEvent event) {

    }

    @FXML
    void showPass(MouseEvent event) {

    }

    @FXML
    void showSignupPage(ActionEvent event) throws IOException {
        new PageLoader().load("/View/signup_page.fxml");
    }

    @FXML
    void showpasswordpress(MouseEvent event) {
        showpass_txt.setVisible(true);
        showpass_txt.setText(password_txt.getText());
        password_txt.setVisible(false);
    }

    @FXML
    void showpasswordrelease(MouseEvent event) {
        password_txt.setVisible(true);
        password_txt.setText(showpass_txt.getText());
        showpass_txt.setVisible(false);
    }

}
