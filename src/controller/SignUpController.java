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
    private TextField usernamefield;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmpassword;

    @FXML
    private TextField show_confirmpass;

    @FXML
    private TextField show_pass;

    @FXML
    private Text passwordmatch;

    @FXML
    private Text usernamematch;

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
    void registeruser() throws IOException {
        String pass1 = password.getText();
        String pass2 = confirmpassword.getText();
        if (pass1.equals(pass2)) {
            passwordmatch.setVisible(false);
            if (! userIdInDB(usernamefield.getText())) {
                signUp(usernamefield.getText(), pass1);
                new PageLoader().load("/view/CompleteProfile.fxml");
            } else {
                usernamematch.setVisible(true);
            }
        } else {
            passwordmatch.setVisible(true);
            usernamematch.setVisible(false);
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

    static void signUp(String id, String pass) {
        Main.myUser = new User(id, pass);

    }

}
