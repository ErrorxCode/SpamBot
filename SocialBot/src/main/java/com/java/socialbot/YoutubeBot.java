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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import sun.misc.IOUtils;

import static java.lang.System.getenv;
import static java.lang.System.out;

public class YoutubeBot {
     WebDriver driver;
     ChromeOptions options;
     ArrayList<String> urls;

     protected int RESULTS = 10;
     protected boolean filter2month = true;
     protected boolean showBot = false;

    public void initialize(){
        System.setProperty("webdriver.chrome.driver",getCurrentDirectory() + "/chromedriver.exe");
        urls = new ArrayList<>();
        driver = new ChromeDriver();
        options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage").addArguments("--disable-gpu").addArguments("disable-infobars").addArguments("--disable-extensions");
        driver.manage().window().fullscreen();
    }

    public void login(String Username,String Password){
        driver.get("https://accounts.google.com/signin/v2/identifier?service=youtube&uilel=3&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26app%3Ddesktop%26hl%3Den%26next%3Dhttps%253A%252F%252Fwww.youtube.com%252F&hl=en&ec=65620&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
        WebElement username = driver.findElement(By.xpath("//*[@id=\"identifierId\"]"));
        username.sendKeys(Username);
        username.sendKeys(Keys.ENTER);
        waitTillVisible(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input"));
        WebElement password = driver.findElement(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input"));
        waitTillClickable(password);
        password.sendKeys(Password);
        password.sendKeys(Keys.ENTER);
        out.println("======================= Login Successful =======================");
    }

    public void getVideos(String file){
        ArrayList<String> keywords = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine())
                keywords.add(scanner.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            out.println("==========================\nKeywords.txt not found");
        }

        for (String url : keywords){
            String quarry = "https://www.youtube.com/results?search_query=" + url + "&sp=EgIIBA%253D%253D";
            driver.get(quarry);
            scrapVideos();
        }
        if (urls.size() == 0) {
            out.println("No video found, try different keywords");
            sleep(5);
            System.exit(-1);
        }
    }

    private void scrapVideos() {
        int limit = 0;
        List<WebElement> videos = driver.findElements(By.xpath("//*[@id=\"video-title\"]"));
        for (WebElement video : videos) {
            if (limit == RESULTS)
                break;
            String link = video.getAttribute("href");
            if (link != null){
                urls.add(link);
                limit++;
            }
        }
    }

    public void comment(String comment){
        for (String link : urls){
            driver.get(link);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            sleep(2);
            executor.executeScript("window.scrollTo(0, window.scrollY + 500)");
            wait(3);
            if (canComment()){
                waitTillVisible(By.cssSelector("#placeholder-area"));
                moveTo(driver.findElement(By.cssSelector("#placeholder-area")));
                WebElement commentBox = driver.findElement(By.cssSelector("#contenteditable-root"));
                commentBox.sendKeys(comment);
                commentBox.sendKeys(Keys.chord(Keys.CONTROL,Keys.ENTER));
                out.println("Commented on : " + link);
            } else {
                out.println("Video skipped : " + link);
            }
        }
        driver.quit();
        out.println("============== Commenting finished ==============");
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

    public void wait(int sec){
        driver.manage().timeouts().implicitlyWait(sec,TimeUnit.SECONDS);
    }

    public void moveTo(WebElement element){
        new Actions(driver).moveToElement(element).click(element).perform();
    }

    public void sleep(int sec){
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            out.print("Execution interrupted...");
            sleep(sec);
        }
    }

    public String getCurrentDirectory(){
        File file = new File(new File(".").getAbsolutePath());
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            out.print(" Error : Make sure all files are in same directory");
            return null;
        }
    }

    public void typeWrite(String text){
        char[] chars = text.toCharArray();
        for (char c : chars){
            out.print(c);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) {
        YoutubeBot bot = new YoutubeBot();
        Scanner scanner = new Scanner(System.in);
        bot.typeWrite("Welcome to youtube spam bot.\nThis tool make you noticeable among all the people on youtube. It will comment on the latest video based on your keywords but make sure not to make it so long , because youtube may detect this & can ban your account from commenting\n");
        out.println("Enter your username.");
        String username = scanner.nextLine();
        out.println("Enter your password. (never saved)");
        String password = scanner.nextLine();
        out.println("Enter your comment");
        String comment = scanner.nextLine();
        bot.initialize();
        bot.login(username,password);
        bot.sleep(5);
        bot.getVideos(bot.getCurrentDirectory() + "/Keywords.txt");
        bot.comment(comment);
    }
}