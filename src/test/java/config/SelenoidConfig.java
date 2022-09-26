package config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.selenide.AllureSelenide;


public class SelenoidConfig {
    public void createWebDriverInstance() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
      //  Configuration.browserVersion = "87";
     //   Configuration.browser = browser;
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.timeout = 30000;
        Configuration.reportsFolder = "target/screenshots";
        Configuration.clickViaJs = true;
       // Configuration.startMaximized = true;
        Configuration.baseUrl = "";
        Configuration.browserCapabilities.setCapability("enableVNC", false);
        Configuration.browserCapabilities.setCapability("enableVideo", false);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
//        logger.info("ok");
    }
}
