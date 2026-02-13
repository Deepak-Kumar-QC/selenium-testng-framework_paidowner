package Reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class GenerateReport {

    private static ExtentReports extent;
    private static String reportPath;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    public static void createInstance() {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        reportPath = System.getProperty("user.dir") + "\\Report\\test-output\\B2C_OwnerDashboard_WEB_Report_" + timestamp + ".html";
        createInstance(reportPath);
    }

    public static ExtentReports createInstance(String fileName) {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);
            sparkReporter.config().setTimelineEnabled(true);
            sparkReporter.config().setReportName("B2C Paid Owner Website Sanity Test Report");
            sparkReporter.config().setDocumentTitle("OwnerDashboard Web B2C Automation");
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();	
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("QA Engineer", "Deepak Kumar");
            extent.setSystemInfo("QC Environment", "MB Production");
        }
        return extent;
    }

    public static String getReportPath() {
        return reportPath;
    }
}
