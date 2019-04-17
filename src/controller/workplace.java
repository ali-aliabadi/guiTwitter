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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Main;
import model.PageLoader;
import model.Tweet.Tweet;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

public class workplace {

    @FXML
    private TextArea tweet_text;

    @FXML
    private VBox v_box;

    private ArrayList<ToggleButton> toggleButtons = new ArrayList<ToggleButton>();
    private ArrayList<DialogPane> dialogPanes = new ArrayList<DialogPane>();
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();

    private ToggleButton btn;
    private DialogPane dialogPane;
    private Tweet tweetnew;

    @FXML
    void find_profile() {

    }

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
    void tweet() {
        String tweetText = tweet_text.getText();
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
            tweet_text.setText("");

            MongoClient mongoClient = new MongoClient();
            MongoDatabase database = mongoClient.getDatabase("miniTweeter");
            MongoCollection<Document> collection = database.getCollection("Tweets");
            MongoCollection<Document> collection1 = database.getCollection("Users");

            long newtweetid = collection.count();

            Tweet myTweet = new Tweet(newtweetid, tweetText);
            Document doc = createDocTweet(myTweet);

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
        di.setHeaderText(Main.myUser.getId() + "\tlikes : " + myTweet.numberOfLikes()
                + "\tTweet id : " + myTweet.getId());
        di.setPrefWidth(496);
        di.setPrefHeight(125);
        di.setPadding(new Insets(10, 10, 10, 10));
        di.setStyle("-fx-border-color: black; -fx-background-color: #D0D3D4; -fx-border-width: 0px 0px 1.8px 0px; -fx-border-color:  #A9A9A9");

        ToggleButton like_btn = new ToggleButton();

        like_btn.setText("Like");

        btn = like_btn;
        dialogPane = di;
        tweetnew = myTweet;

        like_btn.setOnMouseClicked(likeOnAction());

        di.setGraphic(like_btn);

        v_box.getChildren().add(di);
    }

    private EventHandler<? super MouseEvent> likeOnAction() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("\n\nbutton clicked...\n\n");
                System.out.println(btn);
                System.out.println(tweetnew);
                System.out.println(dialogPane);
                if (btn.isSelected()) {
                    System.out.println("btn is selected");
                    btn.setSelected(false);
                    System.out.println("set selected to false");
                    tweetnew.deleteLikedUser(Main.myUser.getId());
                    System.out.println("mongo works started");
                    unlikeTweet(tweetnew);
                    System.out.println("mongo works over");
                } else {
                    btn.setSelected(true);
                    tweetnew.addUserLiked(Main.myUser.getId());
                    likeTweet(tweetnew);
                }
                System.out.println("before set header text");
                dialogPane.setHeaderText(Main.myUser.getId() + "\tlikes : " + tweetnew.numberOfLikes()
                        + "\tTweet id : " + tweetnew.getId());
                System.out.println("after set header text");
            }
        };
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

    private Document createDocTweet(Tweet myTweet) {
        Document doc = new Document();

        doc.append("id", myTweet.getId());
        doc.append("text", myTweet.getText());
        doc.append("usersliked", Arrays.asList());

        return doc;
    }


}
