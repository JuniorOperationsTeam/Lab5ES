package form.tests; // Confirma o teu package!

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DatabasePage {

    private static final String URL = "https://vaadin-database-example.demo.vaadin.com/";
    private final SelenideElement movieGrid = $(By.tagName("vaadin-grid"));

    public void open() {
        Selenide.open(URL);
        $("body").shouldBe(visible);
        movieGrid.shouldBe(visible);
    }

    /**
     * Valida se um filme e o respetivo ano estão visíveis na grelha.
     */
    public void validateMovieOnScreen(String title, String year) {
        // Espera que a grelha carregue
        movieGrid.shouldBe(visible);
        Selenide.sleep(1000);

        // Validação Simples: Verifica se o texto do Título e do Ano estão visíveis na página.
        // Como é uma página de leitura, isto é suficiente para garantir que os dados lá estão.
        $(byText(title)).shouldBe(visible);
        $(byText(year)).shouldBe(visible);
    }

    /**
     * (Opcional) Clica no link do IMDB se quiseres testar navegação
     */
    public void clickImdbLink() {
        // Procura o link específico "Click to IMBD site"
        // Como há vários com o mesmo texto, clicamos no primeiro visível
        $$(byText("Click to IMBD site"))
                .first()
                .shouldBe(visible)
                .click();
    }
}