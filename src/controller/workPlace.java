package controller;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Main;
import model.PageLoader;
import model.Tweet.Tweet;
import model.TweetView;
import org.bson.Document;

import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class workPlace {

    @FXML
    private TextArea tweetText;

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
    void myProfile() throws IOException {
        new PageLoader().load("/view/Profile.fxml");
    }

    @FXML
    void tweet() {
        String tweetText = this.tweetText.getText();
        if (tweetText == null || tweetText.equals("")) {
            // creating empty alert
            Alert a = new Alert(Alert.AlertType.NONE);
            // set alert type
            a.setAlertType(Alert.AlertType.ERROR);
            // set alert title
            a.setTitle("Tweet text error");
            // set alert header to null
            a.setHeaderText(null);
            // set content text
            a.setContentText("Empty tweet text. please type something to tweet...");
            // show the dialog
            a.show();

        } else if (tweetText.length() > 140) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setTitle("Tweet text error");
            a.setHeaderText(null);
            a.setContentText("tweet text more than 140 characters, please type something less than or equal 140 charactars...");
            a.show();
        } else {
            this.tweetText.setText("");

            MongoClient mongoClient = new MongoClient();
            MongoDatabase database = mongoClient.getDatabase("miniTweeter");
            MongoCollection<Document> collection = database.getCollection("Tweets");
            MongoCollection<Document> collection1 = database.getCollection("Users");

            long newtweetid = collection.count();

            Tweet myTweet = new Tweet(newtweetid, tweetText);
            Document doc = createDocTweet(myTweet);

            Main.myUser.addTweet(newtweetid);

            collection.insertOne(doc);

            Document userdoc = collection1.find(eq("username", Main.myUser.getId())).first();
            ArrayList<Long> tweets1 = (ArrayList<Long>) userdoc.get("tweetsid");
            tweets1.add(newtweetid);

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("tweetsid", tweets1));

            collection1.updateOne(new BasicDBObject().append("username", Main.myUser.getId()), newDocument);

            mongoClient.close();

            addToTimeLine(myTweet);
        }
    }

    private void addToTimeLine(Tweet myTweet) {
        DialogPane di = new DialogPane();

        di.setContentText(myTweet.getText());
        di.setHeaderText(myTweet.getCreatorId() + "  \tlikes : " + myTweet.numberOfLikes()
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
                tv.dialogPane.setHeaderText(tv.tweet.getCreatorId() + "  \tlikes : " + tv.tweet.numberOfLikes()
                        + "\tTweet id : " + tv.tweet.getId());

            }
        });


        tweetViews.get(tweetViews.size() - 1).dialogPane.setGraphic(tweetViews.get(tweetViews.size() - 1).button);

        vBox.getChildren().add(0,tweetViews.get(tweetViews.size() - 1).dialogPane);

        /**
         * the border between tweets(dialog panes) are not showing
         * "For someone who is facing the same issue, the solution is just update your java SE from 8u40
         * to 8u60 or above. It is a bug in java SE https://bugs.openjdk.java.net/browse/JDK-8095678"
         * from : https://stackoverflow.com/questions/44860340/javafx-custom-dialogpane-having-border-color-shrinks-when-ever-a-key-is-pessed
         */
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

    private Document createDocTweet(Tweet myTweet) {
        Document doc = new Document();

        doc.append("id", myTweet.getId());
        doc.append("text", myTweet.getText());
        doc.append("creatorid", myTweet.getCreatorId());
        doc.append("usersliked", Arrays.asList());


        return doc;
    }

    @FXML
    private void initialize() {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");
        MongoCollection<Document> collection1 = database.getCollection("Tweets");

        ArrayList<Long> tweets = new ArrayList<Long>((ArrayList<Long>) collection.find(eq("username", Main.myUser.getId())).first().get("tweetsid"));

        for (String userid: Main.myUser.getFollowing()) {
            Document doc = collection.find(eq("username", userid)).first();
            tweets.addAll((ArrayList<Long>) doc.get("tweetsid"));
        }

        int len_tweets = tweets.size();
        long temp;

        for (int i = 0; i < len_tweets; i++) {
            for (int j = 0; j < len_tweets - i - 1; j++) {
                if (tweets.get(j) > tweets.get(j + 1)) {
                  temp = tweets.get(j);
                  tweets.set(j, tweets.get(j + 1));
                  tweets.set(j + 1, temp);
                }
            }
        }

        for (Long idoftweet: tweets) {

            Document doc = collection1.find(eq("id", idoftweet)).first();
            Tweet myTweet = new Tweet(idoftweet, doc.getString("text"));
            myTweet.setCreatorId(doc.getString("creatorid"));
            myTweet.userLiked.addAll((ArrayList<String>) doc.get("usersliked"));

            addToTimeLine(myTweet);
        }

        mongoClient.close();
    }
}