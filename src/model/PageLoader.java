package model;


import controller.Profile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class PageLoader {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 487;
    private static Stage stage;

    public static void intiStage(Stage primarySrage) {
        stage = primarySrage;
        stage.setTitle("Mini Tweeter");
        stage.setResizable(false);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
    }

    public void load (String url) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(url));
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.show();
    }

    public void loadProfile() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Profile.fxml"));
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.show();
        new Profile().initialize();
    }
}
