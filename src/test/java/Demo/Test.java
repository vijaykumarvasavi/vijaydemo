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


    String Currenturl = driver.getCurrentUrl();



    @BeforeMethod()
    public void loginTest() throws InterruptedException {
          initialization();
          Tl = new Test_Page();
    }


    // Test Scenarios(Login Tests)-------> 1
    public void Login_Tests() throws Exception
    {
        Loginpage.ValidateLogin(prop.getProperty("username"), prop.getProperty("pass"));
    }


    // Test Scenarios(Product Filtering)------------>2
    public void Low_price() throws Exception {
        Tl.low_price();
    }
    // Test Scenarios(Cart Functionality)------------>3
    public void Cart_Functionality(){
        Tl.Cart_Functionality();
    }
    // Test Scenarios(Checkout Process)------------>4
    public void Checkout_Process(){
        Tl.Cart_Functionality();
    }



    @AfterMethod
    public void Close() {
        driver.quit();
    }
}


