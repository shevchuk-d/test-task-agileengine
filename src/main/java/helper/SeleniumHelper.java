package helper;

import model.Tweet;
import model.TweetElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SeleniumHelper {
    private static final Properties TEST_PROPERTIES = TestProperties.getProperties();
    private static final String WEB_LOGIN = TEST_PROPERTIES.getProperty("web.login");
    private static final String WEB_PASSWORD = TEST_PROPERTIES.getProperty("web.password");

    public List<Tweet> getTweets(int limit) {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver"
                , RequestHelper.class.getClassLoader().getResource("chromedriver.exe").getPath());
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://twitter.com/");
        driver.findElement(By.id("signin-email")).sendKeys(WEB_LOGIN);
        driver.findElement(By.id("signin-password")).sendKeys(WEB_PASSWORD);
        driver.findElement(By.className("password-signin")).findElement(By.className("submit")).click();
        List <WebElement> tweets = driver.findElements(By.className("tweet"));
        List<Tweet> webTweets = tweets.stream().limit(limit)
                .map(TweetElement::new)
                .map(TweetElement::getTweet).collect(Collectors.toList());
        driver.close();
        return webTweets;
    }
}
