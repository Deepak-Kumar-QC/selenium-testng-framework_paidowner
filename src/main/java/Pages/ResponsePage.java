package Pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.BasePage;

public class ResponsePage extends BasePage{
		private final WebDriverWait wait;

	    // ================== Locators ==================
	    @FindBy(xpath = "//a[normalize-space(text())='Responses']")
	    private WebElement responsetab;

	    @FindBy(xpath = "//div[@class='myreq__toptags__option myreq__toptags__option--active']")
	    private WebElement approvetab;

	    @FindBy(xpath = "(//span[@class='callsection__whatsapp'])[1]")
	    private WebElement whatsapp;

	    @FindBy(xpath = "(//a[normalize-space(text())='Buy Now'])[2]")
	    private WebElement buyNow;

	    @FindBy(xpath = "(//div[@class='owneng__listitem__localinfovalue'])[3]")
	    private WebElement active;

	    @FindBy(xpath = "(//a[normalize-space(text())='Buy Now'])[6]")
	    private WebElement buymatching;

	    
	    // ================== Constructor ==================
	    OwnerDashboardPage ownerPage;
	    public ResponsePage(WebDriver driver) {
	        PageFactory.initElements(driver, this);
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        ownerPage = new OwnerDashboardPage(driver);
	    }

	    // ================== Actions ==================

	    /** Switch to Matching Buyer tab */
	    public void navigateResponse() {
	        wait.until(ExpectedConditions.elementToBeClickable(responsetab)).click();
	        System.out.println("Response tab is clicked Successfully");
	        SwitchOnTab();
	        wait.until(ExpectedConditions.elementToBeClickable(approvetab)).click();
	        System.out.println("Approved tab is clickable Successfully");
	        wait.until(ExpectedConditions.elementToBeClickable(whatsapp)).click();
	        System.out.println("Whatsapp CTA is clickable");
	        wait.until(ExpectedConditions.elementToBeClickable(buyNow)).click();
	        System.out.println("BuyNow CTA is clickable from Response tab");
	    }

	    
}
