package Tests;

import Utils.User;
import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;
import runners.SelenoidRunner;

import static Utils.UserRepo.getIrunaShemraiUser;
import static Utils.UserRepo.getVasylMarusyakUser;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FirstTest extends SelenoidRunner {
    @Test(priority = -19)
    public void verifyVasylMarusyakUsedFreezing() {
        verifyUserUsedFreezingBaseTest(getVasylMarusyakUser());
    }

    @Test(priority = -100)
    public void verifyIrunaShemraiUsedFreezing() {
        verifyUserUsedFreezingBaseTest(getIrunaShemraiUser());
    }

    private void verifyUserUsedFreezingBaseTest(User user) {
        open("https://www.duolingo.com/");
        $x("//button[@data-test='have-account']").click();
        $x("//input[@data-test='email-input']").sendKeys(user.getEmail());
        $x("//input[@data-test='password-input']").sendKeys(user.getPassword());
        $x("//button[@data-test='register-button']").click();
        var actuaLinglotQuantity = parseInt($x("//span[@data-test='lingot-stat']").getText());

        assertThat(actuaLinglotQuantity)
                .as("Linglots size should be greater than 0")
                .isGreaterThan(0);

        refresh();// need to avoid freezing because disabled notifications
        var lingotMenu = $x("//div[@data-test='lingot-menu']");
        actions().moveToElement(lingotMenu).build().perform();
        var profileElement = $x("//*[contains(text(), 'Go to shop')]");

        if(profileElement.isDisplayed()){
            actions().moveToElement(profileElement).build().perform();
            profileElement.click();
        } else {
            lingotMenu.click();
        }

        $x("//div[contains(text(), '2  / 2 equipped')]").should(visible);
        $x("//button[@data-test='purchase-button']").shouldHave(Condition.disabled);
    }
}
