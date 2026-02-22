package Listner;
import org.testng.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import GenericUtilities.ThreadLocalClass;

public class ListnerClass implements ITestListener, IClassListener {

    private ExtentReports report;

    @Override
    public void onBeforeClass(ITestClass testClass) {
        ExtentTest classLevel = report.createTest(testClass.getName());
        ThreadLocalClass.setClassLevel(classLevel);
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        report.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest testLevel = ThreadLocalClass.getClassLevel().createNode(result.getName());
        ThreadLocalClass.setTestLevel(testLevel);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest testLevel = ThreadLocalClass.getTestLevel();
        if (testLevel != null) {
            testLevel.log(Status.PASS, "Test Case Passed: " + result.getName());
            System.out.println("Test case success "  + result.getName());
        }
        report.flush();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest testLevel = ThreadLocalClass.getTestLevel();
        if (testLevel != null) {
            testLevel.log(Status.FAIL, "Test Case Failed: " + result.getName());
            System.out.println("Test case Failed "  + result.getName());
            testLevel.log(Status.FAIL, result.getThrowable());
        }
        report.flush();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest testLevel = ThreadLocalClass.getTestLevel();
        if (testLevel != null) {
            testLevel.log(Status.SKIP, "Test Case Skipped: " + result.getName());
            System.out.println("Test case skipped "  + result.getName());
            testLevel.log(Status.SKIP, result.getThrowable());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        ExtentSparkReporter spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/ExtentReports/ExtentReports_" + dtf.format(now) + ".html");
        spark.config().setDocumentTitle("Test Execution Report");
        spark.config().setReportName("Test Case Execution Report");
        spark.config().setTheme(Theme.DARK);

        report = new ExtentReports();
        report.attachReporter(spark);
        report.setSystemInfo("BuildNo", "5.1");
        report.setSystemInfo("Env", "Pre-Prod");
    }

    @Override
    public void onFinish(ITestContext context) {
        report.flush();
    }

    public static void reportLog(String message) {
        ExtentTest currentTest = ThreadLocalClass.getTestLevel();
        if (currentTest != null) {
            currentTest.log(Status.INFO, message);
        } else {
            System.out.println("Warning: No current test context available for logging.");
        }
    }

    public static void reportLogFail(String message) {
        ExtentTest currentTest = ThreadLocalClass.getTestLevel();
        if (currentTest != null) {
            currentTest.log(Status.FAIL, message);
        } else {
            System.out.println("Warning: No current test context available for failure logging.");
        }
    }

    public static ExtentTest testLevelLog() {
        return ThreadLocalClass.getTestLevel();
    }

}


