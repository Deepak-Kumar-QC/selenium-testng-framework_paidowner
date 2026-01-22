package Pages;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.BasePage;

public class OwnerDashboardPage extends BasePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ==================== Locators ====================
    @FindBy(xpath = "//a[@onclick=\"premiumPopupClose('premiumPopup');\"]")
    private WebElement closeWelcomePopup;

    @FindBy(xpath = "//a[contains(@onclick, 'underprocess-card')]")
    private WebElement failureCloseBtn;

    @FindBy(xpath = "//a[@class='popup_renew_info_detail_action-btn']")
    private WebElement renewNowBtn;

    @FindBy(xpath = "//a[contains(normalize-space(.), 'View More Renewal Packages')]")
    private WebElement viewMoreBtn;

    @FindBy(xpath = "(//a[@class='mbB2cGrid__btn'])[3]")
    private WebElement titaniumPackageBtn;

    @FindBy(xpath = "(//div[@class='mbB2cGrid__price__offer--mrp'])[3]")
    private WebElement actualPrice;

    @FindBy(xpath = "(//div[@class='mbB2cGrid__price__total'])[3]")
    private WebElement offerPrice;

    @FindBy(xpath = "(//div[@class='b2c-cart__summary--value'])[4]")
    private WebElement totalPrice;

    @FindBy(xpath = "(//div[@class='b2c-cart__summary--value'])[1]")
    private WebElement cartSubtotal;

    @FindBy(xpath = "//div[@class='b2c-cart__summary--value green']")
    private WebElement cartDiscount;

    @FindBy(xpath = "//a[normalize-space(text())='Make Payment']")
    private WebElement makePaymentBtn;

    @FindBy(xpath = "//div[@class='b2c-cart__card__package--name']")
    private WebElement cartPackageName;

    @FindBy(xpath = "//a[contains(@href,'modal_popup.close')]")
    private WebElement closeRenewalPopup;
   
    @FindBy(xpath = "//select[@id='addressSelect']/option[contains(text(), '77692281')]")
    private WebElement selectPropertyOption;
    
    @FindBy(xpath = "//a[normalize-space(text())='Renew Listing']")
    private WebElement renewCTA;
  
    @FindBy(xpath = "//input[@id='convertArea']")
    private WebElement renewDropdown;
    
    @FindBy(xpath = "//li[@data-listing='77779']")
    private WebElement renewPackage;

  
    @FindBy(xpath = "//a[@id='continueId']")
    private WebElement Refresh;
    
    @FindBy(xpath = "//a[normalize-space()='Go To Dashboard']")
    private WebElement switchowner;
    
  
    // ==================== Constructor ====================
    public OwnerDashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ==================== Actions ====================
    
    public void clickownerswitch() {
        waitAndClick(switchowner, "Clicked 'Switched owner page successfully.");
    }

    public void clickRenewNow() {
        waitAndClick(renewNowBtn, "Clicked 'Renew Now' CTA successfully.");
    }

    public void clickViewMorePackages() {
        waitAndClick(viewMoreBtn, "Opened 'View More Packages' grid.");
    }

    public void selectTitaniumPackage() {
        waitAndClick(titaniumPackageBtn, "Selected Titanium Package.");
    }

    public void printTitaniumPackageDetails() {
        try {
            wait.until(ExpectedConditions.visibilityOf(actualPrice));
            System.out.println("💎 Titanium Package Actual Price: " + actualPrice.getText());
            System.out.println("💰 Titanium Package Offer Price: " + offerPrice.getText());
        } catch (TimeoutException e) {
            System.out.println("❌ Titanium package details not visible: " + e.getMessage());
        }
    }

    public void printCartDetailsAndProceedToPayment() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cartPackageName));
            System.out.println("🛒 Cart Package Name: " + cartPackageName.getText());
            System.out.println("💵 Subtotal: " + cartSubtotal.getText());
            System.out.println("🎁 Discount: " + cartDiscount.getText());
            System.out.println("✅ Total: " + totalPrice.getText());
            waitAndClick(makePaymentBtn, "Clicked 'Make Payment' button.");
        } catch (Exception e) {
            System.out.println("❌ Error printing cart details or clicking payment: " + e.getMessage());
        }
    }

    public void closeRenewalPopup() {
        waitAndClick(closeRenewalPopup, "Closed Renewal popup successfully.");
    }
  
    public void selectPropertyByVisibleText(String propertyText) {
        try {
            WebElement addressDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addressSelect")));

            Select select = new Select(addressDropdown);
            select.selectByVisibleText(propertyText);
            System.out.println("🏡 Selected property: " + propertyText);

            wait.until(ExpectedConditions.urlContains("/ownerdashboard/property-information"));
            System.out.println("🔄 Redirected to property information page.");
        } catch (TimeoutException e) {
            System.out.println("⏳ Property dropdown not loaded: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Exception while selecting property: " + e.getMessage());
        }
    }

 
    public void closeWelcomePopupIfVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(closeWelcomePopup));
            if (closeWelcomePopup.isDisplayed()) {
                closeWelcomePopup.click();
                System.out.println("✅ Closed Welcome popup successfully.");
            }
        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("ℹ️ Welcome popup not found, checking for failure card...");
            try {
                waitAndClick(failureCloseBtn, "Closed failure card successfully.");
            } catch (Exception ex) {
                System.out.println("❌ Failure card not found: " + ex.getMessage());
            }
        }
    }
    public void renewCTAClicked() {
        try {
            // Step 1: Click on Renew Listing CTA
            wait.until(ExpectedConditions.elementToBeClickable(renewCTA)).click();
            System.out.println("✅ Renew Listing CTA clicked successfully.");

            // Step 2: Switch to iframe
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[contains(@src,'refreshProperty.html')]")
            ));
            System.out.println("✅ Switched to 'refreshProperty' iframe successfully.");

            // Step 3: Open dropdown and select package
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(renewDropdown));
            dropdown.click();
            System.out.println("✅ '--Select--' dropdown clicked.");

            // Wait and select a package
            wait.until(ExpectedConditions.elementToBeClickable(renewPackage)).click();
            System.out.println("✅ Package selected successfully.");

            // Step 4: Click on Refresh CTA
            wait.until(ExpectedConditions.elementToBeClickable(Refresh)).click();
            System.out.println("✅ Refresh CTA clicked successfully.");

            // Optional: Switch back to default content after completing iframe operations
            driver.switchTo().defaultContent();
            System.out.println("✅ Switched back to default content.");
        } 
        catch (TimeoutException e) {
            System.err.println("❌ Element not found within timeout: " + e.getMessage());
        } 
        catch (NoSuchElementException e) {
            System.err.println("❌ Element not found in DOM: " + e.getMessage());
        } 
        catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
        }
    }
   


    // ==================== Utility Methods ====================

    private void waitAndClick(WebElement element, String successMessage) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            scrollAndClick(element);
            System.out.println("✅ " + successMessage);
        } catch (Exception e) {
            System.out.println("❌ Unable to click element: " + e.getMessage());
        }
    }

    /** Scrolls element into view and clicks via JavaScript */
    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
   
}
