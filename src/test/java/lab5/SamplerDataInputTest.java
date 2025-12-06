package lab5; // ou o teu package

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class SamplerDataInputTest {

    @BeforeEach
    public void setup() {
        Configuration.browser = "chrome";
        Configuration.timeout = 8000;

        open("https://demo.vaadin.com/sampler/#ui/data-input/text-input/text-field");
    }

    @Test
    public void accessTextField() {
        // Aceder diretamente ao campo "Write something"
        SelenideElement textField = $("input[placeholder='Write something']");

        // Só verificar que o elemento existe (não escrevemos nada)
        textField.should(exist);
    }
}
