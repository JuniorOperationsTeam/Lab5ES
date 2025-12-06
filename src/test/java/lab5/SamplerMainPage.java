package lab5;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SamplerMainPage {

    private WebDriver driver;

    // "User interface" menu item
    @FindBy(xpath = "//span[text()='User interface']/ancestor::div[contains(@class,'v-tree-node')]")
    public WebElement userInterfaceMenu;

    // "Data input" submenu
    @FindBy(xpath = "//span[text()='Data input']/ancestor::div[contains(@class,'v-tree-node')]")
    public WebElement dataInputMenu;

    // Link para o exemplo "TextField"
    @FindBy(xpath = "//a[.//span[text()='TextField']]")
    public WebElement textFieldExample;

    // Campo TextField dentro da demonstração
    @FindBy(css = ".v-textfield")
    public WebElement textFieldInput;


    public SamplerMainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
