package test.suite5.interaction.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

public class VaadinButtonInteraction {

    // Exemplo: o botão “Click me”
    public SelenideElement clickMeButton = $(byText("Click me"));

    // Botões listados na demo (podem mudar, mas estes existem geralmente)
    public ElementsCollection allButtons = $$(byXpath("//div[contains(@class,'v-button')]"));

    // Área onde aparece o texto do evento
    public SelenideElement eventLog = $("#demo-async");

    // Abre diretamente a página correta
    public VaadinButtonInteraction open() {
        Selenide.open("https://demo.vaadin.com/sampler/#ui/interaction/button");
        return this;
    }

    // Clica num botão pelo texto visível
    public VaadinButtonInteraction clickButton(String label) {
        $(byText(label)).click();
        return this;
    }

    // Lê o texto da área de eventos
    public String getEventText() {
        return eventLog.getText();
    }
}
