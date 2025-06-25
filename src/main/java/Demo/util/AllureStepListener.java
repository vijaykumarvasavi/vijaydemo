package Demo.util;

import Demo.base.TestBase;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class AllureStepListener implements StepLifecycleListener {

    protected static Logger logger = LogManager.getLogger(AllureStepListener.class);

    @Override
    public void beforeStepStop(final StepResult result) {

        if (result.getStatus().equals(Status.FAILED) || result.getStatus().equals(Status.BROKEN)) {
            saveScreenshotPNG(TestBase.driver);
            result.setStatus(Status.FAILED);
        }

    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

}