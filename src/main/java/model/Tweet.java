package model;

import java.util.Objects;

public class Tweet {
    String id;
    String createdAt;
    String retweetCount;
    String text;

    public Tweet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(String retweetCount) {
        this.retweetCount = retweetCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        return id.hashCode()
                * createdAt.hashCode()
                * retweetCount.hashCode()
                * text.hashCode()
                ;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tweet
                && obj.hashCode() == this.hashCode()
                && !(!Objects.equals(((Tweet) obj).getId(), this.getId())
                    || !Objects.equals(((Tweet) obj).getCreatedAt(), this.getCreatedAt())
                    || !Objects.equals(((Tweet) obj).getRetweetCount(), this.getRetweetCount())
                    || !Objects.equals(((Tweet) obj).getText(), this.getText())
        );
    }

    @Override
    public String toString() {
        return "Tweet #" + id + "\n" +
                "Created at: " + createdAt + "\n" +
                "Retweet count: " + retweetCount + "\n" +
                "Text: " + text + "\n";
    }
}
