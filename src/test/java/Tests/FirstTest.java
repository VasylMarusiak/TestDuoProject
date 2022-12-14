package Tests;

import Utils.User;
import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import runners.SelenoidRunner;
import utils.listeners.TestResultsListener;
import utils.testrail.TestRailCase;

import static Utils.UserRepo.getIrunaShemraiUser;
import static Utils.UserRepo.getVasylMarusyakUser;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Listeners(TestResultsListener.class) //works only for separate tests
public class FirstTest extends SelenoidRunner {
    @Test
    @TestRailCase(id = 2298)
    public void verifyVasylMarusyakUsedFreezing() {
        verifyUserUsedFreezingBaseTest(getVasylMarusyakUser());
    }

    @Test
    @TestRailCase(id = 2299)
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
        $x("//button[@data-test='purchase-button']/span[contains(text(), 'Equipped')]").should(visible);
        $x("//div[contains(text(), '2  / 2 equipped') or contains(text(), '3/3 equipped')]").should(visible);
        $x("//button[@data-test='purchase-button']").shouldHave(Condition.disabled);
    }
}
