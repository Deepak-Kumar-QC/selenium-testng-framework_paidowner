package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BasePage {
	
	private static final Logger log = LogManager.getLogger(BasePage.class);
    public static WebDriver driver;
    public WebDriverWait wait;
    public static Properties oconfig;


    public void setup() throws Exception {
        WebDriverManager.chromedriver().setup();
        loadConfig();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-popup-blocking");
     
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-features=NetworkService,NetworkServiceInProcess");
        options.addArguments("--disable-features=MediaRouter,CastMediaRouteProvider,LocalNetworkCodeCache,OptimizationHintsFetching,OptimizationHints,OptimizationHintsValidating");
        options.addArguments("--disable-device-discovery-notifications");
        options.addArguments("--disable-cast");
        options.addArguments("--disable-cast-streaming-hw-encoding");
        options.addArguments("--disable-cast-streaming-media");
        options.addArguments("--disable-print-preview");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--start-maximized");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        log.info("✅ Browser launched and maximized");

        String url = oconfig.getProperty("url");
        log.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    public void ScrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        log.info("Scrolled to element: {}", element);
    }
    

    public void SwitchOnTab() {
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> windowList = new ArrayList<>(windowHandles);
        driver.switchTo().window(windowList.get(windowList.size() - 1));
        log.info("Switched to new browser tab");
    }

    public void ReloadPage()
    {
        driver.navigate().refresh();
        log.info("Page reloaded");

    }
    
    public void loadConfig() throws Exception {
        FileInputStream file = new FileInputStream("C:\\Users\\Deepak.Kumar\\eclipse-workspace\\PaidOwner\\src\\main\\resources\\config.properties");
        oconfig = new Properties();
        oconfig.load(file);
        file.close();
        log.info("✅ Freeowner Config file loaded");
    }
    
    
    public String captureScreenshot(String testName) throws IOException {
    	try {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = "C:\\Users\\Deepak.Kumar\\eclipse-workspace\\Freeowner\\screenshots\\" + testName + "_" + timestamp + ".png";
        File destination = new File(filePath);
        FileUtils.copyFile(screenshot, destination);
        System.out.println("📸 Screenshot saved: " + filePath);
        return filePath;
        
    	} catch (IOException e) {
    		 log.error("❌ Failed to take screenshot: {}", e.getMessage(), e);
            throw e;  // Let the caller handle it
        } catch (Exception e) {
        	log.error("❌ Unexpected error during screenshot: {}", e.getMessage(), e);
        	 return null;  // Explicit return makes the flow clearer
        }

       
    }
    public void writeResultToExcel(String testName, String status) {
        String filePath = "C:\\Users\\Deepak.Kumar\\Documents\\Testresults.csv";
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.append(testName).append(",").append(status).append("\n");
            writer.flush();
            log.info("📄 Test result saved: {} - {}", testName, status);
        } catch (IOException e) {
        	log.error("❌ Error writing result to CSV: {}", e.getMessage(), e);
        }
    }
    public void goBack() {
    	 driver.navigate().back();
    }
    String originalTab;
    
    public void storeOriginalTab() {
        originalTab = driver.getWindowHandle();
    }

    public void switchToNewTab() {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalTab)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public void switchBackToOriginalTab() {
        driver.close(); // Close current tab
        driver.switchTo().window(originalTab);
       
    }
    public void clickAnywhere() {
        Actions actions = new Actions(driver);
        actions.moveByOffset(50, 50).click().build().perform();
    }
    
}
   

       
