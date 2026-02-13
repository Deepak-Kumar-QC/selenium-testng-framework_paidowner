package Pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.List;
import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.BasePage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class LoginPage extends BasePage{
	 private static final Logger log = LogManager.getLogger(LoginPage.class);
    WebDriver driver;
    WebDriverWait wait;
    
    @FindBy(xpath = "//label[text()='Agent/Builder']")
    private WebElement agent_Builder;

    @FindBy(id = "emailOrMobile")
    private WebElement emailOrMobile;

    @FindBy(id = "btnStep1")
    private WebElement nextButton;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "btnLogin")
    private WebElement loginButton;

    @FindBy(id = "captchaCodeSignIn")
    private WebElement captchaInput;

    @FindBy(xpath = "//div[@id='commentCaptchaErrSignIn']")
    private WebElement captchaError;

    @FindBy(xpath = "//button[text()='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "(//a[text()='Sign Up'])[2]")
    private WebElement signUp;

    @FindBy(xpath = "//img[@id='captchaImageSignIn']")
    private WebElement captchaImage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void selectAgent_Builder() {
        wait.until(ExpectedConditions.elementToBeClickable(agent_Builder)).click();
    }

    public void enterEmailOrMobile(String userName) {
        wait.until(ExpectedConditions.visibilityOf(emailOrMobile)).sendKeys(userName);
    }

    public void readCaptchaImage() throws Exception {

        int maxRetries = 50;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            System.out.println("Captcha attempt: " + attempt);

            WebElement captchaImage = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("(//div[@class='signup__captcha--graphic capta'])[1]")));

            // Screenshot captcha
            File src = captchaImage.getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(src);

            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Users\\Deepak.Kumar\\Downloads\\testdata");
            tesseract.setLanguage("eng");

            String text = tesseract.doOCR(image).replaceAll("[^a-zA-Z0-9]", "").trim();

            System.out.println("OCR Captcha text: " + text);
            
            if (text.length() < 4) {
                System.out.println("OCR text invalid, refreshing captcha...");
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@class='signup__captcha--ico-refresh']"))).click();
                Thread.sleep(1500);
                continue;
            }

            WebElement captchaInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("captchaCodeSignIn")));

            // Clear & type normally (restore original behavior)
            captchaInput.clear();
            captchaInput.click();
            Thread.sleep(300);
            captchaInput.sendKeys(text);

            // Submit
            driver.findElement(By.xpath("(//div[@class='m-login__fieldset'])[2]")).click();
            Thread.sleep(2000);

            // Check error
            List<WebElement> errorMessages = driver
                    .findElements(By.xpath("//*[contains(text(),'Please enter valid captcha')]"));

            if (errorMessages.isEmpty()) {
                System.out.println("Captcha verified successfully!");
                return;
            }

            // 🔥 IMPORTANT: refresh captcha before next retry
            System.out.println("Captcha invalid, refreshing and retrying...");
            captchaImage.click();
            Thread.sleep(1500);
        }

        throw new Exception("Captcha verification failed after retries");
    }
    public void enterPassword(String pass) {
        wait.until(ExpectedConditions.visibilityOf(password)).sendKeys(pass);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void clickOnContinueButton() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public void clickOnSignUp() {
        wait.until(ExpectedConditions.elementToBeClickable(signUp)).click();
    }
    public void performLogin() throws Exception {
        log.info("🔐 Logging in with user: {}", oconfig.getProperty("userName"));
        selectAgent_Builder();
        enterEmailOrMobile(oconfig.getProperty("userName"));
        readCaptchaImage();
        enterPassword(oconfig.getProperty("pass"));
        clickLoginButton();
        log.info("✅ Login successful for user: {}", oconfig.getProperty("userName"));
    }
}
