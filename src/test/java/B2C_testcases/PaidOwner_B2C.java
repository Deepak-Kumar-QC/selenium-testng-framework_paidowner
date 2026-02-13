package B2C_testcases;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Base.BasePage;
import Pages.LoginPage;
import Pages.MatchingBuyerPage;
import Pages.OrderPage;
import Pages.OwnerDashboardPage;
import Pages.PaymentPage;
import Pages.ResponsePage;
import Reports.TestListener;
import utils.ApiUtils;
import utils.DbUtils;

@Listeners(TestListener.class)
public class PaidOwner_B2C extends BasePage {

	private static final Logger log = LogManager.getLogger(PaidOwner_B2C.class);

	private LoginPage login;
	private OrderPage orderPage;
	private PaymentPage payment;
	private OwnerDashboardPage ownerPage;
	private MatchingBuyerPage matchingBuyerPage;
	private ResponsePage responsePage;
	private long ubirfnum;

	// ===================== SETUP =====================

	@BeforeMethod(alwaysRun = true)
	public void setUpBrowser() throws Exception {

		log.info("🚀 Browser setup started");

		loadConfig();
		setup();

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		new WebDriverWait(driver, Duration.ofSeconds(20));

		initializePages();
		login.performLogin();

		/*ubirfnum = ApiUtils.getUserRefNoFromAPI(driver);
		log.info("UBI received in test class: {}", ubirfnum);

		Assert.assertTrue(ubirfnum > 0, "❌ Invalid ubirfnum");*/

		log.info("✅ User logged in successfully");
	}

	private void initializePages() {
		login = new LoginPage(driver);
		orderPage = new OrderPage(driver);
		payment = new PaymentPage(driver);
		ownerPage = new OwnerDashboardPage(driver);
		matchingBuyerPage = new MatchingBuyerPage(driver);
		responsePage = new ResponsePage(driver);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			log.info("🧹 Browser closed");
		}
	}

	// ===================== COMMON HELPERS =====================

	/** Safely navigates to Owner Dashboard */
	private void reachOwnerDashboard() {
		orderPage.closeFailure();
		ownerPage.closeRenewalPopup();
		ownerPage.clickownerswitch();
		SwitchOnTab();

	}

	/** Selects property safely */
	private void selectOwnerProperty() {
		String propertyId = oconfig.getProperty("id");
		String location = oconfig.getProperty("location");
		if (propertyId == null || location == null) {
			Assert.fail("❌ Property details missing in config.properties");
		}

		String propertyName = "Property Id - " + propertyId + ", " + location;

		log.info("🏠 Selecting property: {}", propertyName);

		ownerPage.selectPropertyByVisibleText(propertyName);
		orderPage.closeFailure();
		ownerPage.closeRenewalPopup();
	}

	// ===================== TEST CASES =====================

	@Test(priority = 1, description = "Payment from Failure Popup - Order Dashboard")
	public void verifyFailurePopupOrderPage() {
		try {
			orderPage.handleFailureCard();
			payment.performPayment();
			payment.failurePage();
		/*	Assert.assertTrue(
	                waitForDbEntry(ubirfnum, 5, 5),
	                "Latest DB entry not found for userRefNo: " + ubirfnum);
			//.fetchTpustAndTpuseDataUsingUbi(ubirfnum);*/
			payment.performPayment();

			log.info("✅ Failure popup payment verified from Order Dashboard");
		} catch (Exception e) {
			log.error("❌ Failure popup payment failed (Order Dashboard)", e);
			Assert.fail(e.getMessage());
		}
	}
	private boolean waitForDbEntry(long ubirfnum, int maxAttempts, int waitSeconds) {

	    for (int i = 1; i <= maxAttempts; i++) {
	        if (DbUtils.isUserRefNoPresentInDB(ubirfnum)) {
	            log.info("✅ DB entry found on attempt {}", i);
	            return true;
	        }

	        log.info("⏳ DB entry not found. Retrying... Attempt {}", i);

	        try {
	            Thread.sleep(waitSeconds * 1000);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	    }

	    return false;
	}

	@Test(priority = 2, description = "Renew Flow from Order Dashboard")
	public void verifyRenewOrderDashboard() {
		try {
			orderPage.closeFailure();
			ownerPage.clickRenewNow();
			payment.performPayment();

			log.info("✅ Renew flow verified from Order Dashboard");
		} catch (Exception e) {
			log.error("❌ Renew flow failed (Order Dashboard)", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test(priority = 3, description = "Renew More Package from Order Dashboard")
	public void verifyRenewMorePackageOrderDashboard() {
		try {
			orderPage.closeFailure();
			ownerPage.clickViewMorePackages();
			ownerPage.printTitaniumPackageDetails();
			ownerPage.selectTitaniumPackage();
			ownerPage.printCartDetailsAndProceedToPayment();
			payment.performPayment();

			log.info("✅ Renew more package verified from Order Dashboard");
		} catch (Exception e) {
			log.error("❌ Renew more package failed (Order Dashboard)", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test(priority = 4, description = "Refresh / Renew Property from Owner Dashboard")
	public void verifyRefreshPropertyOwnerPage() {
		try {
			reachOwnerDashboard();
			selectOwnerProperty();
			ownerPage.renewCTAClicked();
			payment.performPayment();

			log.info("✅ Refresh/Renew property verified from Owner Dashboard");
		} catch (Exception e) {
			log.error("❌ Refresh property flow failed", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test(priority = 5, description = "Matching Buyer Flow from Owner Dashboard")
	public void verifyMatchingBuyerOwnerPage() {
		try {
			reachOwnerDashboard();
			selectOwnerProperty();
			matchingBuyerPage.navigateMatching();
			payment.performPayment();

			log.info("✅ Matching Buyer flow verified");
		} catch (Exception e) {
			log.error("❌ Matching Buyer flow failed", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test(priority = 6, description = "Response Page B2C Flow")
	public void verifyResponseOwnerPage() {
		try {
			reachOwnerDashboard();
			selectOwnerProperty();
			responsePage.navigateResponse();
			payment.performPayment();

			log.info("✅ Response page B2C flow verified");
		} catch (Exception e) {
			log.error("❌ Response page flow failed", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test(priority = 7, description = "Failure Popup Payment from Owner Dashboard")
	public void verifyFailurePopupOwnerPage() {
		try {
			reachOwnerDashboard();
			ownerPage.closeWelcomePopupIfVisible();
			orderPage.handleFailureCard();
			payment.performPayment();

			log.info("✅ Failure popup payment verified from Owner Dashboard");
		} catch (Exception e) {
			log.error("❌ Failure popup payment failed (Owner Dashboard)", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test(priority = 8, description = "Renew Flow from Owner Dashboard")
	public void verifyRenewOwnerDashboard() {
		try {
			reachOwnerDashboard();
			ownerPage.closeWelcomePopupIfVisible();
			ownerPage.clickRenewNow();
			payment.performPayment();

			log.info("✅ Renew flow verified from Owner Dashboard");
		} catch (Exception e) {
			log.error("❌ Renew flow failed (Owner Dashboard)", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test(priority = 9, description = "Renew More Package from Owner Dashboard")
	public void verifyRenewMorePackageOwnerDashboard() {
		try {
			reachOwnerDashboard();
			ownerPage.closeWelcomePopupIfVisible();
			orderPage.closeFailure();
			ownerPage.clickViewMorePackages();
			ownerPage.printTitaniumPackageDetails();
			ownerPage.selectTitaniumPackage();
			ownerPage.printCartDetailsAndProceedToPayment();
			payment.performPayment();

			log.info("✅ Renew more package verified from Owner Dashboard");
		} catch (Exception e) {
			log.error("❌ Renew more package failed (Owner Dashboard)", e);
			Assert.fail(e.getMessage());
		}
	}
}
