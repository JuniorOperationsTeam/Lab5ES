package iscteiul.ista.lab5es;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.jetbrains.com/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // FECHAR COOKIES ANTES DE QUALQUER COISA
        closeCookiesIfPresent();

        mainPage = new MainPage(driver);
    }

    private void closeCookiesIfPresent() {
        try {
            // espera até 5s por um banner de cookies
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // o overlay principal
            WebElement container = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.ch2-container")
                    )
            );

            // botão principal (aceitar/rejeitar – tanto faz para o teste)
            WebElement button = container.findElement(
                    By.cssSelector("button.ch2-btn-primary")
            );
            button.click();

            // garantir que desapareceu
            shortWait.until(ExpectedConditions.invisibilityOf(container));

        } catch (TimeoutException | NoSuchElementException e) {
            // se não aparecer banner ou a estrutura for ligeiramente diferente, segue em frente
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void search() {
        mainPage.searchButton.click();

        WebElement searchField = driver.findElement(By.cssSelector("input[data-test$='inner']"));
        searchField.sendKeys("Selenium");

        WebElement submitButton = driver.findElement(By.cssSelector("input[data-test$='inner']"));
        submitButton.click();

        WebElement searchPageField = driver.findElement(By.cssSelector("input[data-test$='inner']"));
        assertEquals("Selenium", searchPageField.getAttribute("value"));
    }


    @Test
    public void toolsMenu() {
        mainPage.toolsMenu.click();

        WebElement menuPopup = driver.findElement(By.cssSelector("div[class*='71'] button[data-test='main-menu-item-action']"));
        assertTrue(menuPopup.isDisplayed());
    }

    @Test
    public void navigationToAllTools() {
        mainPage.seeDeveloperToolsButton.click();
        mainPage.findYourToolsButton.click();

        WebElement productsList = driver.findElement(By.id("products-page"));
        assertTrue(productsList.isDisplayed());
        assertEquals("All Developer Tools and Products by JetBrains", driver.getTitle());
    }


    @Test
    public void openStore() {

        // 1) localizar o botão Store
        WebElement storeButton = driver.findElement(
                By.cssSelector("a[data-test='site-header-cart-action']")
        );

        // 2) clicar
        storeButton.click();

        // 3) validar que abriu a página da Store
        assertTrue(driver.getCurrentUrl().contains("store"));

    }

    @Test
    public void openLogin() {

        // 1) clicar no botão de login
        WebElement loginButton = driver.findElement(
                By.cssSelector("a[data-test='site-header-profile-action'] svg[class*='22']")
        );
        loginButton.click();


        assertTrue(driver.getCurrentUrl().contains("login"));
    }

}
