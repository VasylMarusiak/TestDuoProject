package utils.listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.testrail.TestRailManager;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Selenide.screenshot;
import static io.qameta.allure.Allure.addAttachment;
import static org.openqa.selenium.OutputType.BYTES;
//import static utils.listeners.AllureListener.attachScreenshot1;

public class TestResultsListener extends TestListenerAdapter {

    private final TestRailManager testRailManager = new TestRailManager();

    @Override
    public void onTestStart(ITestResult result) {
     //   addAttachment("nameTest", new ByteArrayInputStream(screenshot(BYTES)));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        attachScreenshot();
        addAttachment("nameTest", new ByteArrayInputStream(screenshot(BYTES)));
        testRailManager.setTestResult(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Failured of test cases and its details are : " + result.getName());

       // addAttachment("nameTest", new ByteArrayInputStream(screenshot(BYTES)));
        testRailManager.setTestResult(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
// TODO Auto-generated method stub
        System.out.println("Skip of test cases and its details are : " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Failure of test cases and its details are : " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] attachScreenshot() {
        return screenshot(BYTES);
    }
}
