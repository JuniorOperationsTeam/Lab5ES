package iscteiul.ista.lab5es;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DynamicLoadingTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private DynamicLoadingPage dynamicPage;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
        dynamicPage = new DynamicLoadingPage(driver);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void testDynamicLoading() {

        // Clicar no botão Start
        dynamicPage.startButton.click();

        // Esperar até o texto aparecer
        wait.until(ExpectedConditions.visibilityOf(dynamicPage.finishText));

        // Validar o conteúdo carregado dinamicamente
        assertEquals("Hello World!", dynamicPage.finishText.getText());
    }
}