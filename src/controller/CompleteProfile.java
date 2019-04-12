package controller;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Main;
import model.PageLoader;
import model.User.Gender;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompleteProfile {

    @FXML
    private TextField userName;

    @FXML
    private TextField userEmail;

    @FXML
    private TextArea userBio;

    @FXML
    private RadioButton maleButton;

    @FXML
    private RadioButton femaleButton;

    @FXML
    private Text invalidemail;

    @FXML
    private Button enter;

    @FXML
    private Text uncompletetext;

    @FXML
    void enter() throws IOException {
        String name = userName.getText();
        String email = userEmail.getText();
        String bio = userBio.getText();
        if (maleButton.isSelected() || femaleButton.isSelected()) {
            if (name.equals("") || email.equals("") || bio.equals("")) {
                uncompletetext.setVisible(true);
                invalidemail.setVisible(false);
            } else {
                if (validate(email)) {
                    if (maleButton.isSelected()) {
                        completeProf(name, email, bio, Gender.MALE);
                    } else {
                        completeProf(name, email, bio, Gender.FEMALE);
                    }
                    new PageLoader().load("/view/workplace.fxml");
                } else {
                    invalidemail.setVisible(true);
                    uncompletetext.setVisible(false);
                }
            }
        } else {
            uncompletetext.setVisible(true);
            invalidemail.setVisible(false);
        }
    }

    @FXML
    void femaleClicked() {
        maleButton.setSelected(false);
    }

    @FXML
    void maleClicked() {
        femaleButton.setSelected(false);
    }

    /**
     *
     * @param emailStr : the string to check if it isvalid or not
     * @return : true if the string match the regex
     *
     * email regex to check if the email is valid or not
     * by the way it work fine with 99.0% of emails.
     * check : https://stackoverflow.com/questions/8204680/java-regex-email
     * "You will never end up with a valid expression."
     *  well, "Done is better than perfect."
     */
    public static boolean validate(String emailStr) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    static void completeProf(String name, String email, String bio, Gender gender) {
        Main.myUser.setName(name);
        Main.myUser.setEmail(email);
        Main.myUser.setBio(bio);
        Main.myUser.setGender(gender);

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        Document doc = new Document();

        doc.append("username", Main.myUser.getId());
        doc.append("password", Main.myUser.getPassword());
        doc.append("name", name);
        doc.append("email", email);
        doc.append("bio", bio);
        doc.append("gender", gender.toString());
        doc.append("tweetsid", Arrays.asList());
        doc.append("followers", Arrays.asList());
        doc.append("followings", Arrays.asList());

        collection.insertOne(doc);

    }

}
