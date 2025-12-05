package iscteiul.ista.lab5es;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DynamicLoadingPage {

    @FindBy(css = "#start button")
    public WebElement startButton;

    @FindBy(css = "#finish h4")
    public WebElement finishText;

    public DynamicLoadingPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}