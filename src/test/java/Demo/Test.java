package Demo;
import Demo.base.TestBase;
import Demo.pages.Pages.Test_Page;
import Demo.pages.login.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;


public class Test extends TestBase {
    public Test() {
        super();
    }


    LoginPage Loginpage;
    Test_Page Tl;


    @BeforeMethod()
    public void loginTest() throws InterruptedException {
          initialization();
    }

    public static void main(String[] args) {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys("tomsmith");
        driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.xpath("//i[@class=\"fa fa-2x fa-sign-in\"]")).click();

        driver.findElement(By.xpath("//button[text()='OK']")).click();



//Asserts
        // Locate heading
        WebElement heading = driver.findElement(By.xpath("//h2[text()='Secure Area']"));

        // Assertion
        Assert.assertTrue(heading.isDisplayed(), "Secure Area heading is NOT visible");

       driver.findElement(By.xpath("//i[text()=' Logout']")).click();


// Failed login
        driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys("TESTTEST");
        driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys("TESTETS");
        driver.findElement(By.xpath("//i[@class=\"fa fa-2x fa-sign-in\"]")).click();



        String errorText = driver.findElement(By.xpath("//div[@id=\"flash\"]")).getText();

        Assert.assertTrue(errorText.contains("Your username is invalid!"),
                "Error message mismatch");


    }



    @AfterMethod
    public void Close() {

        driver.quit();
    }
}


