package model.Tweet;


import java.util.ArrayList;
import java.util.Date;

public class Tweet {

    private int id;         // can only be number and its unic
    private String text;    // maximum 140 charectars
    private Date date;
    private ArrayList<String> userLiked = new ArrayList<String>();


    /**
     * constractors
     */

    public Tweet(int id, String text) {
        this.id = id;       // it is seted because the tweet ids start from 0 and new id is previousId + 1 and it is readen from db
        this.text = text;
        this.date = new Date();
    }


    /**
     * setters and adder
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserLiked(ArrayList<String> userLiked) {
        this.userLiked = userLiked;
    }

    public void addUserLiked(String userId) {
        this.userLiked.add(userId);
    }


    /**
     * getters and delleters
     */

    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public Date getDate() {
        return this.date;
    }

    public void deleteLikedUser(String userId) {
        int len = this.numberOfLikes();
        for (int i = 0; i < len; i++) {
            if (userId.equals(this.userLiked.get(i))) {
                this.userLiked.remove(i);
                break;
            }
        }
    }

    public int numberOfLikes() {
        return this.userLiked.size();
    }
}