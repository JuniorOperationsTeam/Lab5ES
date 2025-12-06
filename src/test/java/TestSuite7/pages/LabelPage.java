package TestSuite7.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;

public class LabelPage {

    private SelenideElement labelComponent = $("vaadin-label");

    public boolean isLabelVisible() {
        return labelComponent.shouldBe().exists();
    }
}
