package Demo.commands;


import Demo.base.TestBase;
import Demo.util.LogUtils;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

public class WebCommands {

    private static JavascriptExecutor jsExec;
    public WebDriverWait wait;
    public static boolean isScreenShotCaptured = false;
    public static String parentWindow = "";

    public WebCommands(WebDriver webDrv) {

        jsExec = (JavascriptExecutor) TestBase.driver;

    }

    private final String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
            + "var elementTop = arguments[0].getBoundingClientRect().top;"
            + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

    private final String scrollElementIntoView = "arguments[0].scrollIntoView(true);";

    public void navigate(String url) {

        try {

            TestBase.driver.manage().window().maximize();
            TestBase.driver.navigate().to(url);
            //Allure.step("Navigation to website with URL: " + url + " was successful", Status.PASSED);

            // return this.webDrv;

        } catch (Exception e) {
            getScreenShot();
            //Allure.step("Could not open website " + e.getMessage(), Status.FAILED);
            throw e;
            // return null;
        }
    }

    private void justClick(String locator) {
        waitForElementPresent(locator);
        //waitForElementVisible(locator);
        int retry = 0;
        boolean isClicked = false;
        WebElement element = waitElementToBeClickable(locator);
        while (element == null && retry++ <= 5) {
            try {
                LogUtils.info("Retry for click: " + retry);
                LogUtils.info("Clicking on element:" + locator);
                element = waitElementToBeClickable(locator);
                LogUtils.info("ENABLED: " + element.isEnabled() + ", getAttribute named disable: " + element.getAttribute("disable"));
                if (element.getAttribute("disable") != null)
                    throw new RuntimeException("Element is disabled");
                element.click();
                isClicked = true;
                LogUtils.info("Clicked on element:" + locator);
                LogUtils.info("Retry for click: Success on" + retry);
                break;
            } catch (Exception ex) {
            }
        }
        //final try
        if (!isClicked) {
            try {
                waitElementToBeClickable(locator).click();
            } catch (Exception es) {
                es.printStackTrace();
                throw new RuntimeException("element not clickable + " + locator);
            }
        }

    }

    private void clickError(Exception e, String locator) {
        e.printStackTrace();
        LogUtils.info("Error while clicking on element:" + e.getMessage());
        //Allure.step("Unable to click on element: " + element, Status.FAILED);
        getScreenShot();
//        Assert.assertEquals("",
//                e.toString() + "\nCould not click on element with locator: " + locator + "\n" + e.getMessage());
//

        Assert.assertEquals("", "Error while clicking on element with locator: " + locator);

    }

    public void click(String locator) {
        if (locator == null)
            throw new RuntimeException("Locator must not be null");
        try {
            justClick(locator);
            //Allure.step("Clicked on : " + element, Status.PASSED);
        } catch (StaleElementReferenceException e) {
            try {
                LogUtils.info("StaleElementReferenceException: occured " + locator);
                justClick(locator);
                LogUtils.info("StaleElementReferenceException: handled " + locator);
            } catch (Exception e1) {
                LogUtils.info("StaleElementReferenceException: not handled and came to catch" + locator);

                clickError(e1, locator);
                throw e1;
            }
        } catch (Exception e) {
            clickError(e, locator);
            throw e;
        }
    }

    public String getText(String locator) {

        WebElement element = null;
        String text = "";
        try {

            waitForElementPresent(locator);
            element = getElement(locator);
            text = element.getText();
            waitForAngularLoad();

        } catch (Exception e) {

            e.printStackTrace();
            LogUtils.info("Error while getting value from the element:" + e.getMessage());
            getScreenShot();
            Assert.assertEquals("", "Error while getting value from the element:" + locator);
            throw e;
        }

        return text;

    }


    public void JSscrollIntoMiddle(String locator) {
        try {
            executeJSScript(scrollElementIntoView, locator);
        } catch (Exception e) {
            executeJSScript(scrollElementIntoMiddle, locator);
        }
    }

    public void JSscrollTo(String locator) {
        try {
            executeJSScript(scrollElementIntoMiddle, locator);
        } catch (Exception e) {
            throw new RuntimeException("Can not Scroll to Element" + locator);
        }
    }


    public void doubleclick(String locator) {
        click(locator);
        click(locator);
    }

    public static void staticSleep(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public void executeJSScript(String script, String locator) {
        try {
            waitForElementPresent(locator);
            JavascriptExecutor jse = (JavascriptExecutor) TestBase.driver;
            WebElement element = getElement(locator);
            //  jse.executeAsyncScript(script, element);
            jse.executeScript(script, element);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.info("Execute Javascript: ERROR");
            getScreenShot();
            throw e;
        }
    }

    public void executeJSScript(String script) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) TestBase.driver;
            //  jse.executeAsyncScript(script, element);
            jse.executeScript(script);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.info("Execute Javascript: ERROR");
            getScreenShot();
            throw e;
        }
    }


    public void JSClick(String locator) {
        try {
            waitForElementPresent(locator);
            LogUtils.info("JS Click on element:" + locator);
            JavascriptExecutor jse = (JavascriptExecutor) TestBase.driver;
            WebElement element = getElement(locator);
            jse.executeScript("arguments[0].click();", element);
            LogUtils.info("JS Clicked on element:" + locator);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.info("JSClick: ERROR");
            getScreenShot();
            throw e;
        }
    }

    public void JSClickList(String locator, int count, int total) {
        try {

            waitForElementPresent(locator);
            LogUtils.info("JS Click on element:" + locator);
            JavascriptExecutor jse = (JavascriptExecutor) TestBase.driver;
            List<WebElement> element = getElements(locator);
            LogUtils.info("Element size:" + element.size());
            if (total > element.size()) {
                int i = total - element.size();
                int index = count % i;
                jse.executeScript("arguments[0].click();", element.get(index));
                LogUtils.info("JS Clicked on element:" + locator + " at index:" + index);
            } else {
                jse.executeScript("arguments[0].click();", element.get(count));
                LogUtils.info("JS Clicked on element:" + locator + " at index:" + count);
            }


        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.info("JSClick: ERROR");
            getScreenShot();
            throw e;
        }
    }


    public void switchWindow() {
        parentWindow = TestBase.driver.getWindowHandle();
        Iterator<String> windows = TestBase.driver.getWindowHandles().iterator();
        while (windows.hasNext()) {
            String newWindow = windows.next();
            TestBase.driver.switchTo().window(newWindow);
        }
        //Allure.step("Switched to new window successful", Status.PASSED);
    }

    public void closeAllBrowsers() {
        TestBase.driver.quit();
    }

    public void closeBrowser() {
        TestBase.driver.close();
    }

    public void waitForElementVisible(String locator) {
        LogUtils.info("waitForElementVisible : Start => " + locator);
        try {
            wait = new WebDriverWait(TestBase.driver, Duration.ofSeconds(30), Duration.ofSeconds(50));
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locator)));
            LogUtils.info("waitForElementVisible : End => " + locator + ", Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.info("Error while waiting for visibility of element:" + e.getMessage());
            getScreenShot();
            throw new AssertionError("Error while waiting for visibility of element:" + e.getMessage());
        }
    }

    public void waitForElementPresent(String locator) {
        LogUtils.info("waitForElementPresent : Start => " + locator);
        try {
            wait = new WebDriverWait(TestBase.driver, Duration.ofSeconds(30), Duration.ofSeconds(50));
            WebElement result = wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locator)));
            LogUtils.info("waitForElementPresent : End => " + locator + ", Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.info("Error while waiting for presence of element:" + e.getMessage());
            getScreenShot();
            Assert.assertEquals("", "Error while waiting for element with locator: " + locator);
            throw e;
        }

    }

    public boolean isElementDisplayed(String locator) {
        try {
            try {
                new WebDriverWait(TestBase.driver, Duration.ofSeconds(2), Duration.ofSeconds(40)).until(ExpectedConditions.presenceOfElementLocated(getLocator(locator)));
            } catch (Exception e) {
            }
            WebElement element = getElement(locator);
            LogUtils.info("Checking element:" + element);
            if (element == null) {
                return false;
            }
            LogUtils.info("Checking element:" + element.isDisplayed());
            return element.isDisplayed();
        } catch (Exception e) {

            e.printStackTrace();
            LogUtils.info("Error while checking visibility of element:" + e.getMessage());
            //Allure.step("Could not locate element with locator: " + locator + "\n" + e.getMessage(), Status.FAILED);

            getScreenShot();

//            Assert.assertEquals("",
//                    e.toString() + "\nCould not locate element with locator: " + locator + "\n" + e.getMessage());
            Assert.assertEquals("", "Error while trying to locate element with locator: " + locator);
        }
        return false;
    }

    public void setText(String locator, String inputText) {
        WebElement element = null;
        try {

            waitForElementPresent(locator);
            element = getElement(locator);

            waitForElementVisible(locator);

            // waitUntilAngular5Ready();
            waitForAngularLoad();
            element.sendKeys(inputText);
            //Allure.step("Successfully entered text" + " '" + inputText + "'" + " in : " + element, Status.PASSED);

        } catch (Exception e) {

            e.printStackTrace();
            LogUtils.info("Error while setting value in the txtbox:" + e.getMessage());
            //Allure.step("Could not enter " + " '" + inputText + "'" + " in element with locator: " + locator + "\n"
//            +e.getMessage(), Status.FAILED);

            getScreenShot();
//            Assert.assertEquals("", e.toString() + "\nCould not enter " + " '" + inputText + "'"
//                    + " in element with locator: " + locator + "\n" + e.getMessage());
            Assert.assertEquals("", "Error while entering text in element:" + locator);
            throw e;
        }
    }

    public void clearText(String locator) {

        WebElement element = null;
        try {
            waitForElementPresent(locator);
            element = getElement(locator);
            element.clear();

        } catch (Exception e) {
            getScreenShot();
            e.printStackTrace();
            Assert.assertEquals("", "Error while clearing text from element with locator: " + locator + "\n");
            throw e;
        }
    }

    private WebDriver getWebDriver() {
        return TestBase.driver;
    }

    private void angularWait() {
        if (wait == null) {
            wait = new WebDriverWait(TestBase.driver, Duration.ofSeconds(15));

        }
        // wait.until(AdditionalWait.angularHasFinishedProcessing());
    }

    public WebElement waitElementToBeClickable(String locator) {
//        WebElement element = getElement(locator);
        WebDriverWait wait = new WebDriverWait(TestBase.driver, Duration.ofSeconds(20));
        return wait.until(ExpectedConditions.elementToBeClickable(getLocator(locator)));

    }

    // ------------------------

    private void ajaxComplete() {
        LogUtils.debug("waiting for ajaxComplete");
        jsExec.executeScript("var callback = arguments[arguments.length - 1];" + "var xhr = new XMLHttpRequest();"
                + "xhr.open('GET', '/Ajax_call', true);" + "xhr.onreadystatechange = function() {"
                + "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" + "xhr.send();");
        LogUtils.debug("waited for ajaxComplete");
    }

    private void waitForJQueryLoad() {
        LogUtils.debug("waiting for waitForJQueryLoad");
        try {
            ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active") == 0);

            boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");

            if (!jqueryReady) {
                wait.until(jQueryLoad);
            }
        } catch (WebDriverException ignored) {
            LogUtils.info(ignored.getMessage());
        }
        LogUtils.debug("waited for waitForJQueryLoad");
    }

    private void waitForAngularLoad() {
        LogUtils.debug("waiting for waitForAngularLoad");
        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
        angularLoads(angularReadyScript);
        LogUtils.debug("waited for waitForAngularLoad");
    }

    private void waitUntilJSReady() {
        try {
            LogUtils.debug("waiting for waitUntilJSReady");
            ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").toString().equals("complete");

            boolean jsReady = jsExec.executeScript("return document.readyState").toString().equals("complete");

            if (!jsReady) {
                wait.until(jsLoad);
            }
        } catch (WebDriverException ignored) {
            LogUtils.info(ignored.getMessage());
        }
        LogUtils.debug("waited for waitUntilJSReady");
    }

    private void waitUntilJQueryReady() {
        LogUtils.debug("waiting for waitUntilJQueryReady");
        Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
        if (jQueryDefined) {
            poll(20);

            waitForJQueryLoad();

            poll(20);
        }
        LogUtils.debug("waited for waitUntilJQueryReady");
    }

    public void waitUntilAngularReady() {
        LogUtils.debug("waiting for waitUntilAngularReady");
        try {
            Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
            LogUtils.debug("waiting for waitUntilAngularReady:" + angularUnDefined);
            if (!angularUnDefined) {
                Boolean angularInjectorUnDefined = (Boolean) jsExec
                        .executeScript("return angular.element(document).injector() === undefined");
                LogUtils.debug("waiting for waitUntilAngularReady:" + angularInjectorUnDefined);
                if (!angularInjectorUnDefined) {
                    poll(20);

                    waitForAngularLoad();

                    poll(20);
                }
            }
        } catch (WebDriverException ignored) {
            LogUtils.info(ignored.getMessage());
        }
        LogUtils.debug("waited for waitUntilAngularReady");
    }

    private void waitUntilAngular5Ready() {
        LogUtils.debug("waiting for waitUntilAngular5Ready");
        try {
            Object angular5Check = jsExec
                    .executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
            if (angular5Check != null) {
                Boolean angularPageLoaded = (Boolean) jsExec
                        .executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1");
                if (!angularPageLoaded) {
                    poll(20);

                    waitForAngular5Load();

                    poll(20);
                }
            }
        } catch (WebDriverException ignored) {
            LogUtils.debug(ignored.getMessage());
        }
        LogUtils.debug("waited for waitUntilAngular5Ready");
    }

    private void waitForAngular5Load() {
        LogUtils.debug("waiting for waitForAngular5Load");
        String angularReadyScript = "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1";
        angularLoads(angularReadyScript);
        LogUtils.debug("waited for waitForAngular5Load");
    }

    private void angularLoads(String angularReadyScript) {
        LogUtils.debug("waiting for angularLoads");
        try {
            ExpectedCondition<Boolean> angularLoad = driver -> Boolean
                    .valueOf(((JavascriptExecutor) driver).executeScript(angularReadyScript).toString());

            boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());
            LogUtils.debug("waiting for angularReady:" + angularReady);
            if (!angularReady) {

                while (angularReady != true) {

                    angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());

                    staticSleep(2000);
                    // wait.until(angularLoad);
                }

            }
        } catch (WebDriverException ignored) {
            LogUtils.debug(ignored.getMessage());
        }
        LogUtils.debug("waited for angularLoads");
    }

    public void waitAllRequest() {
        LogUtils.debug("waiting for conditions");
        waitUntilJSReady();
        ajaxComplete();
        waitUntilJQueryReady();
        waitUntilAngularReady();
        // waitUntilAngular5Ready();
        LogUtils.debug("waiting done");
    }

    /**
     * Method to make sure a specific element has loaded on the page
     *
     * @param
     * @param expected
     */
    public void waitForElementAreComplete(String locator, int expected) {
        LogUtils.info("waitin for " + locator);
        ExpectedCondition<Boolean> angularLoad = driver -> {
            int loadingElements = driver.findElements(getLocator(locator)).size();
            LogUtils.info("waitin for " + loadingElements);
            System.out.println(loadingElements >= expected);
            return loadingElements >= expected;
        };
        LogUtils.info("waitin for " + angularLoad);
        wait.until(angularLoad);
    }

    /**
     * Waits for the elements animation to be completed
     *
     * @param css
     */
    private void waitForAnimationToComplete(String css) {
        ExpectedCondition<Boolean> angularLoad = driver -> {
            int loadingElements = driver.findElements(By.cssSelector(css)).size();
            return loadingElements == 0;
        };
        wait.until(angularLoad);
    }

    private void poll(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ------------------------

    private WebElement getElement(String locator) {
        String[] element = locator.split("__");
        try {
            switch (element[0].toUpperCase()) {

                case "XPATH":
                    return TestBase.driver.findElement(By.xpath(element[1]));
                case "ID":
                    return TestBase.driver.findElement(By.id(element[1]));
                case "CLASS":
                    return TestBase.driver.findElement(By.className(element[1]));
                case "TAG":
                    return TestBase.driver.findElement(By.tagName(element[1]));
                case "PARTIALLINKTEXT":
                    return TestBase.driver.findElement(By.partialLinkText(element[1]));
                case "LINKTEXT":
                    return TestBase.driver.findElement(By.linkText(element[1]));
                case "CSS":
                    return TestBase.driver.findElement(By.cssSelector(element[1]));
                default:
                    System.err.print("Invalid choice");
                    break;
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private List<WebElement> getElements(String locator) {
        String[] element = locator.split("__");
        try {
            switch (element[0].toUpperCase()) {

                case "XPATH":
                    return TestBase.driver.findElements(By.xpath(element[1]));
                case "ID":
                    return TestBase.driver.findElements(By.id(element[1]));
                case "CLASS":
                    return TestBase.driver.findElements(By.className(element[1]));
                case "TAG":
                    return TestBase.driver.findElements(By.tagName(element[1]));
                case "PARTIALLINKTEXT":
                    return TestBase.driver.findElements(By.partialLinkText(element[1]));
                case "LINKTEXT":
                    return TestBase.driver.findElements(By.linkText(element[1]));
                case "CSS":
                    return TestBase.driver.findElements(By.cssSelector(element[1]));
                default:
                    System.err.print("Invalid choice");
                    break;
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }


    private By getLocator(String locator) {

        try {
            String[] element = locator.split("__");
            switch (element[0].toUpperCase()) {

                case "XPATH":
                    return By.xpath(element[1]);
                case "ID":
                    return By.id(element[1]);
                case "CLASS":
                    return By.className(element[1]);
                case "TAG":
                    return By.tagName(element[1]);
                case "PARTIALLINKTEXT":
                    return By.partialLinkText(element[1]);
                case "LINKTEXT":
                    return By.linkText(element[1]);
                case "CSS":
                    return By.cssSelector(element[1]);
                case "NAME":
                    return By.name(element[1]);
                default:
                    System.err
                            .print("Invalid choice of element locator,kindly check the locator and locator format in OR\n");
                    break;
            }

        } catch (Exception e) {
            LogUtils.error("Could not extract the locator", e);
            throw e;
        }
        return null;
    }

    private void getScreenShot() {

        try {
            if (!isScreenShotCaptured) {


                // Call getScreenshotAs method to create image file
                TakesScreenshot scrShot = ((TakesScreenshot) TestBase.driver);
                File f = scrShot.getScreenshotAs(OutputType.FILE);

                Allure.addAttachment("Screenshot", FileUtils.openInputStream(f));
                isScreenShotCaptured = true;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }



}
