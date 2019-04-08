package model.User;

import java.util.ArrayList;
import java.util.Scanner;
import model.Tweet.Tweet;

public class User {
    
    private String id;
    private String password;
    private String name;
    private String email;
    private String bio;

    private ArrayList <User> followers = new ArrayList <User>();

    private ArrayList <User> following = new ArrayList <User>();

    private ArrayList <Tweet> tweets = new ArrayList <Tweet>();

    private ArrayList <User> blockUsers = new ArrayList <User>();


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
         * email validation need here
         */

        this.email = email;
    }

    public void setBio(String bio) {

        /**
         * checking bio be less than 161 words needed here
         */

        this.bio = bio;
    }

    public void addFollowing(User user) {
        following.add(user);
    }

    public void addFollower(User user) {
        following.add(user);
    }

    public void addTweet(Tweet tweet) {
        tweets.add(tweet);
    }

    public void addBlockUser(User user) {
        blockUsers.add(user);
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

    public int getBlockUsersNumber() {
        return blockUsers.size();
    }

    public void deleteFollowing(User user) {
        int len = followers.size();
        for (int i = 0; i < len; i++) {
            if (user == followers.get(i)) {
                followers.remove(i);
                break;
            }
        }
    }

    public void deleteBlockUser(User user) {
        int len = blockUsers.size();
        for (int i = 0; i < len; i++) {
            if (user == blockUsers.get(i)) {
                blockUsers.remove(i);
                break;
            }
        }
    }


    /** other methodes */

    @Override
    public boolean equals(Object obj) {
        
        User user = (User) obj;
        return this.getId() == user.getId();
    }

    public void myProfile() {
        /**
         * this method must change
         */
        int flag = 0;
        System.out.println("My id : " + id);
        
        if (name != null) {
            System.out.println("Name : " + name);
            flag ++;
        }
        if (email != null) {
            System.out.println("Email : " + email);
            flag ++;
        }
        if (bio != null) {
            System.out.println("Bio : " + bio);
            flag ++;
        }

        System.out.println("Number of followers : " + followers.size());
        System.out.println("Number of followings : " + following.size());

        int len = tweets.size();
        for (int i = 0; i < len; i++) {
            tweets.get(i).print();
        }

        if (flag < 3) {
            System.out.println("Your profile is not complete, do you want to complete it now?(Y for yes and N for no)");
            Scanner in = new Scanner(System.in);
            if (in.next().toLowerCase().equals("y")) {
                this.completeProfile();
            }
        }
    }

    public void completeProfile() {
        /**
         * this method must change
         */
        Scanner in = new Scanner(System.in);
        if (name == null) {
            System.out.println("You dont have a name, want to set a name?(Y for yes and N for no)");
            if (in.nextLine().toLowerCase().equals("y")) {
                System.out.println("please input your full name");
                this.setName(in.nextLine());
            } 
        }
        if (email == null) {
            System.out.println("You dont have a email, want to set a email?(Y for yes and N for no)");
            if (in.nextLine().toLowerCase().equals("y")) {
                System.out.println("please input your email");
                this.setEmail(in.nextLine());
            } 
        }
        if (bio == null) {
            System.out.println("You dont have a bio, want to set a bio?(Y for yes and N for no)");
            if (in.nextLine().toLowerCase().equals("y")) {
                System.out.println("Please input your bio that have less than 160 character(spaces are counted)");
                String bio = in.nextLine();
                this.setBio(bio);
            } 
        }
    }

    public static void profileNotTweet(User user) {
        /**
         * this method must change
         */
        System.out.println("id : " + user.getId());
        int flag = 0;
        
        if (user.getName() != null) {
            System.out.println("Name : " + user.getName());
            flag ++;
        }
        if (user.getBio() != null) {
            System.out.println("Bio : " + user.getBio());
            flag ++;
        }

        System.out.println("Number of followers : " + user.getFollowersNumber());
        System.out.println("Number of followings : " + user.getFollowingsNumber());
        System.out.println("Number of tweets : " + user.getTweetsNumber());
        System.out.println("Number of blocked users : " + user.getBlockUsersNumber());

    }

    public void showFollowers() {
        for (User user : followers) {
            profileNotTweet(user);
        }
    }

    public void showFollowings() {
        for (User user : following) {
            profileNotTweet(user);
        }
    }

    public void showBlockUsers() {
        for (User user : blockUsers) {
            profileNotTweet(user);
        }
    }
}