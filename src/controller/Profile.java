package controller;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Info;
import model.Main;
import model.PageLoader;
import model.Tweet.Tweet;
import model.TweetView;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class Profile {


    @FXML
    private Text nameField;

    @FXML
    private Text emailField;

    @FXML
    private Text followings;

    @FXML
    private Text followers;

    @FXML
    private Text tweet;

    @FXML
    private Text bioField;

    @FXML
    private Text usernameField;

    @FXML
    private VBox vBox;

    private ArrayList<TweetView> tweetViews = new ArrayList<TweetView>();

    @FXML
    void findProfile() throws IOException {
        new PageLoader().load("/view/findProfile.fxml");
    }

    @FXML
    void logOut() throws IOException {
        Main.myUser = null;
        new PageLoader().load("/view/Login.fxml");
    }

    @FXML
    void workPlace() throws IOException {
        new PageLoader().load("/view/workPlace.fxml");
    }

    @FXML
    private void initialize() {
        nameField.setText(Main.myUser.getName());
        emailField.setText(Main.myUser.getEmail());
        usernameField.setText(Main.myUser.getId());
        String bio = Main.myUser.getBio();
        int len = bio.length();
        int maxLenght = PageLoader.HEIGHT / 9;
        if (len > maxLenght) {
            bio = bio.substring(0, maxLenght) + "_\n" + bio.substring(maxLenght, len);
        }
        if (len + 2 > maxLenght * 2) {
            bio = bio.substring(0, maxLenght * 2) + "_\n" + bio.substring(maxLenght * 2, len + 2);
        }
        if (len + 4 > maxLenght * 3) {
            bio = bio.substring(0, maxLenght * 3) + "_\n" + bio.substring(maxLenght * 3, len + 4);
        }
        bioField.setText(bio);
        followers.setText(String.valueOf(Main.myUser.getFollowersNumber()));
        followings.setText(String.valueOf(Main.myUser.getFollowingsNumber()));
        tweet.setText(String.valueOf(Main.myUser.getTweetsNumber()));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> tweet_collection = database.getCollection("Tweets");

        for (int i = 0; i < Main.myUser.getTweets().size(); i++) {

            Document doc = tweet_collection.find(eq("id", Main.myUser.getTweets().get(i))).first();

            Tweet myTweet = new Tweet((Long) doc.get("id"), doc.getString("text"));
            myTweet.userLiked = (ArrayList<String>) doc.get("usersliked");

            addToTimeLine(myTweet);
        }

        mongoClient.close();
    }

    private void unLikeTweet(Tweet myTweet) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Tweets");

        Document doc = collection.find(eq("id", myTweet.getId())).first();

        ArrayList<String> usersliked = (ArrayList<String>) doc.get("usersliked");
        usersliked.remove(Main.myUser.getId());

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("usersliked", usersliked));

        collection.updateOne(new BasicDBObject().append("id", myTweet.getId()), newDocument);

        mongoClient.close();
    }

    private void likeTweet(Tweet myTweet) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Tweets");

        Document doc = collection.find(eq("id", myTweet.getId())).first();

        ArrayList<String> usersliked = (ArrayList<String>) doc.get("usersliked");
        usersliked.add(Main.myUser.getId());

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("usersliked", usersliked));

        collection.updateOne(new BasicDBObject().append("id", myTweet.getId()), newDocument);

        mongoClient.close();
    }

    private void addToTimeLine(Tweet myTweet) {
        DialogPane di = new DialogPane();

        di.setContentText(myTweet.getText());
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

        TweetView myTweetView = new TweetView();
        myTweetView.button = like_btn;
        myTweetView.dialogPane = di;
        myTweetView.tweet = myTweet;

        tweetViews.add(myTweetView);

        like_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TweetView tv = null;
                for (TweetView t : tweetViews) {
                    if (t.button == like_btn) {
                        tv = t;
                    }
                }

                if (tv.tweet.userLiked.contains(Main.myUser.getId())) {
                    tv.tweet.deleteLikedUser(Main.myUser.getId());
                    unLikeTweet(tv.tweet);
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

        vBox.getChildren().add(0,tweetViews.get(tweetViews.size() - 1).dialogPane);
    }

    @FXML
    void followers() throws IOException {

        Info.setInfo("myFollowers");
        new PageLoader().load("/view/followersFollowings.fxml");

    }

    @FXML
    void followings() throws IOException {

        Info.setInfo("myFollowings");
        new PageLoader().load("/view/followersFollowings.fxml");

    }
}