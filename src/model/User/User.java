package model.User;

import java.util.ArrayList;

public class User {
    
    private String id;
    private String password;
    private String name;
    private String email;
    private String bio;

    private ArrayList <String> followers = new ArrayList <String>();

    private ArrayList <String> following = new ArrayList <String>();

    private ArrayList <Integer> tweets = new ArrayList <Integer>();


    /** constractors */

    User(String id, String password) {
        this.id = id;
        this.password = password;
    }
    
    /** setters and adders(add follower and add following) */

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        /**
         * checkhing of valid email needed
         */
        this.email = email;
    }

    public void setBio(String bio) {

        /**
         * checking bio be less than 161 words needed
         */

        this.bio = bio;
    }

    public void addFollowing(String userid) {
        following.add(userid);
    }

    public void addFollower(String userid) {
        following.add(userid);
    }

    public void addTweet(int tweetid) {
        tweets.add(tweetid);
    }

    /** getters and deleters(delete follower and delete following) */

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public int getFollowersNumber() {
        return followers.size();
    }

    public int getFollowingsNumber() {
        return following.size();
    }

    public int getTweetsNumber() {
        return tweets.size();
    }

    public void deleteFollowing(String userid) {

        int len = followers.size();
        for (int i = 0; i < len; i++) {
            if (userid.equals(followers.get(i))) {
                followers.remove(i);
                break;
            }
        }
    }

    /** other methodes */

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return this.getId().equals(user.getId());
    }

}