package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import model.Main;
import model.PageLoader;

import java.io.IOException;

public class workplace {

    @FXML
    private TextArea tweet_text;

    @FXML
    private ImageView tweet;

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

}
