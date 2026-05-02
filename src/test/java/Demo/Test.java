package Demo;
import Demo.base.TestBase;
import Demo.pages.Pages.Test_Page;
import Demo.pages.login.LoginPage;
import org.testng.annotations.*;



public class Test extends TestBase {
    public Test() {
        super();
    }


    LoginPage Loginpage;
    Test_Page Tl;


    @BeforeMethod()
    public void loginTest() throws InterruptedException {
          initialization();
          Tl = new Test_Page();


        Loginpage.ValidateLogin(prop.getProperty("username"), prop.getProperty("pass"));
    }




    @AfterMethod
    public void Close() {
        driver.quit();
    }
}


