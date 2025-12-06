package form.tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import form.tests.DatabasePage;

public class DatabaseTest {

    private DatabasePage databasePage;

    @BeforeAll
    public static void globalSetup() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 10000;
    }

    @BeforeEach
    public void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        databasePage = new DatabasePage();
        databasePage.open();
    }

    @Test
    @DisplayName("Validar dados do filme Law Abiding Citizen")
    public void validateMovieInformation() {
        // Baseado na TUA imagem:
        String movieTitle = "Law Abiding Citizen";
        String movieYear = "2009";

        // Valida apenas a visualização
        databasePage.validateMovieOnScreen(movieTitle, movieYear);

        // (Opcional) Se quiseres ver o link a ser clicado, descomenta:
         databasePage.clickImdbLink();
    }
}