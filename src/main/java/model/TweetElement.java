package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TweetElement {
    String id;
    String createdAt;
    String retweetCount;
    String text;

    public TweetElement(WebElement webElement) {
        id = (null==webElement.getAttribute("data-retweet-id"))
                ? webElement.getAttribute("data-tweet-id")
                :webElement.getAttribute("data-retweet-id");
        createdAt = webElement.findElement(By.className("_timestamp")).getAttribute("data-time-ms");
        retweetCount = webElement.findElement(By.className("ProfileTweet-actionCountForPresentation")).getText();
        retweetCount = retweetCount.isEmpty() ? "0" :retweetCount;
        text = webElement.findElement(By.className("tweet-text")).getText();
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public String getText() {
        return text;
    }

    public Tweet getTweet(){
        Tweet tweet =  new Tweet();
        tweet.setId(id);
        tweet.setCreatedAt(createdAt);
        tweet.setRetweetCount(retweetCount);
        tweet.setText(text);
        return tweet;
    }
}
