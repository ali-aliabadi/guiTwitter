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
import model.User.Gender;
import model.User.User;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class LoginController {

    @FXML
    private TextField username_txt;

    @FXML
    private PasswordField password_txt;

    @FXML
    private TextField showpass_txt;

    @FXML
    private Text alertmsg;

    @FXML
    void enterworkplace() throws IOException {

        String username = username_txt.getText();
        String password = password_txt.getText();

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        Document doc = collection.find(eq("username", username)).first();

        if (doc != null) {
            if (doc.getString("password").equals(password)) {
                loginUser(doc);
                new PageLoader().load("/view/workplace.fxml");
            } else {
                // wrong password
                alertmsg.setText("Wrong password");
                alertmsg.setVisible(true);
            }
        } else {
            // user does not exist
            alertmsg.setText("User does not exist");
            alertmsg.setVisible(true);
        }

    }

    private void loginUser(Document doc) {
        Main.myUser = new User(doc.getString("username"), doc.getString("password"));
        Main.myUser.setName(doc.getString("name"));
        Main.myUser.setEmail(doc.getString("email"));
        Main.myUser.setBio(doc.getString("bio"));

        if (doc.getString("gender").equals("male")) {
            Main.myUser.setGender(Gender.MALE);
        } else {
            Main.myUser.setGender(Gender.FEMALE);
        }

        ArrayList<String> tweetsId = (ArrayList<String>) doc.get("tweetsid");
        ArrayList<String> followers = (ArrayList<String>) doc.get("followers");
        ArrayList<String> followings = (ArrayList<String>) doc.get("followings");

        Main.myUser.setTweets(tweetsId);
        Main.myUser.setFollowers(followers);
        Main.myUser.setFollowing(followings);

        // some test code
        /*
        System.out.println(doc.getDate("_id"));  // Error
        System.out.println(tweetsId); // print tweetsid list and when it is empty print '[]'
        System.out.println(tweetsId.get(0)); // Error when tweetsid is empty
        */
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
