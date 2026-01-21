package Pages;



import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.BasePage;

public class OrderPage extends BasePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // ================== Locators ==================
    @FindBy(xpath = "//div[@class='underprocess-card__close']/a")
    private WebElement closeBtn;

    @FindBy(xpath = "//a[contains(@onclick, 'underprocess-card')]")
    private WebElement failclose;

    @FindBy(xpath = "//a[@id='underprocess-card_pitch1_btn']")
    private WebElement completePaymentBtn;

    @FindBy(xpath = "//a[@class='mb-header__main__link js-menu-link normal-user']")
    private WebElement hovermanage;

    @FindBy(xpath = "//a[text()='Manage Properties']")
    private WebElement manage;

    @FindBy(xpath = "//a[@onclick=\"premiumPopupClose('premiumPopup');\"]")
    private WebElement closeWelcome;

    @FindBy(xpath = "//a[@class='popup_renew_info_detail_action-btn']")
    private WebElement renewNowBtn;

    @FindBy(xpath = "(//a[@class='oppPopup_close md-close'])[2]")
    private WebElement closeQuickPost;

    @FindBy(xpath = "//div[@class='fo_12px font-type-2 c_white']")
    private WebElement movetoheader;

    @FindBy(xpath = "//a[@class='md-close' and contains(@href,'showRenewalTab')]")
    private WebElement closeRenewalPopup;
    @FindBy(xpath = "//a[text()='Manage Properties']") 
    private WebElement manageproperties;
    // ================== Constructor ==================
    OwnerDashboardPage ownerPage;
    public OrderPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        ownerPage = new OwnerDashboardPage(driver);
    }

    // ================== Actions ==================

    /** Switch to Manage Property tab */
    public void switchToManage() {
        wait.until(ExpectedConditions.elementToBeClickable(manage)).click();
        SwitchOnTab();
    }

    /** Handle Failure or Renewal Cards */
    public void handleFailureCard() {
        try {
            if (isElementVisible(failclose)) {
                wait.until(ExpectedConditions.elementToBeClickable(completePaymentBtn)).click();
                System.out.println("Clicked on 'Complete Your Payment' button from Failure Popup.");
            } else if (isElementVisible(renewNowBtn)) {
                wait.until(ExpectedConditions.elementToBeClickable(renewNowBtn)).click();
                System.out.println("Clicked on 'Renew Now' button from Renewal Grid.");
            } else {
                System.out.println("No failure-related card is displayed.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Exception in handleFailureCard(): " + e.getMessage());
        }
    }

    public void closeFailure(){
    try {
    	wait.until(ExpectedConditions.elementToBeClickable(failclose)).click();
   	 System.out.println("Failure card closed successfully.");
    }
    catch(TimeoutException e) {
    	 System.out.println("ℹ️ Failure card not visible within wait time.");
    	ownerPage.closeWelcomePopupIfVisible();
    	
    }
    }
    /** Close popups in loop until renewal popup is handled */
    public void closeFailure1(){
    	if(failclose.isDisplayed()) {
    	 wait.until(ExpectedConditions.elementToBeClickable(failclose)).click();
    	 System.out.println("Failure card closed successfully.");
    }
    	if(closeQuickPost.isDisplayed()) {
    	wait.until(ExpectedConditions.elementToBeClickable(closeQuickPost)).click();
       	 System.out.println("QuickPost closed successfully.");
       	 Actions a=new Actions(driver);
       	 a.moveToElement(movetoheader).click().perform();
             wait.until(ExpectedConditions.elementToBeClickable(manageproperties)).click();
             System.out.println("Moved to Owner Dashboard.");
         } 
    	else {
    		wait.until(ExpectedConditions.elementToBeClickable(closeRenewalPopup)).click();
            System.out.println("Closed Renewal popup successfully.");
    		      
    		    }

    }

    /** Hover to Owner menu and click Manage Properties */
    public void switchToOwnerboard() {
        wait.until(ExpectedConditions.visibilityOf(hovermanage));
        Actions actions = new Actions(driver);
        actions.moveToElement(hovermanage).pause(Duration.ofMillis(500)).perform();
        wait.until(ExpectedConditions.elementToBeClickable(manage)).click();
        System.out.println("🧭 Navigated to Manage Properties via Ownerboard menu.");
    }

    // ================== Utilities ==================

    private boolean isElementVisible(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}
