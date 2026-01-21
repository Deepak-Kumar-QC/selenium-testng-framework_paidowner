package B2C_testcases;


import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Base.BasePage;
import Pages.LoginPage;
import Pages.MatchingBuyerPage;
import Pages.OrderPage;
import Pages.OwnerDashboardPage;
import Pages.PaymentPage;
import Pages.ResponsePage;

public class PaidOwner_B2C extends BasePage {

    private static final Logger log = LogManager.getLogger(PaidOwner_B2C.class);
    private LoginPage login;
    private OrderPage orderPage;
    private PaymentPage payment;
    private OwnerDashboardPage ownerPage;
    private MatchingBuyerPage matchingBuyerPage;
    private ResponsePage responsePage;
    @BeforeMethod
    public void setUpBrowser() throws Exception {
        try {
            log.info("✅ Browser setup started.");
            setup();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            new WebDriverWait(driver, Duration.ofSeconds(20));

            initializePages();
            login.performLogin();

            log.info("✅ Browser setup completed and user logged in successfully.");
        } catch (Exception e) {
            log.error("❌ Error during setup: {}", e.getMessage(), e);
            throw e;
        }
    }

   
    public void initializePages() {
        login = new LoginPage(driver);
        payment = new PaymentPage(driver);
        orderPage = new OrderPage(driver);
        ownerPage = new OwnerDashboardPage(driver);
        matchingBuyerPage = new MatchingBuyerPage(driver);
        responsePage= new ResponsePage(driver);
        log.info("✅ Page objects initialized successfully before test method.");
    }
    
    @AfterMethod
	public void tearDown() {
		if (driver != null)
			driver.quit();
	}
    @Test(priority = 1, description = "Validate Payment flow from Failure Popup- Order Dashboard Page")
    public void verifyFailurePopup() {
        log.info("🚀 Starting Order Dashboard failure Popup Test...");

        try {
            orderPage.handleFailureCard();
            payment.performPayment();
            payment.failurePage(); // handles failure scenario if any
            payment.performPayment();
         

            log.info("✅ Successfully Payment flow verified from Failure Popup- Order Dashboard Page");
        } catch (Exception e) {
            log.error("❌ Error during Payment flow from Failure Popup- Order Dashboard Page: {}", e.getMessage(), e);
        }
    }
    @Test(priority = 2, description = "Validate Renew Flow from Order-Dashboard")
    public void verifyB2CRenewal() {
        log.info("🚀 Starting Order Dashboard Renew CTA Test...");

        try {
        	 orderPage.closeFailure();
        	 ownerPage.clickRenewNow();
        	 payment.performPayment();
             
            log.info("✅ Renew Flow from Order-Dashboard has executed successfully.");
        } catch (Exception e) {
            log.error("❌ Error during Renew Flow from Order-Dashboard: {}", e.getMessage(), e);
        } 
    }
    @Test(priority = 3, description = "Validate Renew Flow from Order-Dashboard")
    public void verifyB2CRenewalMorePackage() {
        log.info("🚀 Starting Order Dashboard Renew more package Test...");

        try {
        	 orderPage.closeFailure();
        	 ownerPage.clickViewMorePackages();
        	 ownerPage.printTitaniumPackageDetails();
             ownerPage.selectTitaniumPackage();
             ownerPage.printCartDetailsAndProceedToPayment();
             payment.performPayment();
             
            log.info("✅ Renew more package Flow from Order-Dashboard has executed successfully.");
        } catch (Exception e) {
            log.error("❌ Error during Renew more package Flow from Order-Dashboard: {}", e.getMessage(), e);
        } 
    }
    @Test(priority = -1, description = "Validate Owner Dashboard property selection and Refresh popup handling")
    public void verifyiapprove() {
        log.info("🚀 Starting Order Dashboard Renew more package Test...");

        try {
        	orderPage.closeFailure();
        	ownerPage.closeRenewalPopup();
        	ownerPage.clickownerswitch();
        	SwitchOnTab();
            ownerPage.selectPropertyByVisibleText("Property Id - 77692281, Ashok Nagar, Bangalore");
            orderPage.closeFailure();
            ownerPage.closeRenewalPopup();

            log.info("✅ Property selection and renewal popup handling verified successfully.");
        } catch (Exception e) {
            log.error("❌ Error during Owner Dashboard property selection: {}", e.getMessage(), e);
        }
        ownerPage.renewCTAClicked();
        payment.performPayment();
        ownerPage.closeWelcomePopupIfVisible();
        orderPage.closeFailure1();
        ownerPage.closeRenewalPopup();
        ownerPage.selectPropertyByVisibleText("Property Id - 77692281, Ashok Nagar, Bangalore");
        orderPage.closeFailure1();
        ownerPage.closeRenewalPopup();
        clickAnywhere();
        
    }

}
