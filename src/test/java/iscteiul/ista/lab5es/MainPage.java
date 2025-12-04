package iscteiul.ista.lab5es;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// page_url = https://www.jetbrains.com/
public class MainPage {

    @FindBy(xpath = "//*[@data-test-marker='Developer Tools']")
    public WebElement seeDeveloperToolsButton;

    @FindBy(xpath = "//*[@data-test='suggestion-link']")
    public WebElement findYourToolsButton;

    // Example definition for Team Tools (you must verify this locator)
    @FindBy(xpath = "//*[@data-test-marker='Team Tools']")
    public WebElement teamToolsButton;

   // @FindBy(css = "div[data-test='main-submenu'][data-test-parent='tools']")
   // public WebElement toolsSubmenu;

    @FindBy(css = "[data-test='site-header-search-action']")
    public WebElement searchButton;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}

