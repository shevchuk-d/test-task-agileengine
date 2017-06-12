package model;

import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TweetDTO {
    String id;
    String created_at;
    String retweet_count;
    String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(String retweet_count) {
        this.retweet_count = retweet_count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Tweet getTweet(){
        Tweet tweet =  new Tweet();
        tweet.setId(id);
        String createdAt = String.valueOf(LocalDateTime.parse(this.created_at
                , DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss xxxx yyyy"))
                .atOffset(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli());
        tweet.setCreatedAt(createdAt);
        tweet.setRetweetCount(retweet_count);
        tweet.setText(text.startsWith("RT @") ? text.substring(text.indexOf(":")).trim() : text);
        return tweet;
    }
}
