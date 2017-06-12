package helper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import model.Error;
import model.Errors;
import model.Tweet;
import model.TweetDTO;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;


public class RequestHelper {

    private static final Properties TEST_PROPERTIES = TestProperties.getProperties();
    private static final String CONSUMER_KEY = TEST_PROPERTIES.getProperty("consumer.key");
    private static final String CONSUMER_SECRET = TEST_PROPERTIES.getProperty("consumer.secret");
    private static final String ACCESS_TOKEN = TEST_PROPERTIES.getProperty("access.token");
    private static final String ACCESS_SECRET = TEST_PROPERTIES.getProperty("access.secret");

    public OAuthConsumer getOathConsumer() throws IOException {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
                CONSUMER_SECRET);
        consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_SECRET);
        return consumer;
    }

    public HttpResponse doOauthRequest(String uri, String method) throws OAuthException, IOException {
        OAuthConsumer consumer = getOathConsumer();
        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase httpRequest;
        if (method.equals("POST")) httpRequest = new HttpPost(uri);
        else if (method.equals("PUT")) httpRequest = new HttpPut(uri);
        else if (method.equals("DELETE")) httpRequest = new HttpDelete(uri);
        else httpRequest = new HttpGet(uri);
        consumer.sign(httpRequest);
        return httpclient.execute(httpRequest);
    }


    public List<Tweet> getTweets(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        TweetDTO[] tweets = new Gson().fromJson(EntityUtils.toString(entity), TweetDTO[].class);
        return Arrays.stream(tweets).map(TweetDTO::getTweet).collect(Collectors.toList());
    }

    public Tweet getTweet(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        TweetDTO tweet = new Gson().fromJson(EntityUtils.toString(entity), TweetDTO.class);
        return tweet.getTweet();
    }

    public Errors getErrors(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        return new Gson().fromJson(EntityUtils.toString(entity), Errors.class);
    }


    public static void main(String[] args) throws IOException, OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
                CONSUMER_SECRET);
        consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_SECRET);
        String status = "encoding test";
        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode(status, "UTF-8");
        System.out.println(uri);
        HttpClient httpclient = new DefaultHttpClient();
        HttpEntity entity;
        HttpResponse response;

        HttpPost httpPost = new HttpPost(uri);
        consumer.sign(httpPost);
        response = httpclient.execute(httpPost);
        entity = response.getEntity();
        String e = EntityUtils.toString(entity);
        Errors errors = new Gson().fromJson(e, Errors.class);
        errors.getErrors().stream().map(Error::getMessage).forEach(System.out::println);
        System.out.println(e);
//
//        HttpPost delete = new HttpPost("https://api.twitter.com/1.1/statuses/destroy/" + new Gson().fromJson(e, TweetDTO.class).getId() + ".json");
////        HttpPost delete = new HttpPost("https://api.twitter.com/1.1/statuses/destroy/872745179610001408.json");
//        consumer.sign(delete);
//
//        response = httpclient.execute(httpPost);
//        entity = response.getEntity();
//        System.out.println(response.getStatusLine());
//        System.out.println(EntityUtils.toString(entity));
    }
}
