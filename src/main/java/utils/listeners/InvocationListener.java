package utils.listeners;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.OutputType;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Selenide.screenshot;
import static io.qameta.allure.Allure.addAttachment;
import static org.openqa.selenium.OutputType.BYTES;

public class InvocationListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        System.out.println("beforeInvocation: runs before every method in the Test Class");
    }


    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        addAttachment("nameTest", new ByteArrayInputStream(screenshot(BYTES)));
    }
}