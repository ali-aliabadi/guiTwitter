package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Main;
import model.PageLoader;
import model.Tweet.Tweet;
import model.User.Gender;
import model.User.User;
import model.tweetView;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class findprofile {

    @FXML
    private VBox v_box;

    @FXML
    private TextField usernamefield;

    private ArrayList<Button> btns = new ArrayList<Button>();


    @FXML
    void log_out() throws IOException {
        Main.myUser = null;
        new PageLoader().load("/view/Login.fxml");
    }

    @FXML
    void my_profile() throws IOException {
        new PageLoader().load("/view/Profile.fxml");
    }

    @FXML
    void search() throws IOException {
        String userid = usernamefield.getText();
        if (userid == null || userid.equals("")) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Search error");
            a.setHeaderText(null);
            a.setContentText("please input someones username to find him/her profile...\nyou can find everyones profile below too.");
            a.show();
        } else {
            MongoClient mongoClient = new MongoClient();
            MongoDatabase database = mongoClient.getDatabase("miniTweeter");
            MongoCollection<Document> collection = database.getCollection("Users");

            Document doc = collection.find(eq("username", userid)).first();

            if (doc == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Search error");
                a.setHeaderText(null);
                a.setContentText("user not found...\nplease input something else or find him/her down below.");
                a.show();
            } else {
                Main.anotherUser = new User(userid, doc.getString("password"));
                Main.anotherUser.setName(doc.getString("name"));
                Main.anotherUser.setEmail(doc.getString("email"));
                Main.anotherUser.setBio(doc.getString("bio"));
                if (doc.getString("gender").equals("MALE")) {
                    Main.anotherUser.setGender(Gender.MALE);
                } else {
                    Main.anotherUser.setGender(Gender.FEMALE);
                }
                Main.anotherUser.setTweets((ArrayList<Long>) doc.get("tweetsid"));
                Main.anotherUser.setFollowing((ArrayList<String>) doc.get("followings"));
                Main.anotherUser.setFollowers((ArrayList<String>) doc.get("followers"));

                new PageLoader().load("/view/anotherprofile.fxml");
            }

            mongoClient.close();
        }
        usernamefield.setText("");
    }

    @FXML
    void timeline() throws IOException {
        new PageLoader().load("/view/workplace.fxml");
    }

    @FXML
    void initialize() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        for (Document doc: collection.find()) {
            if (doc.getString("username").equals(Main.myUser.getId())) {
                continue;
            }
            addToTimeLine(doc);
        }

        mongoClient.close();
    }

    private void addToTimeLine(Document doc) {
        DialogPane di = new DialogPane();

        di.setContentText("name : " + doc.getString("name") + '\n' + "num of tweets : " + ((ArrayList<Long>)(doc.get("tweetsid"))).size()
                            + "  num of followers : " + ((ArrayList<String>)(doc.get("followers"))).size()
                            + "  num of followings : " + ((ArrayList<String>)(doc.get("followings"))).size());

        //////////////////////////////////////////////////////////////////////////////////////////////

        di.setHeaderText(Main.myUser.getId() + "\tlikes : " + myTweet.numberOfLikes()
                + "\tTweet id : " + myTweet.getId());
        di.setPrefWidth(496);
        di.setPrefHeight(125);
        di.setPadding(new Insets(10, 10, 10, 10));
        di.setStyle("-fx-border-color: black; -fx-background-color: #D0D3D4; -fx-border-width: 0px 0px 1.8px 0px; -fx-border-color:  #A9A9A9");

        Button like_btn = new Button();

        if (myTweet.userLiked.contains(Main.myUser.getId())) {
            like_btn.setText("unlike");
        } else {
            like_btn.setText("like");
        }

        tweetView myTweetView = new tweetView();
        myTweetView.button = like_btn;
        myTweetView.dialogPane = di;
        myTweetView.tweet = myTweet;

        tweetViews.add(myTweetView);

        like_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tweetView tv = null;
                for (tweetView t : tweetViews) {
                    if (t.button == like_btn) {
                        tv = t;
                    }
                }

                if (tv.tweet.userLiked.contains(Main.myUser.getId())) {
                    tv.tweet.deleteLikedUser(Main.myUser.getId());
                    unlikeTweet(tv.tweet);
                    tv.button.setText("like");
                } else {
                    tv.tweet.addUserLiked(Main.myUser.getId());
                    likeTweet(tv.tweet);
                    tv.button.setText("unlike");
                }
                tv.dialogPane.setHeaderText(tv.tweet.getCreatorId() + "\tlikes : " + tv.tweet.numberOfLikes()
                        + "\tTweet id : " + tv.tweet.getId());

            }
        });


        tweetViews.get(tweetViews.size() - 1).dialogPane.setGraphic(tweetViews.get(tweetViews.size() - 1).button);

        v_box.getChildren().add(0,tweetViews.get(tweetViews.size() - 1).dialogPane);
    }

}
