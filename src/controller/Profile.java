package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import model.Main;
import model.PageLoader;
import org.bson.Document;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

public class Profile {


    @FXML
    private Text name_field;

    @FXML
    private Text email_field;

    @FXML
    private Text followings;

    @FXML
    private Text followers;

    @FXML
    private Text tweet;

    @FXML
    private Text bio_field;

    @FXML
    private Text usernamefield;

    @FXML
    private ScrollPane scroll_pane;

    @FXML
    void find_profile() {
    }

    @FXML
    void log_out() throws IOException {
        Main.myUser = null;
        new PageLoader().load("/view/Login.fxml");
    }

    @FXML
    void workplace() throws IOException {
        new PageLoader().load("/view/workplace.fxml");
    }

    @FXML
    private void initialize() {
        try {
            name_field.setText(Main.myUser.getName());
            email_field.setText(Main.myUser.getEmail());
            usernamefield.setText(Main.myUser.getId());
            String bio = Main.myUser.getBio();
            int len = bio.length();
            if (len > 45) {
                bio = bio.substring(0, 45) + "_\n" + bio.substring(45, len);
            }
            if (len + 2 > 90) {
                bio = bio.substring(0, 90) + "_\n" + bio.substring(90, len + 2);
            }
            if (len + 4 > 135) {
                bio = bio.substring(0, 135) + "_\n" + bio.substring(135, len + 4);
            }
            bio_field.setText(bio);
            followers.setText(String.valueOf(Main.myUser.getFollowersNumber()));
            followings.setText(String.valueOf(Main.myUser.getFollowingsNumber()));
            tweet.setText(String.valueOf(Main.myUser.getTweetsNumber()));

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> user_collection = database.getCollection("Users");
        MongoCollection<Document> tweet_collection = database.getCollection("Tweets");

        Document doc = user_collection.find(eq("username", Main.myUser.getId())).first();

        String[] tweets_id = (String[]) doc.get("tweetsId");

        for (int i = 0; i < tweets_id.length; i++) {
            Document doc1 = tweet_collection.find(eq("id", tweets_id[i])).first();

            DialogPane di = new DialogPane();

            di.setContentText((String) doc1.get("text"));
            di.setHeaderText((String) Main.myUser.getId() + "\tlikes : " + ((String[]) doc1.get("userslikedid")).length
                                + "\tTweet id : " + doc1.get("id") );
            //di.contentProperty();
        }
    }

}
