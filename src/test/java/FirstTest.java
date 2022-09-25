import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FirstTest {

    @Test
    public void verifyThatUserUsedFreezing() {
        open("https://www.duolingo.com/");
        $x("//button[@data-test='have-account']").click();
        $x("//input[@data-test='email-input']").sendKeys("");
        $x("//input[@data-test='password-input']").sendKeys("");
        $x("//button[@data-test='register-button']").click();
        var actuaLinglotQuantity = parseInt($x("//span[@data-test='lingot-stat']").getText());

        assertThat(actuaLinglotQuantity)
                .as("Linglots size should be greater than 0")
                .isGreaterThan(0);

        var lingotMenu = $x("//div[@data-test='lingot-menu']");
        actions().moveToElement(lingotMenu).build().perform();
        var profileElement = $x("//*[contains(text(), 'Go to shop')]");
        profileElement.click();
        $x("//div[contains(text(), '2  / 2 equipped')]").should(Condition.visible);
        $x("//button[@data-test='purchase-button']").shouldHave(Condition.disabled);
    }
}
