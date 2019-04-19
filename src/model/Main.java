package model;

import javafx.application.Application;
import javafx.stage.Stage;
import model.User.User;


public class Main extends Application {

    public static User myUser = null;
    public static User anotherUser = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        PageLoader.intiStage(primaryStage);
        new PageLoader().load("/view/Login.fxml");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
