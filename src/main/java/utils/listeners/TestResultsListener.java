package utils.listeners;

import io.qameta.allure.Attachment;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.testrail.TestRailUtil;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Selenide.screenshot;
import static io.qameta.allure.Allure.addAttachment;
import static org.openqa.selenium.OutputType.BYTES;
import static utils.testrail.TestRailUtil.*;

public class TestResultsListener extends TestListenerAdapter {

   // private final TestRailUtil testRailManager = new TestRailUtil();

    @Override
    public void onTestStart(ITestResult result) {
     //   addAttachment("nameTest", new ByteArrayInputStream(screenshot(BYTES)));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        addAttachment("nameTest", new ByteArrayInputStream(screenshot(BYTES)));
        if (milestoneId == 0) {
            milestoneId = addMilestone(PROJECT_ID, milestoneName).getId();
        }
        var id = getOrAddTestRun(runName);
        TestRailUtil.setTestResult(result, id, milestoneId);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Failured of test cases and its details are : " + result.getName());

        if (milestoneId == 0) {
            milestoneId = addMilestone(PROJECT_ID, milestoneName).getId();
        }
        var id = getOrAddTestRun(milestoneName);
        setTestResult(result, id, milestoneId);
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
