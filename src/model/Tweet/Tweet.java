package model.Tweet;


import model.Main;

import java.util.ArrayList;

public class Tweet {

    private long id;         // can only be number and its unic
    private String text;    // maximum 140 charectars
    public ArrayList<String> userLiked = new ArrayList<String>();
    private String creatorId;


    /**
     * constractors
     */

    public Tweet(long id, String text) {
        this.id = id;       // it is seted because the tweet ids start from 0 and new id is previousId + 1 and it is readen from db
        this.text = text;
        this.creatorId = Main.myUser.getId();
    }


    /**
     * setters and adder
     */

    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void addUserLiked(String userId) {
        this.userLiked.add(userId);
    }


    /**
     * getters and delleters
     */

    public long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public void deleteLikedUser(String userId) {
        this.userLiked.remove(userId);
    }

    public int numberOfLikes() {
        return this.userLiked.size();
    }

}