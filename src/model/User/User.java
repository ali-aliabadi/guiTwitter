package model.User;

import java.util.ArrayList;

public class User {
    
    private String id;
    private String password;
    private String name;
    private String email;
    private String bio;
    private Gender gender;

    private ArrayList <String> followers = new ArrayList <String>();

    private ArrayList <String> following = new ArrayList <String>();

    private ArrayList <Long> tweets = new ArrayList <Long>();


    /** constractors */

    public User(String id, String password) {
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

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // add setter for arraylists
    public void setTweets(ArrayList<Long> tweets) {
        this.tweets = tweets;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public void addFollowing(String userid) {
        following.add(userid);
    }

    public void addFollower(String userid) {
        following.add(userid);
    }

    public void addTweet(Long tweetid) {
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

    public Gender getGender() {
        return this.gender;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public ArrayList<Long> getTweets() {
        return tweets;
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

    @Override
    public String toString() {

        String strtweet = "";
        String strfollower = "";
        String strfollowing = "";

        for (Long str: this.tweets) {
            strtweet = (strtweet + str + ' ');
        }

        for (String str: this.followers) {
            strfollower = (strfollower + str + ' ');
        }

        for (String str: this.following) {
            strfollowing = (strfollowing + str + ' ');
        }

        String str = '[' + strtweet + "] [" + strfollower + "] [" + strfollowing + ']';

        return this.getId() + ' ' + this.getPassword() + ' ' + this.getName() + ' ' + this.email + ' ' + this.getBio() + ' ' + this.getGender().toString() + str;
    }

}