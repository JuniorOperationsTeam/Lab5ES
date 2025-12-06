package test.suite5.interaction.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class VaadinButtonInteractionTest {

    @BeforeEach
    public void setup() {
        Configuration.browser = "chrome";
        Configuration.timeout = 8000;
        open("https://demo.vaadin.com/sampler/#ui/interaction/button");
    }

    @Test
    public void clickButtonClick() {
        // Localiza o botão Vaadin pelo texto interno "Click"
        SelenideElement button = $x("//div[contains(@class,'v-button')][.//span[text()='Click']]");

        button.shouldBe(visible).click();
    }
}
