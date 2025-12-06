package iscteiul.ista.lab5es;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
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

        // Fechar cookies antes de qualquer ação
        closeCookiesIfPresent();

        mainPage = new MainPage(driver);
    }

    private void closeCookiesIfPresent() {
        try {
            // Espera até 5s por um banner de cookies.
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Overlay principal do cookie.
            WebElement container = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.ch2-container")
                    )
            );

            // Botão principal (aceitar/rejeitar — indistinto para os testes).
            WebElement button = container.findElement(
                    By.cssSelector("button.ch2-btn-primary")
            );
            button.click();

            // Aguarda até o overlay desaparecer.
            shortWait.until(ExpectedConditions.invisibilityOf(container));

        } catch (TimeoutException | NoSuchElementException e) {
            // Se o banner não aparecer ou a estrutura for diferente, prossegue normalmente.
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
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
        // Localizar o botão Store
        WebElement storeButton = driver.findElement(
                By.cssSelector("a[data-test='site-header-cart-action']")
        );

        // Clicar
        storeButton.click();

        // Validar que abriu a página da Store
        assertTrue(driver.getCurrentUrl().contains("store"));
    }

    @Test
    public void openLogin() {
        // Clicar no botão de login
        WebElement loginButton = driver.findElement(
                By.cssSelector("a[data-test='site-header-profile-action'] svg[class*='22']")
        );
        loginButton.click();

        assertTrue(driver.getCurrentUrl().contains("login"));
    }

    @Test
    public void returnToHome() {
        // 1. Forçar navegação para uma página diferente (ex: /all/)
        driver.get("https://www.jetbrains.com/all/");

        // 2. Encontrar o logótipo da JetBrains no cabeçalho
        WebElement siteLogo = driver.findElement(
                By.cssSelector("a[data-test='site-logo']")
        );

        // 3. Clicar no logótipo
        siteLogo.click();

        // 4. Verificar se voltámos à página inicial
        assertEquals("https://www.jetbrains.com/", driver.getCurrentUrl());
    }

    @Test
    public void solutionsMenu() {
        // 1. Encontrar o botão "Solutions" no menu principal
        WebElement solutionsButton = driver.findElement(
                By.xpath("//button[@data-test='main-menu-item-action' and contains(., 'Solutions')]")
        );

        // 2. Clicar no menu
        solutionsButton.click();

        // 3. Verificar se o popup do menu apareceu.
        WebElement subMenuLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(text(), 'Remote Development')]")
        ));

        assertTrue(subMenuLink.isDisplayed());
    }

    @Test
    public void dynamicContentTest() {
        // Ir para o site de testes dinâmicos (pedido na tarefa)
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

        // localizar botão Start
        WebElement startButton = driver.findElement(By.cssSelector("#start button"));
        startButton.click();

        // esperar que o texto "Hello World!" apareça
        // Nota: re-instanciar o WebDriverWait aqui não é estritamente necessário se usares o 'wait' global,
        // mas não faz mal.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement finishText = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4"))
        );

        // validar conteúdo dinâmico
        assertEquals("Hello World!", finishText.getText());
    }

    @Test
    public void testFileUpload() throws IOException {
        driver.get("https://the-internet.herokuapp.com/upload");

        String fileName = "teste_upload.txt";
        File file = new File(System.getProperty("user.dir") + File.separator + fileName);

        // Criar o ficheiro se não existir para evitar falhas no teste
        if (!file.exists()) {
            file.createNewFile();
        }
        String absolutePath = file.getAbsolutePath();

        WebElement fileInput = driver.findElement(By.id("file-upload"));
        fileInput.sendKeys(absolutePath);

        WebElement uploadButton = driver.findElement(By.id("file-submit"));
        uploadButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h3")));
        assertEquals("File Uploaded!", header.getText(), "Mensagem de sucesso incorreta.");

        WebElement uploadedFiles = driver.findElement(By.id("uploaded-files"));
        assertEquals(fileName, uploadedFiles.getText().trim(), "O nome do ficheiro enviado não corresponde.");
    }
}