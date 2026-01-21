package Pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.BasePage;

public class MatchingBuyerPage extends BasePage{
		private final WebDriverWait wait;

	    // ================== Locators ==================
	    @FindBy(xpath = "(//a[normalize-space(text())='Matching Buyers'])[1]")
	    private WebElement matchingtab;

	    @FindBy(xpath = "(//a[normalize-space(text())='View Phone Number'])[1]")
	    private WebElement ViewPhoneNo;

	    @FindBy(xpath = "(//div[@class='owneng__listitem__localinfovalue'])[1]")
	    private WebElement budget;

	    @FindBy(xpath = "(//div[@class='owneng__listitem__localinfovalue'])[2]")
	    private WebElement Searching;

	    @FindBy(xpath = "(//div[@class='owneng__listitem__localinfovalue'])[3]")
	    private WebElement active;

	    @FindBy(xpath = "(//a[normalize-space(text())='Buy Now'])[6]")
	    private WebElement buymatching;

	    
	    // ================== Constructor ==================
	    OwnerDashboardPage ownerPage;
	    public MatchingBuyerPage(WebDriver driver) {
	        PageFactory.initElements(driver, this);
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        ownerPage = new OwnerDashboardPage(driver);
	    }

	    // ================== Actions ==================

	    /** Switch to Matching Buyer tab */
	    public void navigateMatching() {
	        wait.until(ExpectedConditions.elementToBeClickable(matchingtab)).click();
	        System.out.println("Matching tab is clicked Successfully");
	        SwitchOnTab();
	        ScrollToElement(ViewPhoneNo);
	        System.out.println("Budget: " + budget.getText());
	        System.out.println("Searching Since: " + Searching.getText());
	        System.out.println("Last Active: " + active.getText());
	        wait.until(ExpectedConditions.elementToBeClickable(ViewPhoneNo)).click();
	        System.out.println("View Phone Number CTA is clickable from Matching Page");
	        wait.until(ExpectedConditions.elementToBeClickable(buymatching)).click();
	        System.out.println("Matching Grid is displayed and Buy Now CTA is clickable");
	        
	    }

	    
}
