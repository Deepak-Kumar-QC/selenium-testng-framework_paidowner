package Pages;




import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.BasePage;

public class PaymentPage extends BasePage {
	private static final Logger log = LogManager.getLogger(PaymentPage.class);
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//article[starts-with(text(), 'MB')]")
	private WebElement orderid;

	@FindBy(xpath="//article[normalize-space(text())='Proceed to Pay']")
	private WebElement pay;
	
	@FindBy(xpath="//div[@class='payment-failure__head' and contains(., 'Payment Failure')]")
	private WebElement fail;
	@FindBy(xpath="//div[@class='discount-con__head']")
	private WebElement failmessage;
	@FindBy(xpath="//a[@class='btn__try-again']")
	private WebElement retry;
	@FindBy(xpath="//article[text()='Credit / Debit Card']")
	private WebElement credit;
	@FindBy(xpath="//input[@placeholder='Enter Card Number']")
	private WebElement cardNo;
	@FindBy(xpath="//input[@testid='edt_expiry_date']")
	private WebElement Expiry;
	@FindBy(xpath="//input[@testid='edt_cvv']")
	private WebElement cvv;
	
	
	public PaymentPage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
		this.wait=new WebDriverWait(driver, Duration.ofSeconds(15));
	}
		public void clickCredit() {
			log.info("PGI Landed");
			log.info("Order id Found on PGI page is :" + orderid.getText());
			
		}
			 
		
	public void EnterCard(String Card) {
		wait.until(ExpectedConditions.visibilityOf(cardNo));
		wait.until(ExpectedConditions.elementToBeClickable(cardNo)).sendKeys(Card);
		
	}
	public void EnterExpiry(String ExpiryNo) {
		wait.until(ExpectedConditions.visibilityOf(Expiry));
		wait.until(ExpectedConditions.elementToBeClickable(Expiry)).sendKeys(ExpiryNo);
		
	}
	public void EnterCvv(String cvvNo) {
		wait.until(ExpectedConditions.visibilityOf(cvv));
		wait.until(ExpectedConditions.elementToBeClickable(cvv)).sendKeys(cvvNo);
		
	
	}
	public void clickPay() {
		wait.until(ExpectedConditions.elementToBeClickable(pay)).click();
		log.info("Order Failed Successfully");
		ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
	}
	
	
		public void failurePage() {
			wait.until(ExpectedConditions.elementToBeClickable(fail));
			log.info("Payment gateway response :" + fail.getText());
			log.info("Failure screen message on UI :" + failmessage.getText());
			retry.click();
			log.info("Retried Payment Successfully");
		}
		 public void performPayment() {
		        log.info("💳 Performing payment using card details.");
		        clickCredit();
		        EnterCard(oconfig.getProperty("Card"));
		        EnterExpiry(oconfig.getProperty("ExpiryNo"));
		        EnterCvv(oconfig.getProperty("cvvNo"));
		        clickPay();
		        log.info("✅ Payment process executed successfully.");
		    }
	}

