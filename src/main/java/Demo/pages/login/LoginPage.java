package Demo.pages.login;

import Demo.base.TestBase;
import Demo.util.LogUtils;
import Demo.util.TestUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends TestBase {

    @FindBy(xpath = "//input[@id=\"user-name\"]")
    WebElement uesrs;

    @FindBy(xpath = "//input[@id=\"password\"]")
    WebElement pass;

    @FindBy(xpath = "//input[@id=\"login-button\"]")
    WebElement login;




    public LoginPage() {
        PageFactory.initElements(driver, this);
    }

    public void ValidateLogin(String username, String otp) {
        String strUrl = driver.getCurrentUrl();
        LogUtils.info("Opened Website: " + strUrl);
        TestUtil.sendKeys(uesrs, username, "Mobile Number Entered");
        TestUtil.sendKeys(pass, otp, "OTP Entered");
        TestUtil.click(login, "Login Successful");
    }
}
