package TestSuite7.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

public class VaadinLabelInteraction {

    // Exemplo: o botão “Label”
    public SelenideElement labelButton = $(byText("Label"));

    // Todos os botões da página (para referência)
    public ElementsCollection allButtons = $$("vaadin-button");

    // Área de feedback ou texto exibido (se houver)
    public SelenideElement labelText = $("vaadin-label");

    // Abre diretamente a página correta do Sampler para Labels
    public VaadinLabelInteraction open() {
        Selenide.open("https://demo.vaadin.com/sampler/#ui/data-input/label");
        return this;
    }

    // Clica em um botão pelo texto visível
    public VaadinLabelInteraction clickButton(String label) {
        $(byText(label)).click();
        return this;
    }

    // Lê o texto do Label
    public String getLabelText() {
        return labelText.getText();
    }
}
