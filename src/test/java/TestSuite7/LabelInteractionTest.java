package TestSuite7;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import TestSuite7.pages.VaadinLabelInteraction;

public class LabelInteractionTest {

    private VaadinLabelInteraction labelInteraction;

    @BeforeEach
    public void setup() {
        Configuration.browser = "chrome";
        Configuration.timeout = 8000;

        // Inicializa a classe de interação e abre a página do Sampler
        labelInteraction = new VaadinLabelInteraction().open();
    }

    @Test
    public void clickLabelButton() {
        // Apenas clica no botão "Label"
        labelInteraction.clickButton("Label");
    }
}
