package Demo.pages.Pages;

import Demo.base.TestBase;
import Demo.commands.WebCommands;
import Demo.util.LogUtils;
import Demo.util.TestUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;


public class Test_Page extends TestBase {
    public Test_Page() {
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//select[@data-test=\"product-sort-container\"]")
    WebElement selected;
    @FindBy(xpath = "//option[text()='Price (low to high)']")
    WebElement lowprice;
    @FindBy(xpath = "//button[@id=\"add-to-cart-sauce-labs-onesie\"]")
    WebElement T_Shirt;
    @FindBy(xpath = "//button[@id=\"add-to-cart-sauce-labs-bolt-t-shirt\"]")
    WebElement T_Shirt1;
    @FindBy(xpath = "//button[@id=\"add-to-cart-sauce-labs-backpack\"]")
    WebElement T_Shirt2;
    @FindBy(xpath = "//button[@id=\"add-to-cart-sauce-labs-bike-light\"]")
    WebElement T_Shirt3;
    @FindBy(xpath = "//button[@id=\"add-to-cart-sauce-labs-fleece-jacket\"]")
    WebElement T_Shirt4;
    @FindBy(xpath = "//a[@class=\"shopping_cart_link\"]")
    WebElement cart;
    @FindBy(xpath = "//button[@id=\"checkout\"]")
    WebElement Checkout;
    @FindBy(xpath = "//input[@id=\"first-name\"]")
    WebElement fname;
    @FindBy(xpath = "//input[@id=\"last-name\"]")
    WebElement Lname;
    @FindBy(xpath = "//input[@id=\"postal-code\"]")
    WebElement PIN;
    @FindBy(xpath = "//input[@id=\"continue\"]")
    WebElement Cont;
    @FindBy(xpath = "//div[@class=\"summary_total_label\"]")
    WebElement Total;
    @FindBy(xpath = "//button[@id=\"finish\"]")
    WebElement Finish;
    @FindBy(xpath = "//h2[text()='Thank you for your order!']")
    WebElement Finalorder;



    public void low_price()  {
        TestUtil.click(selected,"Selected filter");
        TestUtil.click(lowprice,"low to higt is Clicked.........");

    }
    public void Cart_Functionality(){
        TestUtil.click(T_Shirt," Added clicked 1");
        TestUtil.click(T_Shirt1," Added clicked 2");
        TestUtil.click(T_Shirt2,"Added clicked 3");
        TestUtil.click(T_Shirt3,"Added clicked 4");
        TestUtil.click(T_Shirt4,"Added clicked 5");
        WebCommands.staticSleep(1000);
        TestUtil.click(T_Shirt3,"Removed clicked 4");
        TestUtil.click(cart,"clicked cart");
    }
    public void checkout(){
      TestUtil.click(Checkout,"Clicked checkout");
      TestUtil.sendKeys(fname,"Demo","");
      TestUtil.sendKeys(Lname,"Demo","");
      TestUtil.sendKeys(PIN,"411004","");
      TestUtil.click(Cont,"Clicked Con");
      WebCommands.staticSleep(1000);

      String st = Total.getText();
      LogUtils.info(st);
      String op1 = "75.76";
      Assert.assertEquals(st,op1);
      TestUtil.click(Finish,"Clicked finish");

        String oderfinal = Finalorder.getText();
        LogUtils.info(oderfinal);
        String final_message = "Thank you for your order!";
        Assert.assertEquals(oderfinal,final_message);
    }
    public void EdgeCases(){
        TestUtil.click(cart,"clicked cart");
    }

}
