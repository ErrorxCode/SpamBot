package com.java.socialbot;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import io.github.bonigarcia.wdm.WebDriverManager;

import static java.lang.System.out;

public class YoutubeBot {

     WebDriver driver;
     ArrayList<String> urls = new ArrayList<>();
     Preferences preferences;
     int RESULTS;
     int count;
     String filterTime;
     String username;
     String password;
     String comment;
     String spamUrl;
     boolean spam;
     boolean isSub;

    private void init(){
        System.err.flush();
        System.err.close();
        Properties properties = new Properties();
        preferences = Preferences.userRoot().node("SocialBot");
        try {
            properties.load(new FileInputStream(getCurrentDirectory() + "/Config.txt"));
            filterTime = properties.getProperty("FILTER_UPLOAD",null);
            spam = Boolean.parseBoolean(properties.getProperty("SPAM_1_VIDEO", String.valueOf(false)));
            RESULTS = Integer.parseInt(properties.getProperty("VIDEO_PER_SEARCH", String.valueOf(5)));
            count = Integer.parseInt(properties.getProperty("SPAM_1_VIDEO_COUNT", String.valueOf(10)));
            isSub = preferences.getBoolean("isSub",false);
            username = properties.getProperty("USERNAME","Enter your username");
            password = properties.getProperty("PASSWORD","Enter your password");
            comment = properties.getProperty("COMMENT","Get this bot now :- https://github.com/ErrorxCode/SocialBot");
            spamUrl = properties.getProperty("SPAM_VIDEO_URL","https://youtu.be/rSmyCK1nt7g").replace("www.","");
            String browser = properties.getProperty("BROWSER","chrome");


            if (RESULTS > 20)
                out.println("Error [ limit exceed ] : Please enter no. less then 20 for 'videosPerSearch'.");

            if (filterTime.contains("month"))
                filterTime = "&sp=EgQIBBAB";
            else if (filterTime.contains("week"))
                filterTime = "&sp=EgQIAxAB";
            else if (filterTime.contains("today"))
                filterTime = "&sp=EgQIAhAB";
            else if (filterTime.contains("last"))
                filterTime = "&sp=EgQIARAB";
            else {
                pause("[ Error ] : Incorrect configuration, please refer to configuration guide at README.md");
                System.exit(-1);
            }

            if (browser.equalsIgnoreCase("chrome"))
                WebDriverManager.chromedriver().setup();
            else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("Mozilla"))
                WebDriverManager.firefoxdriver().setup();
            else
                pause("Error : Invalid browser. Only Chrome & Firefox are supported !");

            if (!(spamUrl.startsWith("https://youtu.be/") || spamUrl.startsWith("https://youtube.com/")))
                pause("Error : Invalid youtube video url. Does this video even exist ? : " + spamUrl);



            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-dev-shm-usage")
                    .addArguments("--start-maximized")
                    .addArguments("--disable-infobars")
                    .addArguments("--disable-gpu")
                    .addArguments("--disable-extensions");

            driver = new ChromeDriver(options);
        } catch (IOException | NumberFormatException e) {
            if (e instanceof NumberFormatException)
                pause("Error : Invalid value found in Config.txt'. Expected number, Provided string !");
        }
    }

    private void subscribe() {
        out.println("Notice: By using our bot you are agree to subscribe our youtube channel. [y/n]");
        String ans = new Scanner(System.in).next();
        if (ans.equalsIgnoreCase("y")){
            out.println("Proceeding....");
            driver.get("https://www.youtube.com/channel/UCcQS2F6LXAyuE_RXoIQxkMA");
            wait(2);
            WebElement element = driver.findElement(By.xpath("//*[@id=\"subscribe-button\"]/ytd-subscribe-button-renderer/tp-yt-paper-button"));
            element.click();
            preferences.putBoolean("isSub",true);
            pause("[!] Please restart the bot !");
        } else {
            pause("You have to agree our Term of use in order to run bot.");
        }
    }

    private void login(){
        driver.get("https://accounts.google.com/signin/v2/identifier?service=youtube&uilel=3&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26app%3Ddesktop%26hl%3Den%26next%3Dhttps%253A%252F%252Fwww.youtube.com%252F&hl=en&ec=65620&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
        WebElement user = driver.findElement(By.xpath("//*[@id=\"identifierId\"]"));
        user.sendKeys(username);
        user.sendKeys(Keys.ENTER);
        waitTillVisible(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input"));
        WebElement pass = driver.findElement(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input"));
        waitTillClickable(pass);
        pass.sendKeys(password);
        pass.sendKeys(Keys.ENTER);
        out.println("======================= Login Successful =======================");

        if (!isSub)
            subscribe();
    }

    private void getVideos(){
        ArrayList<String> keywords = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(getCurrentDirectory() + "/Keywords.txt"));
            while (scanner.hasNextLine())
                keywords.add(scanner.nextLine());
        } catch (FileNotFoundException e) {
            pause("==========================\nKeywords.txt not found. Exiting....");
        }

        for (String url : keywords){
            String quarry = "https://www.youtube.com/results?search_query=" + url + filterTime;
            driver.get(quarry);
            scrapVideos();
        }
        if (urls.size() == 0) {
            pause("No video found, try different keywords. exiting...\nPress ENTER to exit.");
            System.exit(-1);
        }
    }

    private void scrapVideos() {
        int limit = 0;
        List<WebElement> videos = driver.findElements(By.xpath("//*[@id=\"video-title\"]"));
        for (WebElement video : videos) {
            out.println("Loop : " + limit);
            if (limit == RESULTS)
                break;
            String link = video.getAttribute("href");
            if (link != null){
                urls.add(link);
                limit++;
            } else {
                out.println("Link is null");
            }
        }
        out.println("Total links : " + urls.size());
    }

    private void comment(String link){
        driver.get(link);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, window.scrollY + 500)");
        wait(2);
        if (canComment()){
            waitTillVisible(By.cssSelector("#placeholder-area"));
            moveTo(driver.findElement(By.cssSelector("#placeholder-area")));
            WebElement commentBox = driver.findElement(By.cssSelector("#contenteditable-root"));
            commentBox.sendKeys(comment);
            commentBox.sendKeys(Keys.chord(Keys.CONTROL,Keys.ENTER));
            out.println("Commented on : " + link);
        } else {
            if (spam)
                pause("Error : Video is not commentable !");
            else
                out.println("Video skipped : " + link);
        }
    }

    private boolean canComment() {
        return  !driver.findElements(By.xpath("//*[@id=\"simple-box\"]/ytd-comment-simplebox-renderer")).isEmpty() &&
                 driver.findElements(By.xpath("//*[@id=\"contents\"]/ytd-message-renderer")).isEmpty() &&
                !driver.findElements(By.xpath("//*[@id=\"movie_player\"]")).isEmpty();
    }

    private void waitTillVisible(By element){
        new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(element));
    }

    private void waitTillClickable(WebElement element){
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(element));
    }

    private void wait(int sec){
        driver.manage().timeouts().implicitlyWait(sec,TimeUnit.SECONDS);
    }

    private void moveTo(WebElement element){
        new Actions(driver).moveToElement(element).click(element).perform();
    }

    private void sleep(int sec){
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            out.print("Execution interrupted. Exiting...");
            System.exit(-1);
        }
    }

    private String getCurrentDirectory(){
        File file = new File(new File(".").getAbsolutePath());
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            out.print(" Error : Make sure all files are in same directory");
            return null;
        }
    }

    private void typeWrite(String text){
        char[] chars = text.toCharArray();
        for (char c : chars){
            out.print(c);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.exit(-1);
            }
        }
    }

    private void pause(String msg){
        try {
            out.println(msg + " (. Press ENTER to exit) ");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
            out.println("Unexpected error occurred !");
        }
        driver.close();
        System.exit(-1);
    }

    public static void main(String[] args) {
        YoutubeBot bot = new YoutubeBot();
        bot.init();
        bot.login();
        bot.sleep(2);
        if (bot.spam) {
            for (int i = 0; i < bot.count; i++) {
                bot.comment(bot.spamUrl);
            }
            out.println("============== Commenting finished ==============");
        } else {
            bot.getVideos();
            for (String line : bot.urls)
                bot.comment(line);
            bot.driver.quit();
            out.println("============== Commenting finished ==============");
            bot.pause("Press Enter to exit..");
        }
    }
}