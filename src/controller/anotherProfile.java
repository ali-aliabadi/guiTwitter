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

public class anotherProfile {

    @FXML
    private Text nameField;

    @FXML
    private Text emailField;

    @FXML
    private Text usernameField;

    @FXML
    private Text tweet;

    @FXML
    private Text followings;

    @FXML
    private Text followers;

    @FXML
    private Text bioField;

    @FXML
    private VBox vBox;

    @FXML
    private Button followButton;

    private ArrayList<TweetView> tweetViews = new ArrayList<TweetView>();

    @FXML
    void initialize() {

        if (Main.myUser.getId().equals(Main.anotherUser.getId())) {
            followButton.setVisible(false);
        }

        nameField.setText(Main.anotherUser.getName());
        usernameField.setText(Main.anotherUser.getId());
        emailField.setText(Main.anotherUser.getEmail());
        tweet.setText(String.valueOf(Main.anotherUser.getTweetsNumber()));
        followers.setText(String.valueOf(Main.anotherUser.getFollowersNumber()));
        followings.setText(String.valueOf(Main.anotherUser.getFollowingsNumber()));

        String bio = Main.anotherUser.getBio();
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

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");
        MongoCollection<Document> collection1 = database.getCollection("Tweets");

        Document doc = collection.find(eq("username", Main.anotherUser.getId())).first();
        if (((ArrayList<String>) (doc.get("followers"))).contains(Main.myUser.getId())) {

            followButton.setText("unFollow");

        } else {

            followButton.setText("Follow");

        }

        ArrayList<Long> tweets = (ArrayList<Long>) doc.get("tweetsid");

        for (Long idoftweet : tweets) {

            Document doc1 = collection1.find(eq("id", idoftweet)).first();
            Tweet myTweet = new Tweet(idoftweet, doc1.getString("text"));
            myTweet.setCreatorId(doc1.getString("creatorid"));
            myTweet.userLiked.addAll((ArrayList<String>) doc1.get("usersliked"));

            addTweets(myTweet);
        }

        mongoClient.close();

    }

    private void addTweets(Tweet myTweet) {
        DialogPane di = new DialogPane();

        di.setContentText(myTweet.getText());
        di.setHeaderText(Main.anotherUser.getId() + "  \tlikes : " + myTweet.numberOfLikes()
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
                    unlikeTweet(tv.tweet);
                    tv.button.setText("like");
                } else {
                    tv.tweet.addUserLiked(Main.myUser.getId());
                    likeTweet(tv.tweet);
                    tv.button.setText("unlike");
                }
                tv.dialogPane.setHeaderText(tv.tweet.getCreatorId() + "  \tlikes : " + tv.tweet.numberOfLikes()
                        + "\tTweet id : " + tv.tweet.getId());

            }
        });


        tweetViews.get(tweetViews.size() - 1).dialogPane.setGraphic(tweetViews.get(tweetViews.size() - 1).button);

        vBox.getChildren().add(0,tweetViews.get(tweetViews.size() - 1).dialogPane);
    }

    private void unlikeTweet(Tweet myTweet) {
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

    @FXML
    void find_profile() throws IOException {

        new PageLoader().load("/view/findProfile.fxml");

    }

    @FXML
    void followUser() {

        if (followButton.getText().equals("Follow")) {

            follow();
            followButton.setText("unFollow");

        } else {

            unfollow();
            followButton.setText("Follow");

        }

    }

    private void unfollow() {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        Document follower = collection.find(eq("username", Main.myUser.getId())).first();
        Document following = collection.find(eq("username", Main.anotherUser.getId())).first();

        ArrayList<String> currectUser = (ArrayList<String>) follower.get("followings");
        currectUser.remove(Main.anotherUser.getId());

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("followings", currectUser));
        collection.updateOne(new BasicDBObject().append("username", Main.myUser.getId()), newDocument);

        Main.myUser.addFollowing(Main.anotherUser.getId());

        ArrayList<String> anotherUser = (ArrayList<String>) following.get("followers");
        anotherUser.remove(Main.myUser.getId());

        BasicDBObject newDocument1 = new BasicDBObject();
        newDocument1.append("$set", new BasicDBObject().append("followers", anotherUser));
        collection.updateOne(new BasicDBObject().append("username", Main.anotherUser.getId()), newDocument1);

        Main.anotherUser.addFollower(Main.myUser.getId());

        mongoClient.close();

    }

    private void follow() {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        Document follower = collection.find(eq("username", Main.myUser.getId())).first();
        Document following = collection.find(eq("username", Main.anotherUser.getId())).first();

        ArrayList<String> currectUser = (ArrayList<String>) follower.get("followings");
        currectUser.add(Main.anotherUser.getId());

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("followings", currectUser));
        collection.updateOne(new BasicDBObject().append("username", Main.myUser.getId()), newDocument);

        Main.myUser.addFollowing(Main.anotherUser.getId());

        ArrayList<String> anotherUser = (ArrayList<String>) following.get("followers");
        anotherUser.add(Main.myUser.getId());

        BasicDBObject newDocument1 = new BasicDBObject();
        newDocument1.append("$set", new BasicDBObject().append("followers", anotherUser));
        collection.updateOne(new BasicDBObject().append("username", Main.anotherUser.getId()), newDocument1);

        Main.anotherUser.addFollower(Main.myUser.getId());

        mongoClient.close();
    }

    @FXML
    void log_out() throws IOException {

        Main.myUser = null;
        new PageLoader().load("/view/Login.fxml");

    }

    @FXML
    void workplace() throws IOException {

        new PageLoader().load("/view/workPlace.fxml");

    }

    @FXML
    void followers() throws IOException {

        Info.setInfo("anotherFollowers");
        new PageLoader().load("/view/followersFollowings.fxml");

    }

    @FXML
    void followings() throws IOException {

        Info.setInfo("anotherFollowings");
        new PageLoader().load("/view/followersFollowings.fxml");

    }

    @FXML
    void myProfile() throws IOException {

        new PageLoader().load("/view/Profile.fxml");

    }
}
