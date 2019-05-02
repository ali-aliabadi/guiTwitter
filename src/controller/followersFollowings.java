package controller;

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
import model.Info;
import model.Main;
import model.PageLoader;
import model.User.Gender;
import model.User.User;
import model.ProView;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class followersFollowings {

    @FXML
    private VBox vBox;

    @FXML
    private ArrayList<ProView> profiles = new ArrayList<ProView>();

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
    void timeLine() throws IOException {
        new PageLoader().load("/view/workPlace.fxml");
    }

    @FXML

    private void initialize() {

        switch (Info.getInfo()) {
            case "myFollowers":
                myFollowers();
                break;
            case "myFollowings":
                myFollowings();
                break;
            case "anotherFollowings":
                anotherFollowings();
                break;
            case "anotherFollowers":
                anotherFollowers();
                break;
        }
    }

    private void anotherFollowers() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        ArrayList<String> users = (ArrayList<String>) collection.find(eq("username", Main.anotherUser.getId())).first().get("followers");

        for (String id: users) {
            addUser(collection.find(eq("username", id)).first());
        }

        mongoClient.close();
    }

    private void anotherFollowings() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        ArrayList<String> users = (ArrayList<String>) collection.find(eq("username", Main.anotherUser.getId())).first().get("followings");

        for (String id: users) {
            addUser(collection.find(eq("username", id)).first());
        }

        mongoClient.close();
    }

    private void myFollowings() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        ArrayList<String> users = (ArrayList<String>) collection.find(eq("username", Main.myUser.getId())).first().get("followings");

        for (String id: users) {
            addUser(collection.find(eq("username", id)).first());
        }

        mongoClient.close();
    }

    private void myFollowers() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        ArrayList<String> users = (ArrayList<String>) collection.find(eq("username", Main.myUser.getId())).first().get("followers");

        for (String id: users) {
            addUser(collection.find(eq("username", id)).first());
        }

        mongoClient.close();
    }

    private void addUser(Document doc) {
        DialogPane di = new DialogPane();

        di.setContentText("name : " + doc.getString("name") + '\n' + "num of tweets : " + ((ArrayList<Long>)(doc.get("tweetsid"))).size()
                + "  num of followers : " + ((ArrayList<String>)(doc.get("followers"))).size()
                + "  num of followings : " + ((ArrayList<String>)(doc.get("followings"))).size());


        di.setHeaderText(doc.getString("username"));
        di.setPrefWidth(496);
        di.setPrefHeight(125);
        di.setPadding(new Insets(10, 10, 10, 10));
        di.setStyle("-fx-border-color: black; -fx-background-color: #D0D3D4; -fx-border-width: 0px 0px 1.8px 0px; -fx-border-color:  #A9A9A9");

        Button find_btn = new Button("find");
        User newUser = new User(doc.getString("username"), doc.getString("password"));

        newUser.setName(doc.getString("name"));
        newUser.setEmail(doc.getString("email"));
        newUser.setBio(doc.getString("bio"));
        if (doc.getString("gender").equals("MALE")) {
            newUser.setGender(Gender.MALE);
        } else {
            newUser.setGender(Gender.FEMALE);
        }
        newUser.setTweets((ArrayList<Long>) doc.get("tweetsid"));
        newUser.setFollowing((ArrayList<String>) doc.get("followings"));
        newUser.setFollowers((ArrayList<String>) doc.get("followers"));

        ProView pro = new ProView();

        pro.button = find_btn;
        pro.user = newUser;

        profiles.add(pro);

        find_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ProView prof = null;
                for (ProView b : profiles) {
                    if (b.button == find_btn) {
                        prof = b;
                        break;
                    }
                }
                assert prof != null;
                if (Main.myUser.getId().equals(prof.user.getId())) {
                    try {
                        new PageLoader().load("/view/Profile.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Main.anotherUser = prof.user;
                try {
                    new PageLoader().load("/view/anotherProfile.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        di.setGraphic(find_btn);

        vBox.getChildren().add(0,di);
    }

    @FXML
    void findProfile() throws IOException {
        new PageLoader().load("/view/findProfile.fxml");
    }

}
