package controller;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Main;
import model.PageLoader;
import model.User.User;
import org.bson.Document;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField showConfirmPass;

    @FXML
    private TextField showPass;

    @FXML
    private Text passwordMatch;

    @FXML
    private Text usernameMatch;

    @FXML
    void hideConfirmPass() {
        confirmPassword.setVisible(true);
        showPass.setVisible(false);
    }

    @FXML
    void hidePass() {
        password.setVisible(true);
        showConfirmPass.setVisible(false);
    }

    @FXML
    void loadLoginPage() throws IOException {
        new PageLoader().load("/view/Login.fxml");
    }

    @FXML
    void registerUser() throws IOException {
        String pass1 = password.getText();
        String pass2 = confirmPassword.getText();
        if (pass1.equals(pass2)) {
            passwordMatch.setVisible(false);
            if (usernameField.getText().replace(" ", "").equals("") || usernameField.getText() == null) {
                return;
            }
            if (! userIdInDB(usernameField.getText())) {
                signUp(usernameField.getText(), pass1);
                new PageLoader().load("/view/completeProfile.fxml");
            } else {
                usernameMatch.setVisible(true);
            }
        } else {
            passwordMatch.setVisible(true);
            usernameMatch.setVisible(false);
        }
    }

    private boolean userIdInDB(String text) {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        return collection.find(eq("username", text)).first() != null;
    }

    @FXML
    void showConfirmPass() {
        showPass.setVisible(true);
        showPass.setText(confirmPassword.getText());
        confirmPassword.setVisible(false);
    }

    @FXML
    void showPass() {
        showConfirmPass.setVisible(true);
        showConfirmPass.setText(password.getText());
        password.setVisible(false);
    }

    static void signUp(String id, String pass) {
        Main.myUser = new User(id, pass);

    }

}
