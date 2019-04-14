package controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import model.Main;
import model.PageLoader;

import java.io.IOException;

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

    public void initialize() {
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
            System.out.println("NullPointerException because we callthis methode from other class right aftercreating" +
                    "scene + we create a obj from this class there, But dont worry everythings works fine");
        }
    }

}
