import helper.RequestHelper;
import helper.SeleniumHelper;
import model.Error;
import model.Errors;
import model.Tweet;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TwitterApiTest {

    @Test
    public void home_timeline_web() throws IOException, OAuthException {
        RequestHelper requestHelper = new RequestHelper();
        String uri = "https://api.twitter.com/1.1/statuses/home_timeline.json?count=1";
        HttpResponse response = requestHelper.doOauthRequest(uri, "GET");
        List<Tweet> apiTweets = requestHelper.getTweets(response);

        List<Tweet> webTweets = new SeleniumHelper().getTweets(apiTweets.size());

        Tweet apiTweet = apiTweets.get(0);
        Tweet webTweet = webTweets.get(0);

        assertEquals("Check tweets: \n"
                        + apiTweet
                        + "and\n"
                        + webTweet
                ,  apiTweet, webTweet);
    }

    @Test
    public void checkUpdate_api() throws IOException, OAuthException {
        RequestHelper requestHelper = new RequestHelper();
        String status = RandomStringUtils.random(140);

        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        HttpResponse responseCreation = requestHelper.doOauthRequest(uri, "POST");
        Tweet createdTweet = requestHelper.getTweet(responseCreation);

        String uriCheck = "https://api.twitter.com/1.1/statuses/home_timeline.json?count=1";
        HttpResponse responseCheck = requestHelper.doOauthRequest(uriCheck, "GET");
        List<Tweet> apiTweetsCheck = requestHelper.getTweets(responseCheck);
        Tweet apiTweetCheck = apiTweetsCheck.get(0);

        requestHelper.doOauthRequest("https://api.twitter.com/1.1/statuses/destroy/"
                + createdTweet.getId() + ".json", "POST");

        assertEquals("Check that status was updated", createdTweet, apiTweetCheck);
    }

    @Test
    public void checkUpdate_web() throws IOException, OAuthException {
        RequestHelper requestHelper = new RequestHelper();
        String status = RandomStringUtils.random(140);

        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        HttpResponse responseCreation = requestHelper.doOauthRequest(uri, "POST");
        Tweet createdTweet = requestHelper.getTweet(responseCreation);

        List<Tweet> webTweetsAfterUpdate = new SeleniumHelper().getTweets(1);
        Tweet webTweetAfterUpdate = webTweetsAfterUpdate.get(0);

        requestHelper.doOauthRequest("https://api.twitter.com/1.1/statuses/destroy/"
                + createdTweet.getId() + ".json", "POST");

        assertEquals("Check that status was updated", createdTweet, webTweetAfterUpdate);
    }

    @Test
    public void errorOnDuplicate_checkError_api() throws IOException, OAuthException {
        Error expectedError = new Error("187", "Status is a duplicate.");

        RequestHelper requestHelper = new RequestHelper();
        String status = RandomStringUtils.random(140);

        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        HttpResponse responseCreation = requestHelper.doOauthRequest(uri, "POST");
        Tweet createdTweet = requestHelper.getTweet(responseCreation);

        HttpResponse responseDuplicateCreation = requestHelper.doOauthRequest(uri, "POST");
        Errors errors = requestHelper.getErrors(responseDuplicateCreation);

        requestHelper.doOauthRequest("https://api.twitter.com/1.1/statuses/destroy/"
                                        + createdTweet.getId() + ".json", "POST");

        assertEquals("Check 'Status is a duplicate.' error", expectedError, errors.getErrors().get(0));
    }


    @Test
    public void errorOnDuplicate_checkStatusCode_api() throws IOException, OAuthException {
        int expectedStatusCode = 403;

        RequestHelper requestHelper = new RequestHelper();
        String status = RandomStringUtils.random(140);

        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        HttpResponse responseCreation = requestHelper.doOauthRequest(uri, "POST");
        Tweet createdTweet = requestHelper.getTweet(responseCreation);
        HttpResponse responseDuplicateCreation = requestHelper.doOauthRequest(uri, "POST");

        requestHelper.doOauthRequest("https://api.twitter.com/1.1/statuses/destroy/"
                                        + createdTweet.getId() + ".json", "POST");

        assertEquals("Check status code", expectedStatusCode
                , responseDuplicateCreation.getStatusLine().getStatusCode());
    }

    @Test
    public void checkDestroy_api() throws IOException, OAuthException {
        RequestHelper requestHelper = new RequestHelper();
        String status = RandomStringUtils.random(140);

        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        HttpResponse responseCreation = requestHelper.doOauthRequest(uri, "POST");
        Tweet createdTweet = requestHelper.getTweet(responseCreation);

        HttpResponse responseDestroying = requestHelper.doOauthRequest("https://api.twitter.com/1.1/statuses/destroy/"
                + createdTweet.getId() + ".json", "POST");
        Tweet destroyedTweet = requestHelper.getTweet(responseDestroying);

        assertEquals("Check tweet after destroying", createdTweet, destroyedTweet);
    }

    @Test
    public void errorOnDuplicate_checkError_web() throws IOException, OAuthException {
        RequestHelper requestHelper = new RequestHelper();
        String status = RandomStringUtils.random(140);

        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        HttpResponse responseCreation = requestHelper.doOauthRequest(uri, "POST");
        Tweet createdTweet = requestHelper.getTweet(responseCreation);

        requestHelper.doOauthRequest(uri, "POST");

        List<Tweet> webTweets = new SeleniumHelper().getTweets(1);
        Tweet webTweet = webTweets.get(0);

        requestHelper.doOauthRequest("https://api.twitter.com/1.1/statuses/destroy/"
                + createdTweet.getId() + ".json", "POST");

        assertEquals("Check that new tweet hasn't been created", createdTweet, webTweet);
    }


    @Test
    public void checkDestroy_web() throws IOException, OAuthException {
        RequestHelper requestHelper = new RequestHelper();
        String status = RandomStringUtils.random(140);

        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        HttpResponse responseCreation = requestHelper.doOauthRequest(uri, "POST");
        Tweet createdTweet = requestHelper.getTweet(responseCreation);

        List<Tweet> createdWebTweets = new SeleniumHelper().getTweets(1);
        Tweet createdWebTweet = createdWebTweets.get(0);

        requestHelper.doOauthRequest("https://api.twitter.com/1.1/statuses/destroy/"
                + createdTweet.getId() + ".json", "POST");

        List<Tweet> postDestroyingWebTweets = new SeleniumHelper().getTweets(1);
        Tweet postDestroyingWebTweet = postDestroyingWebTweets.get(0);

        assertNotEquals("Check tweet after destroying", createdWebTweet, postDestroyingWebTweet);
    }
}

