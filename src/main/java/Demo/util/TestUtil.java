package Demo.util;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.github.javafaker.Faker;
import Demo.commands.WebCommands;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static Demo.base.TestBase.driver;

public class TestUtil {

    public static long Page_load_time = 60;
    public static long implicit_wait = 12;

    public String firstname;
    public String lastname;
    public static String shtr;

    public String full_name;
    public String RegNo;
    public static String plno;

    public String NameGenerator() {
        Faker faker = new Faker();
        firstname = faker.name().firstName();
        firstname = firstname.replaceAll("[^a-zA-Z0-9]", " ");
        lastname = faker.name().lastName();
        lastname = lastname.replaceAll("[^a-zA-Z0-9]", " ");
        full_name = firstname + " " + lastname;
        full_name = full_name.replaceAll("[^a-zA-Z0-9]", " ");
        return full_name;
    }

    public static String getTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
        return timeStamp;
    }

    public void GenerateRegNo() {
        int alpha1 = 'A' + (int) (Math.random() * ('Z' - 'A'));
        int alpha2 = 'A' + (int) (Math.random() * ('Z' - 'A'));
        // int alpha3 = 'A' + (int)(Math.random() * ('Z' - 'A'));
        int digit1 = (int) (Math.random() * 10);
        int digit2 = (int) (Math.random() * 10);
        int digit3 = (int) (Math.random() * 10);
        int digit4 = (int) (Math.random() * 10);
        RegNo = ("MH39" + (char) (alpha1) + ((char) (alpha2)) +
                +digit1 + digit2 + digit3 + digit4);
        System.out.println(RegNo + "IN test UTIL");
    }
    public static void moveToElement(WebElement element) {

        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();

    }
    public static void OvdUploadFile(String YourFileLocationFolder) {
        WebCommands.staticSleep(2000);
        driver.findElement(By.xpath("//input[@type='file']")).sendKeys( YourFileLocationFolder);
        WebCommands.staticSleep(1000);
    }

    public static void click(WebElement element, String msg) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        LogUtils.info(msg);
    }

    public static void sendKeys(WebElement element, String keys, String msg) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(keys);
        LogUtils.info(msg);
    }

    public static void clear(WebElement element, String msg) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.clear();
        LogUtils.info(msg);

    }

    public static void IsDisplayed(WebElement element, String msg) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.isDisplayed();
        LogUtils.info(msg);
    }

    public static void IsSelected(WebElement element, String msg) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.isSelected();
        LogUtils.info(msg);
    }

    public static void waitElementToBeClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitUntilTextToPresent(WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public static void waitUntilElementToBeVisible(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    public static void waitUntilInvisibilityOfElement(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(element)));
    }

    public static void waitUntilVisibilityOfElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void fluentWait(By element, String msg) {
        Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(10)).ignoring(Exception.class);
        wait.until(driver -> {
            System.out.println(msg);
            return driver.findElement(element);
        });
    }

    public static void scroll(WebElement element) {
        while (true) {
            if (!element.isDisplayed()) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollBy(0,800)", element);
                LogUtils.info("Successfully Scrolled Down by 300 pixels");
            } else {
                break;
            }
        }
    }

    public static void selectValueFromDropDown(@NotNull List<WebElement> elementList, String value) {
        elementList.stream().filter(obj -> obj.getText().equals(value)).findAny().ifPresent(element -> {
            waitElementToBeClickable(element);
            click(element, value + " selected");
        });
    }

    public static void utilUpdate() throws InterruptedException {
        WebCommands.staticSleep(2000);
        WebElement UpdateResultModelText = driver.findElement(By.xpath("//div[text()=' Are you done setting up your policy ?']"));
        WebElement UpdateResultsBtn = driver.findElement(By.xpath("//a[text()=' Update Results']"));

        Actions act = new Actions(driver);
        //   act.moveByOffset(10,0).perform();  //moves cursor to point (5,5)

        WebElement MoveElement = driver.findElement(By.xpath("//span[text()='Share Results']"));
        act.moveToElement(MoveElement).build().perform();

        WebCommands.staticSleep(1000);
        UpdateResultModelText.isDisplayed();
        UpdateResultModelText.isDisplayed();
        UpdateResultsBtn.click();
        WebCommands.staticSleep(3000);
    }

    public static void getScreenShot() {
        try {
            // Call getScreenshotAs method to create image file
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File f = scrShot.getScreenshotAs(OutputType.FILE);
            Allure.addAttachment("Screenshot " + getTimeStamp(), FileUtils.openInputStream(f));

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Attachment(value = "Page Screenshot", type = "image/png")
    public static void getFullPageScreenShot() throws IOException {
        byte[] t  = Shutterbug.shootPage(driver, Capture.FULL,true).getBytes();
        Allure.addAttachment("FULL SCREENSHOT " + getTimeStamp() , new ByteArrayInputStream(t));
    }

    public static String PastDate(int days) {
        LocalDateTime currentDateTime = LocalDateTime.now().minusDays(days);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTimeFormatter.format(currentDateTime);
    }

    public static String FutureDate(int days) {
        LocalDateTime currentDateTime = LocalDateTime.now().plusDays(days);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTimeFormatter.format(currentDateTime);

    }
    public static String payemntcompletedate(int days) {
        LocalDateTime currentDateTime = LocalDateTime.now().plusDays(days);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTimeFormatter.format(currentDateTime);

    }

    public static String PresentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTimeFormatter.format(currentDateTime);

    }

    public static String ninjaFutureDate(int days) {
        LocalDateTime currentDateTime = LocalDateTime.now().plusDays(days);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        return dateTimeFormatter.format(currentDateTime);
    }

    public static String generateRandomPolicyNo(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        plno = sb.toString();
        return plno;
    }
    public static String generateRandommobileNo(int len) {
        String chars = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        plno = sb.toString();
        return plno;
    }

    public static String getRandomTransactionNo() {
        // It will generate 7 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 7 character.
        return String.format("%07d", number);
    }

    public static String getRandomOtp() {
        // It will generate 4 digit random Number.
        // from 0 to 9999
        Random rnd = new Random();
        int number = rnd.nextInt(9999);

        // this will convert any number sequence into 7 character.
        return String.format("%04d", number);
    }

    public static void uploadFile(String YourFileLocationFolder) {
        WebCommands.staticSleep(2000);
   //     driver.findElement(By.xpath("//input[@type='file']")).sendKeys("/Users/" + YourFileLocationFolder + "/Downloads/dog.png");
        driver.findElement(By.xpath("//input[@type='file']")).sendKeys("/home/" + YourFileLocationFolder + "/storage/dog.png");
        WebCommands.staticSleep(1000);
    }

    public static String getRandomPhoneNumber(){
        Random rand = new Random();
        int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
        int num2 = rand.nextInt(743);
        int num3 = rand.nextInt(10000);

        DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
        DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

        String phoneNumber = df3.format(num1) + df3.format(num2) + df4.format(num3);

        return phoneNumber;
    }

    public static void LoginLess() {

       // driver.get(prop.getProperty("url"));
        driver.get(System.getProperty("url"));

        String strUrl = driver.getCurrentUrl();
        LogUtils.info("Opened Website with LoginLess: " + strUrl);

        String Current = driver.getWindowHandle();
        Set<String> AllHandles = driver.getWindowHandles();
        for (String x : AllHandles) {
            if (x.equals(Current)) {
                continue;
            }
            driver.switchTo().window(x).close();
        }
        driver.switchTo().window(Current);
    }


}

