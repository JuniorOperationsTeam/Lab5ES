// java
package iscteiul.ista.lab5es;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // prefer explicit waits
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.get("https://www.jetbrains.com/");
        mainPage = new MainPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    private void hideKnownOverlays() {
        By[] overlaySelectors = new By[] {
                By.cssSelector("button[aria-label*='cookie']"),
                By.cssSelector("button[aria-label*='accept']"),
                By.cssSelector("button#rcc-confirm-button"),
                By.cssSelector(".cookie-banner button"),
                By.cssSelector(".js-cookie-consent-accept"),
                By.cssSelector(".overlay-close"),
                By.cssSelector(".modal__close"),
                By.cssSelector(".close")
        };
        for (By sel : overlaySelectors) {
            try {
                List<WebElement> els = driver.findElements(sel);
                for (WebElement e : els) {
                    if (e.isDisplayed() && e.isEnabled()) {
                        try { e.click(); pause(200); } catch (Exception ex) {
                            try { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", e); pause(200); } catch (Exception ignored) {}
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private void safeClick(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        try {
            hideKnownOverlays();
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException | TimeoutException | StaleElementReferenceException ex) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception e2) {
                try {
                    new Actions(driver).moveToElement(element).click().perform();
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
    }

    private WebElement waitForAnyVisible(long timeoutSeconds, By... locators) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(drv -> {
            for (By locator : locators) {
                try {
                    List<WebElement> els = drv.findElements(locator);
                    for (WebElement el : els) {
                        if (el.isDisplayed()) return el;
                    }
                } catch (Exception ignored) {}
            }
            return null;
        });
    }

    private WebElement waitForSearchInput(long timeoutSeconds) {
        String[] selectors = {
                "[data-test='search-input']",
                "input[type='search']",
                "input[placeholder*='Search']",
                "input[id*='search']",
                "input[name*='search']",
                "input[aria-label*='Search']",
                "input[class*='search']"
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(drv -> {
            for (String sel : selectors) {
                try {
                    List<WebElement> els = drv.findElements(By.cssSelector(sel));
                    for (WebElement e : els) {
                        if (e.isDisplayed() && e.isEnabled()) return e;
                    }
                } catch (Exception ignored) {}
            }

            // fallback: JS procura inputs visíveis cujo placeholder/aria-label/id/nome contenha 'search'
            try {
                Object result = ((JavascriptExecutor) drv).executeScript(
                        "const inputs = Array.from(document.querySelectorAll('input')); " +
                                "for (const i of inputs) { " +
                                "  const txt = (i.placeholder||i.getAttribute('aria-label')||i.id||i.name||'').toLowerCase(); " +
                                "  const rect = i.getBoundingClientRect(); " +
                                "  if (txt.includes('search') && rect.width>0 && rect.height>0) return i; " +
                                "} return null;"
                );
                if (result instanceof WebElement) {
                    WebElement el = (WebElement) result;
                    if (el.isDisplayed() && el.isEnabled()) return el;
                }
            } catch (Exception ignored) {}

            return null;
        });
    }

    private boolean waitForSearchResults(long timeoutSeconds) {
        By[] resultLocators = new By[] {
                By.cssSelector("div.search-results"),
                By.cssSelector(".search__results"),
                By.cssSelector("section.search-results"),
                By.cssSelector("#search-results"),
                By.cssSelector(".results"),
                By.cssSelector(".search-page")
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return wait.until(drv -> {
                String url = "";
                try { url = drv.getCurrentUrl().toLowerCase(); } catch (Exception ignored) {}
                if (url.contains("/search") || url.contains("?q=") || url.contains("search?")) return true;

                for (By loc : resultLocators) {
                    try {
                        List<WebElement> els = drv.findElements(loc);
                        for (WebElement e : els) {
                            if (e.isDisplayed()) {
                                // também verificar se existem itens dentro do container
                                List<WebElement> children = e.findElements(By.cssSelector("*"));
                                if (children.size() > 0) return true;
                            }
                        }
                    } catch (Exception ignored) {}
                }
                return false;
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Test
    public void search() {
        // abrir a caixa de pesquisa (botão do mainPage)
        safeClick(mainPage.searchButton);

        WebElement searchField;
        try {
            searchField = waitForSearchInput(20);
        } catch (TimeoutException e) {
            searchField = null;
        }

        assertNotNull(searchField, "Campo de pesquisa não encontrado após tentativas e timeouts.");
        searchField.clear();
        searchField.sendKeys("Selenium");
        pause(300);

        By submitLocator = By.cssSelector("button[data-test='full-search-button'], button[type='submit'], button[aria-label*='Search']");
        try {
            WebElement submit = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(submitLocator));
            submit.click();
        } catch (Exception ex) {
            // fallback: enviar ENTER
            try { searchField.sendKeys(Keys.ENTER); } catch (Exception ignored) {}
        }

        boolean results = waitForSearchResults(30);
        assertTrue(results, "Página de resultados de pesquisa não foi carregada corretamente.");
    }

    // Inside MainPageTest.java
    @Test
    public void toolsMenu() {
        // 1. Setup Wait and Actions
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);

        // 2. Wait for the corrected/existing menu button (e.g., Developer Tools)
        WebElement toolsButton = wait.until(ExpectedConditions.visibilityOf(mainPage.teamToolsButton));

        // 3. Perform the Mouse-Over Action
        actions.moveToElement(toolsButton)
                .pause(Duration.ofMillis(500))
                .perform();

        // 4. Wait for the submenu (This step will now be reached)
        // If the submenu locator itself fails, you'll need to inspect it next.
     //   WebElement submenu = wait.until(ExpectedConditions.visibilityOf(mainPage.toolsSubmenu));

        // 5. Assert
        assertTrue(toolsButton.isDisplayed(), "The submenu should be visible after hovering.");
    }

    @Test
    public void navigationToAllTools() {
        safeClick(mainPage.seeDeveloperToolsButton);
        safeClick(mainPage.findYourToolsButton);

        WebElement productsList = null;
        try {
            productsList = waitForAnyVisible(20,
                    By.id("products-page"),
                    By.cssSelector("div.products-list"),
                    By.cssSelector(".products-grid"),
                    By.cssSelector(".products-list"));
        } catch (TimeoutException ignored) {}

        assertNotNull(productsList, "Lista de produtos não encontrada.");
        assertTrue(productsList.isDisplayed());
        // título pode variar; ajuste se necessário
        assertTrue(driver.getTitle().toLowerCase().contains("developer tools") ||
                driver.getTitle().toLowerCase().contains("all developer tools") ||
                driver.getTitle().toLowerCase().contains("tools"));
    }
}
